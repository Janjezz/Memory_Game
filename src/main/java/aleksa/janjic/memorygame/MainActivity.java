package aleksa.janjic.memorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    TextView tw1, tw2;
    EditText et1, et2;
    Button bt1, bt2;
    private HTTPHelper httpHelper;
    public static String BASE_URL = "http://192.168.85.1:3000";
    public static String LOGIN = BASE_URL + "/auth/signin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tw1 = findViewById(R.id.text1);
        tw2 = findViewById(R.id.text2);
        et1 = findViewById(R.id.editText1);
        et2 = findViewById(R.id.editText2);
        bt1 = findViewById(R.id.dugmeLogin);
        bt2 = findViewById(R.id.registruj);

        httpHelper = new HTTPHelper();


        bt1.setOnClickListener(new View.OnClickListener() {
            TextUtils textUtils;
            @Override
            public void onClick(View view) {
                    Intent goToGame = new Intent(MainActivity.this, GameActivity.class);
                    String username = et1.getText().toString();
                    goToGame.putExtra("username", username);

                JSONObject jsonObject = new JSONObject();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            jsonObject.put("username", et1.getText().toString());
                            jsonObject.put("password", et2.getText().toString());
                            try {
                                int retcode = httpHelper.postJSONObjectFromURL(LOGIN, jsonObject);

                                if(retcode == 201){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent gameActivityIntent = new Intent(MainActivity.this, GameActivity.class);
                                            Intent statisticsActivityIntent = new Intent(MainActivity.this, StatisticsActivity.class);
                                            gameActivityIntent.putExtra("username", username);
                                            statisticsActivityIntent.putExtra("username", username);
                                            startActivity(gameActivityIntent);
                                        }
                                    });
                                }
                                else if(retcode == 500){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(MainActivity.this, "Bad connection!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                                else{
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(MainActivity.this, "Something wrong with your credentials!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }catch (JSONException | IOException e){
                                e.printStackTrace();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToGame = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(goToGame);
            }
        });
    }
}