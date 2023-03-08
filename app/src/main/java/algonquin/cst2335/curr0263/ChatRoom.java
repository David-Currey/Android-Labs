package algonquin.cst2335.curr0263;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import algonquin.cst2335.curr0263.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.curr0263.databinding.ReceivedMessageBinding;
import algonquin.cst2335.curr0263.databinding.SentMessageBinding;

public class ChatRoom extends AppCompatActivity
{
    class MyRowHolder extends RecyclerView.ViewHolder
    {
        TextView messageText;
        TextView timeText;

        public MyRowHolder(@NonNull View itemView)
        {
            super(itemView);
            messageText = itemView.findViewById(R.id.message);
            timeText = itemView.findViewById(R.id.time);
        }
    }

    ActivityChatRoomBinding binding;
    ArrayList<ChatMessage> messages = new ArrayList<>();
    chatRoomViewModel chatModel;
    RecyclerView.Adapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        chatModel = new ViewModelProvider(this).get(chatRoomViewModel.class);
        messages = chatModel.messages.getValue();

        if(messages == null)
        {
            chatModel.messages.postValue( messages = new ArrayList<ChatMessage>());
        }

        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));

        binding.send.setOnClickListener(click -> {

            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MM-yyyy hh-mm-ss a");
            String currentDateAndTime = sdf.format(new Date());
            String messageText = binding.editText.getText().toString();

            messages.add(new ChatMessage(messageText,currentDateAndTime, true ));
            myAdapter.notifyItemInserted(messages.size()-1);
            binding.editText.setText("");
        });

        binding.receive.setOnClickListener(click -> {

            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MM-yyyy hh-mm-ss a");
            String currentDateAndTime = sdf.format(new Date());
            String messageText = binding.editText.getText().toString();

            messages.add(new ChatMessage(messageText,currentDateAndTime, false ));
            myAdapter.notifyItemInserted(messages.size()-1);
            binding.editText.setText("");
        });

        binding.recycleView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>()
        {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                if (viewType == 0)
                {
                    SentMessageBinding sent = SentMessageBinding.inflate(getLayoutInflater());
                    return new MyRowHolder(sent.getRoot());
                }
                else
                {
                    ReceivedMessageBinding received = ReceivedMessageBinding.inflate(getLayoutInflater());
                    return new MyRowHolder((received.getRoot()));
                }
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position)
            {
                holder.messageText.setText("");
                holder.timeText.setText("");

                ChatMessage msg = messages.get(position);

                holder.messageText.setText(msg.message);
                holder.timeText.setText(msg.timeSent);
            }

            @Override
            public int getItemCount()
            {
                return messages.size();
            }

            @Override
            public int getItemViewType(int position)
            {
                ChatMessage msg = messages.get(position);

                if (msg.isSend == true)
                {
                    return 0;
                }
                else
                {
                    return 1;
                }
            }
        });
    }
}

