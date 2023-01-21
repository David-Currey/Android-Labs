package algonquin.cst2335.curr0263.data.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import algonquin.cst2335.curr0263.R;
import algonquin.cst2335.curr0263.data.data.MainViewModel;
import algonquin.cst2335.curr0263.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding variableBinding;
    private MainViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        model = new ViewModelProvider(this).get(MainViewModel.class);

        variableBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(variableBinding.getRoot());

        variableBinding.mybutton.setOnClickListener(click ->
        {
            model.editString.postValue(variableBinding.myedittext.getText().toString());
        });

        model.editString.observe(this, s ->
        {
            variableBinding.textview.setText("Your edit text has: " + s);
        });

        model.isSelected.observe(this, selected ->
        {
            variableBinding.checkBox.setChecked(selected);
            variableBinding.radioButton.setChecked(selected);
            variableBinding.switch1.setChecked(selected);
        });

       variableBinding.checkBox.setOnCheckedChangeListener((btn, isChecked)->
        {
            Toast.makeText(this, "The value is now " + isChecked, Toast.LENGTH_SHORT).show();
        });

        variableBinding.switch1.setOnCheckedChangeListener((btn, isChecked)->
        {
            Toast.makeText(this, "The value is now " + isChecked, Toast.LENGTH_SHORT).show();
        });

        variableBinding.radioButton.setOnCheckedChangeListener((btn, isChecked)->
        {
            Toast.makeText(this, "The value is now " + isChecked, Toast.LENGTH_SHORT).show();
        });

       variableBinding.myimagebutton.setOnClickListener(click ->
       {
           Toast.makeText(this, "the width = " + variableBinding.myimagebutton.getWidth()
                   + " and the height = " + variableBinding.myimagebutton.getHeight(), Toast.LENGTH_SHORT).show();
       });


    }
}