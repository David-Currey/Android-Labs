package algonquin.cst2335.curr0263;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import algonquin.cst2335.curr0263.databinding.ActivityMainBinding;

/** this class contains the main Page of the application and related methods
 * @author David Currey
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    /** This holds the text at the centre of the screen */
    TextView tv = null;

    /** this holds the password entered by the user */
    EditText et = null;

    /** this is the login button */
    Button btn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.textView);
        et = findViewById(R.id.editText);
        btn = findViewById(R.id.button);

        btn.setOnClickListener( clk->
        {
            String password = et.getText().toString();
            boolean pwd = checkPasswordComplexity(password);

            if (pwd == true)
            {
                tv.setText("your password meets the requirements");
            }
            else
            {
                tv.setText("you shall not pass!");
            }
        });

    }

    /** this function checks the complexity of a users password
     *
     * @param pw the String object that we are checking
     * @return returns true if the password is complex enough
     */
    boolean checkPasswordComplexity(String pw)
    {
        boolean foundUpperCase, foundLowerCase, foundNumber, foundSpecial;
        foundUpperCase = foundLowerCase = foundNumber = foundSpecial = false;

        for (int i = 0; i < pw.length(); i++)
        {
            char c = pw.charAt(i);
            if (Character.isUpperCase(c) == true) foundUpperCase = true;
            else if (Character.isLowerCase(c) == true) foundLowerCase = true;
            else if (Character.isDigit(c) == true) foundNumber = true;
            else if (isSpecialCharacter(c) == true) foundSpecial = true;
        }

        if(!foundUpperCase)
        {

            Toast.makeText(this, "missing uppercase letter", Toast.LENGTH_SHORT).show();

            return false;
        }

        else if( ! foundLowerCase)
        {
            Toast.makeText(this, "missing lowercase letter", Toast.LENGTH_SHORT).show();

            return false;
        }

        else if( ! foundNumber)
        {
            Toast.makeText(this, "missing number", Toast.LENGTH_SHORT).show();

            return false;
        }

        else if(! foundSpecial)
        {
            Toast.makeText(this, "missing special character", Toast.LENGTH_SHORT).show();

            return false;
        }
        else

            return true;
    }

    /** checks if a character is special
     *
     * @param c character beign checked
     * @return true if c is a scpecial character
     */
    boolean isSpecialCharacter(char c)
    {
        switch(c)
        {
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