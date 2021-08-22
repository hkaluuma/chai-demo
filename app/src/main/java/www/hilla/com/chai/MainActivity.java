package www.hilla.com.chai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class MainActivity extends AppCompatActivity {
    TextView welcometxv;
    //Button btn_login;
    String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //capturing the ids from the interfaces
        welcometxv = (TextView)findViewById(R.id.welcome_txv);
        YoYo.with(Techniques.Shake).duration(4000).repeat(8).playOn(welcometxv);
        //button codes
        //btn_login = (Button)findViewById(R.id.login_btn);
        Button btn_login = findViewById(R.id.login_btn);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //capturing ids of the edit text fields
                EditText username_edtx = findViewById(R.id.edtx_username);
                EditText password_edtx = findViewById(R.id.edtx_password);
                //capturing the input
                username = username_edtx.getText().toString();
                password = password_edtx.getText().toString();

                if(username.isEmpty()){
                    username_edtx.setError("Username is missing");
                }else if(password.isEmpty()){
                    password_edtx.setError("Password is missing");
                }else{
                    YoYo.with(Techniques.FadeIn).duration(400).repeat(0).playOn(welcometxv);
                    //toasts to show messages onclick
                    Toast.makeText(MainActivity.this, "Logging in .....", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getApplicationContext(), "login", Toast.LENGTH_LONG).show();
                    //create and use intent
                    Intent login_intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(login_intent);
                    }

            }
        });

    }
}
