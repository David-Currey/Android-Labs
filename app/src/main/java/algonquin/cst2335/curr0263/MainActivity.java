package algonquin.cst2335.curr0263;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onDestroy() {
        Log.w("MainActivity", "In onDestroy() - memory being freed");
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        Log.w("MainActivity", "In onStop() - Application is no longer visible");
        super.onStop();
    }

    @Override
    protected void onPause() {
        Log.w("MainActivity", "In onPause() - Application no longer responding to user input");
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.w("MainActivity", "In onResume() - Application now responding to user input");
        super.onResume();
    }

    @Override
    protected void onStart() {
        Log.w("MainActivity", "In onStart() - Application is now visible on screen");
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.w("MainActivity", "In onCreate() - Loading Widgets");

        Button btn = findViewById(R.id.loginButton);
        EditText et = findViewById(R.id.emailEditText);

        SharedPreferences prefs = getSharedPreferences("myData", Context.MODE_PRIVATE);

        String name = prefs.getString("EmailAddress","");
        et.setText(name);

        Intent nextPage = new Intent(MainActivity.this, SecondActivity.class);
        btn.setOnClickListener(clk ->
        {

            nextPage.putExtra("EmailAddress", et.getText().toString());
            startActivity(nextPage);

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("EmailAddress", et.getText().toString());
            editor.apply();
        });
    }
}