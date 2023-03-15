package algonquin.cst2335.curr0263;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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

            itemView.setOnClickListener(clk ->
            {
                int position = getAbsoluteAdapterPosition();
                AlertDialog.Builder builder = new AlertDialog.Builder( ChatRoom.this );

                builder.setMessage("do you want to delete the message: " + messageText.getText())
                .setTitle("Question:")
                .setNegativeButton("No", (dialog, cl) -> {})
                .setPositiveButton("Yes", (dialog, cl) ->
                {
                    ChatMessage m = messages.get(position);

                    Executor thread = Executors.newSingleThreadExecutor();
                    thread.execute( () -> {
                        mDAO.deleteMessage(m);
                    });
                    messages.remove(position);
                    myAdapter.notifyItemRemoved(position);

                    Snackbar.make(messageText, "you deleted message #" + position, Snackbar.LENGTH_LONG)
                            .setAction("Undo", click -> {
                                messages.add(position, m);
                                myAdapter.notifyItemInserted(position);
                            })
                            .show();
                }).create().show();
            });

            messageText = itemView.findViewById(R.id.message);
            timeText = itemView.findViewById(R.id.time);
        }
    }

    ActivityChatRoomBinding binding;
    ArrayList<ChatMessage> messages = new ArrayList<>();
    chatRoomViewModel chatModel;
    RecyclerView.Adapter myAdapter;
    ChatMessageDAO mDAO;
    ArrayList<ChatMessage> messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        chatModel = new ViewModelProvider(this).get(chatRoomViewModel.class);
        messages = chatModel.messages.getValue();

        //open database
        MessageDatabase db = Room.databaseBuilder(getApplicationContext(),
                MessageDatabase.class, "database-name").build();
        mDAO = db.cmDAO();
        
        // load old messages

        if(messages == null)
        {
            chatModel.messages.setValue(messages = new ArrayList<>());

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                messages.addAll( mDAO.getAllMessages() ); //Once you get the data from database

                runOnUiThread( () ->  binding.recycleView.setAdapter( myAdapter )); //You can then load the RecyclerView
            });
        }

//        if(messages == null)
//        {
//            chatModel.messages.postValue( messages = new ArrayList<ChatMessage>());
//        }

        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));

        binding.send.setOnClickListener(click -> {

            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MM-yyyy hh-mm-ss a");
            String currentDateAndTime = sdf.format(new Date());
            String messageText = binding.editText.getText().toString();

            ChatMessage newMessage = new ChatMessage(messageText, currentDateAndTime, true);

            // insert into db
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute( () -> {
                   long id = mDAO.insertMessage(newMessage);
                   newMessage.id = id;
            });

            messages.add(newMessage);
            myAdapter.notifyItemInserted(messages.size()-1);
            binding.editText.setText("");
        });

        binding.receive.setOnClickListener(click -> {

            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MM-yyyy hh-mm-ss a");
            String currentDateAndTime = sdf.format(new Date());
            String messageText = binding.editText.getText().toString();

            ChatMessage newMessage = new ChatMessage(messageText, currentDateAndTime, false);

            // insert into db
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute( () -> {
                long id = mDAO.insertMessage(newMessage);
                newMessage.id = id;
            });

            messages.add(newMessage);
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

