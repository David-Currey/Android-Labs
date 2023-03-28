package algonquin.cst2335.curr0263;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * this class contains the main Page of the application and related methods
 *
 * @author David Currey
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    /**
     * This holds the text at the centre of the screen
     */
    TextView tv = null;

    /**
     * this holds the password entered by the user
     */
    EditText et = null;

    /**
     * this is the login button
     */
    Button btn = null;

    /**
     * name of city input into search bar by user
     **/
    String cityName;

    /**
     * Volley object used to connect to server
     **/
    RequestQueue queue = null;

    /**
     * image used for forecast
     **/
    Bitmap image;

    TextView temp;
    TextView maxTemp;
    TextView minTemp;
    TextView humid;
    TextView desc;
    ImageView icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.textView);
        et = findViewById(R.id.editText);
        btn = findViewById(R.id.button);
        temp = findViewById(R.id.temp);
        maxTemp = findViewById(R.id.maxtemp);
        minTemp = findViewById(R.id.mintemp);
        humid = findViewById(R.id.humidity);
        desc = findViewById(R.id.description);
        icon = findViewById(R.id.icon);

        queue = Volley.newRequestQueue(this);

        btn.setOnClickListener(clk ->
        {
            cityName = et.getText().toString();

            String stringURL = null;
            try {
                stringURL = "https://api.openweathermap.org/data/2.5/weather?q="
                        + URLEncoder.encode(cityName, "UTF-8")
                        + "&appid=7e943c97096a9784391a981c4d878b22&units=metric";
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, stringURL, null,
                    (response) ->
                    {

                        try {
                            JSONObject coord = response.getJSONObject("coord");

                            JSONArray weatherArray = response.getJSONArray("weather");
                            JSONObject position0 = weatherArray.getJSONObject(0);

                            String description = position0.getString("description");
                            String iconName = position0.getString("icon");

                            JSONObject mainObject = response.getJSONObject("main");
                            double current = mainObject.getDouble("temp");
                            double min = mainObject.getDouble("temp_min");
                            double max = mainObject.getDouble("temp_max");
                            int humidity = mainObject.getInt("humidity");

                            String pathname = getFilesDir() + "/" + iconName + ".png";
                            File file = new File(pathname);
                            if (file.exists()) {
                                image = BitmapFactory.decodeFile(pathname);
                            } else {
                                ImageRequest imgReq = new ImageRequest("https://openweathermap.org/img/w/" + iconName + ".png", new Response.Listener<Bitmap>() {
                                    @Override
                                    public void onResponse(Bitmap bitmap) {
                                        try {
                                            image = bitmap;
                                            image.compress(Bitmap.CompressFormat.PNG, 100,
                                                    MainActivity.this.openFileOutput(iconName + ".png", Activity.MODE_PRIVATE));
                                            icon.setImageBitmap(image);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, 1024, 1024, ImageView.ScaleType.CENTER, null, (error) -> {
                                    Toast.makeText(MainActivity.this, "" + error, Toast.LENGTH_SHORT).show();
                                });
                                queue.add(imgReq);
                            }

                            runOnUiThread(() -> {
                                temp.setText("the current tempreture is " + current);
                                temp.setVisibility(View.VISIBLE);

                                maxTemp.setText("the max tempreture is " + max);
                                maxTemp.setVisibility(View.VISIBLE);

                                minTemp.setText("the min tempreture is " + min);
                                minTemp.setVisibility(View.VISIBLE);

                                humid.setText("the humidity is " + humidity + "%");
                                humid.setVisibility(View.VISIBLE);

                                desc.setText(description);
                                desc.setVisibility(View.VISIBLE);

                                icon.setImageBitmap(image);
                                icon.setVisibility(View.VISIBLE);

                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    (error) -> {
                    });
            queue.add(request);
        });
    }

    /**
     * this function checks the complexity of a users password
     *
     * @param pw the String object that we are checking
     * @return returns true if the password is complex enough
     */
    boolean checkPasswordComplexity(String pw) {
        boolean foundUpperCase, foundLowerCase, foundNumber, foundSpecial;
        foundUpperCase = foundLowerCase = foundNumber = foundSpecial = false;

        for (int i = 0; i < pw.length(); i++) {
            char c = pw.charAt(i);
            if (Character.isUpperCase(c) == true) foundUpperCase = true;
            else if (Character.isLowerCase(c) == true) foundLowerCase = true;
            else if (Character.isDigit(c) == true) foundNumber = true;
            else if (isSpecialCharacter(c) == true) foundSpecial = true;
        }

        if (!foundUpperCase) {

            Toast.makeText(this, "missing uppercase letter", Toast.LENGTH_SHORT).show();

            return false;
        } else if (!foundLowerCase) {
            Toast.makeText(this, "missing lowercase letter", Toast.LENGTH_SHORT).show();

            return false;
        } else if (!foundNumber) {
            Toast.makeText(this, "missing number", Toast.LENGTH_SHORT).show();

            return false;
        } else if (!foundSpecial) {
            Toast.makeText(this, "missing special character", Toast.LENGTH_SHORT).show();

            return false;
        } else

            return true;
    }

    /**
     * checks if a character is special
     *
     * @param c character beign checked
     * @return true if c is a scpecial character
     */
    boolean isSpecialCharacter(char c) {
        switch (c) {
            case '#':
            case '?':
            case '*':
            case '$':
            case '%':
            case '^':
            case '&':
            case '!':
            case '@':
                return true;
            default:
                return false;
        }
    }
}