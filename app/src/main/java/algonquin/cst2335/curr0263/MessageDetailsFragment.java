package algonquin.cst2335.curr0263;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import algonquin.cst2335.curr0263.databinding.DetailsLayoutBinding;

public class MessageDetailsFragment extends Fragment{

    ChatMessage selected;

    public MessageDetailsFragment(ChatMessage m)
    {
        selected = m;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        DetailsLayoutBinding binding = DetailsLayoutBinding.inflate(inflater);

        String isSent;

        if(selected.isSend = true) isSent = "sent";
        else isSent = "receieved";

        binding.message.setText(selected.message);
        binding.time.setText(selected.timeSent);
        binding.databaseID.setText("id = " + selected.id);
        binding.receive.setText(isSent);
        return binding.getRoot();
    }

}
