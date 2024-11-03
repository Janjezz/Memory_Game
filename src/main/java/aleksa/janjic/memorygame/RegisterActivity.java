package aleksa.janjic.memorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private Button register;
    private EditText username, password, email;
    public static String BASE_URL = "http://192.168.85.1:3000";
    public static String REGISTER = BASE_URL + "/auth/signup";

    private HTTPHelper httpHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.editT1);
        password = findViewById(R.id.editT2);
        email = findViewById(R.id.editEmail2);
        register = findViewById(R.id.dugmeRegister);

        httpHelper = new HTTPHelper();
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.dugmeRegister){
            JSONObject jsonObject = new JSONObject();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        jsonObject.put("username", username.getText().toString());
                        jsonObject.put("password", password.getText().toString());
                        jsonObject.put("email", email.getText().toString());

                        try {
                            int retcode = httpHelper.postJSONObjectFromURL(REGISTER, jsonObject);

                            if (retcode == 201) {
                                Intent loginActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(RegisterActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                startActivity(loginActivityIntent);
                            } else if (retcode == 500) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(RegisterActivity.this, "Bad connection!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else{
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(RegisterActivity.this, "Something wrong with your credentials or user already exists!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}