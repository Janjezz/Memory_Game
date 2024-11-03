package aleksa.janjic.memorygame;

import static aleksa.janjic.memorygame.R.color.purple_500;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.lang.invoke.VolatileCallSite;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    Button b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14, b15, b16;
    Button startB, statisticsB;
    Button currentButton1, currentButton2;
    ImageView s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, s12, s13, s14, s15, s16;
    ImageView currentImage1, currentImage2;
    public int counter = 0, score = 0, image1 = 0, image2 = 0, id = 0, max_pairs = 0;
    String username;
    ArrayList<Element> elements = new ArrayList<>();
    Integer[] drawables = {R.drawable.andresized2, R.drawable.ededdneddy, R.drawable.scooby,
            R.drawable.samurai ,R.drawable.scooby , R.drawable.johnny,
            R.drawable.jakesalad, R.drawable.jakesalad, R.drawable.dexter, R.drawable.ededdneddy,
            R.drawable.ben10, R.drawable.johnny, R.drawable.ben10, R.drawable.dexter,
            R.drawable.samurai, R.drawable.andresized2};
    public HTTPHelper httpHelper;
    public static String BASE_URL = "http://192.168.85.1:3000/";
    public static String POST_ONE = "score";

    public void cmprImages(){

        if(image1 == image2){
            score += 5;
            max_pairs += 1;
        }else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    currentButton1.setVisibility(View.VISIBLE);
                    currentButton2.setVisibility(View.VISIBLE);

                    currentImage1.setVisibility(View.INVISIBLE);
                    currentImage2.setVisibility(View.INVISIBLE);
                    score--;
                }
            }, 500);
        }
    }
    public int getImageId(ImageView view){
        return (int) view.getTag();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Bundle bundle = getIntent().getExtras();
        username = bundle.getString("username");

        b1 = findViewById(R.id.dugme1);
        b2 = findViewById(R.id.dugme2);
        b3 = findViewById(R.id.dugme3);
        b4 = findViewById(R.id.dugme4);
        b5 = findViewById(R.id.dugme5);
        b6 = findViewById(R.id.dugme6);
        b7 = findViewById(R.id.dugme7);
        b8 = findViewById(R.id.dugme8);
        b9 = findViewById(R.id.dugme9);
        b10 = findViewById(R.id.dugme10);
        b11 = findViewById(R.id.dugme11);
        b12 = findViewById(R.id.dugme12);
        b13 = findViewById(R.id.dugme13);
        b14 = findViewById(R.id.dugme14);
        b15 = findViewById(R.id.dugme15);
        b16 = findViewById(R.id.dugme16);
        s1 = findViewById(R.id.slika1);
        s2 = findViewById(R.id.slika2);
        s3 = findViewById(R.id.slika3);
        s4 = findViewById(R.id.slika4);
        s5 = findViewById(R.id.slika5);
        s6 = findViewById(R.id.slika6);
        s7 = findViewById(R.id.slika7);
        s8 = findViewById(R.id.slika8);
        s9 = findViewById(R.id.slika9);
        s10 = findViewById(R.id.slika10);
        s11 = findViewById(R.id.slika11);
        s12 = findViewById(R.id.slika12);
        s13 = findViewById(R.id.slika13);
        s14 = findViewById(R.id.slika14);
        s15 = findViewById(R.id.slika15);
        s16 = findViewById(R.id.slika16);

//        s1.setTag(R.drawable.andresized2);
//        s2.setTag(R.drawable.ededdneddy);
//        s3.setTag(R.drawable.scooby);
//        s4.setTag(R.drawable.samurai);
//        s5.setTag(R.drawable.scooby);
//        s6.setTag(R.drawable.johnny);
//        s7.setTag(R.drawable.jakesalad);
//        s8.setTag(R.drawable.jakesalad);
//        s9.setTag(R.drawable.dexter);
//        s10.setTag(R.drawable.ededdneddy);
//        s11.setTag(R.drawable.ben10);
//        s12.setTag(R.drawable.johnny);
//        s13.setTag(R.drawable.ben10);
//        s14.setTag(R.drawable.dexter);
//        s15.setTag(R.drawable.samurai);
//        s16.setTag(R.drawable.andresized2);

        statisticsB = findViewById(R.id.dugmeStatistics);
        startB = findViewById(R.id.dugmeStart);

        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        b4.setOnClickListener(this);
        b5.setOnClickListener(this);
        b6.setOnClickListener(this);
        b7.setOnClickListener(this);
        b8.setOnClickListener(this);
        b9.setOnClickListener(this);
        b10.setOnClickListener(this);
        b11.setOnClickListener(this);
        b12.setOnClickListener(this);
        b13.setOnClickListener(this);
        b14.setOnClickListener(this);
        b15.setOnClickListener(this);
        b16.setOnClickListener(this);

        startB.setOnClickListener(this);
        statisticsB.setOnClickListener(this);

        b1.setEnabled(false);
        b2.setEnabled(false);
        b3.setEnabled(false);
        b4.setEnabled(false);
        b5.setEnabled(false);
        b6.setEnabled(false);
        b7.setEnabled(false);
        b8.setEnabled(false);
        b9.setEnabled(false);
        b10.setEnabled(false);
        b11.setEnabled(false);
        b12.setEnabled(false);
        b13.setEnabled(false);
        b14.setEnabled(false);
        b15.setEnabled(false);
        b16.setEnabled(false);

        s1.setVisibility(View.INVISIBLE);
        s2.setVisibility(View.INVISIBLE);
        s3.setVisibility(View.INVISIBLE);
        s4.setVisibility(View.INVISIBLE);
        s5.setVisibility(View.INVISIBLE);
        s6.setVisibility(View.INVISIBLE);
        s7.setVisibility(View.INVISIBLE);
        s8.setVisibility(View.INVISIBLE);
        s9.setVisibility(View.INVISIBLE);
        s10.setVisibility(View.INVISIBLE);
        s11.setVisibility(View.INVISIBLE);
        s12.setVisibility(View.INVISIBLE);
        s13.setVisibility(View.INVISIBLE);
        s14.setVisibility(View.INVISIBLE);
        s15.setVisibility(View.INVISIBLE);
        s16.setVisibility(View.INVISIBLE);

    }

    public void postScore(String name, int score){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", name);
            jsonObject.put("score", score);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int ret = httpHelper.postJSONObjectFromURL(BASE_URL + POST_ONE, jsonObject);
                    if(ret == 201){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(GameActivity.this, "201, Created", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else if(ret == 400){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(GameActivity.this, "400, Bad Request", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        if (counter == 2) {

            cmprImages();

            image1 = 0;
            image2 = 0;
            counter = 0;
        }
        else if(view.getId() == R.id.dugmeStart){
            List<Integer> drawables_list = Arrays.asList(drawables);
            Collections.shuffle(drawables_list);

            s1.setImageResource(drawables_list.get(0));
            s2.setImageResource(drawables_list.get(1));
            s3.setImageResource(drawables_list.get(2));
            s4.setImageResource(drawables_list.get(3));
            s5.setImageResource(drawables_list.get(4));
            s6.setImageResource(drawables_list.get(5));
            s7.setImageResource(drawables_list.get(6));
            s8.setImageResource(drawables_list.get(7));
            s9.setImageResource(drawables_list.get(8));
            s10.setImageResource(drawables_list.get(9));
            s11.setImageResource(drawables_list.get(10));
            s12.setImageResource(drawables_list.get(11));
            s13.setImageResource(drawables_list.get(12));
            s14.setImageResource(drawables_list.get(13));
            s15.setImageResource(drawables_list.get(14));
            s16.setImageResource(drawables_list.get(15));

            s1.setTag(drawables_list.get(0));
            s2.setTag(drawables_list.get(1));
            s3.setTag(drawables_list.get(2));
            s4.setTag(drawables_list.get(3));
            s5.setTag(drawables_list.get(4));
            s6.setTag(drawables_list.get(5));
            s7.setTag(drawables_list.get(6));
            s8.setTag(drawables_list.get(7));
            s9.setTag(drawables_list.get(8));
            s10.setTag(drawables_list.get(9));
            s11.setTag(drawables_list.get(10));
            s12.setTag(drawables_list.get(11));
            s13.setTag(drawables_list.get(12));
            s14.setTag(drawables_list.get(13));
            s15.setTag(drawables_list.get(14));
            s16.setTag(drawables_list.get(15));

            if(startB.getText().toString().equals("Start")) {
                startB.setBackgroundColor(getColor(purple_500));
                String text = new String("Restart");
                startB.setText(text.toString());

                counter = 0;
                id = 0;

                b1.setEnabled(true);
                b2.setEnabled(true);
                b3.setEnabled(true);
                b4.setEnabled(true);
                b5.setEnabled(true);
                b6.setEnabled(true);
                b7.setEnabled(true);
                b8.setEnabled(true);
                b9.setEnabled(true);
                b10.setEnabled(true);
                b11.setEnabled(true);
                b12.setEnabled(true);
                b13.setEnabled(true);
                b14.setEnabled(true);
                b15.setEnabled(true);
                b16.setEnabled(true);

            } else if(startB.getText().toString().equals("Restart")) {
                id++;
                score = 0;
                if(max_pairs < 8) {
                    String email = username + "@email.com";
                    Element element = new Element(username, email, String.valueOf(score), String.valueOf(id));
                    elements.add(element);
                    postScore(username, score);
                }

                s1.setVisibility(View.INVISIBLE);
                s2.setVisibility(View.INVISIBLE);
                s3.setVisibility(View.INVISIBLE);
                s4.setVisibility(View.INVISIBLE);
                s5.setVisibility(View.INVISIBLE);
                s6.setVisibility(View.INVISIBLE);
                s7.setVisibility(View.INVISIBLE);
                s8.setVisibility(View.INVISIBLE);
                s9.setVisibility(View.INVISIBLE);
                s10.setVisibility(View.INVISIBLE);
                s11.setVisibility(View.INVISIBLE);
                s12.setVisibility(View.INVISIBLE);
                s13.setVisibility(View.INVISIBLE);
                s14.setVisibility(View.INVISIBLE);
                s15.setVisibility(View.INVISIBLE);
                s16.setVisibility(View.INVISIBLE);

                b1.setVisibility(View.VISIBLE);
                b2.setVisibility(View.VISIBLE);
                b3.setVisibility(View.VISIBLE);
                b4.setVisibility(View.VISIBLE);
                b5.setVisibility(View.VISIBLE);
                b6.setVisibility(View.VISIBLE);
                b7.setVisibility(View.VISIBLE);
                b8.setVisibility(View.VISIBLE);
                b9.setVisibility(View.VISIBLE);
                b10.setVisibility(View.VISIBLE);
                b11.setVisibility(View.VISIBLE);
                b12.setVisibility(View.VISIBLE);
                b13.setVisibility(View.VISIBLE);
                b14.setVisibility(View.VISIBLE);
                b15.setVisibility(View.VISIBLE);
                b16.setVisibility(View.VISIBLE);
            }
        }else if(view.getId() == R.id.dugme1){

                b1.setVisibility(View.INVISIBLE);
                s1.setVisibility(View.VISIBLE);
                counter++;
                if(image1 == 0){
                    image1 = getImageId(s1);
                    currentButton1 = findViewById(R.id.dugme1);
                    currentImage1 = findViewById(R.id.slika1);
                }else {
                    image2 = getImageId(s1);
                    currentButton2 = findViewById(R.id.dugme1);
                    currentImage2 = findViewById(R.id.slika1);
                }

        }else if(view.getId() == R.id.dugme2){
                b2.setVisibility(View.INVISIBLE);
                s2.setVisibility(View.VISIBLE);
                counter++;
                if(image1 == 0){
                    image1 = getImageId(s2);
                    currentButton1 = findViewById(R.id.dugme2);
                    currentImage1 = findViewById(R.id.slika2);
                }else {
                    image2 = getImageId(s2);
                    currentButton2 = findViewById(R.id.dugme2);
                    currentImage2 = findViewById(R.id.slika2);
                }
        }else if(view.getId() == R.id.dugme3){
                b3.setVisibility(View.INVISIBLE);
                s3.setVisibility(View.VISIBLE);
                counter++;
                if(image1 == 0){
                    image1 = getImageId(s3);
                    currentButton1 = findViewById(R.id.dugme3);
                    currentImage1 = findViewById(R.id.slika3);
                }else {
                    image2 = getImageId(s3);
                    currentButton2 = findViewById(R.id.dugme3);
                    currentImage2 = findViewById(R.id.slika3);
                }
        }else if(view.getId() == R.id.dugme4){
                b4.setVisibility(View.INVISIBLE);
                s4.setVisibility(View.VISIBLE);
                counter++;
                if(image1 == 0){
                    image1 = getImageId(s4);
                    currentButton1 = findViewById(R.id.dugme4);
                    currentImage1 = findViewById(R.id.slika4);
                }else {
                    image2 = getImageId(s4);
                    currentButton2 = findViewById(R.id.dugme4);
                    currentImage2 = findViewById(R.id.slika4);
                }
        }else if(view.getId() == R.id.dugme5){
                b5.setVisibility(View.INVISIBLE);
                s5.setVisibility(View.VISIBLE);
                counter++;
                if(image1 == 0){
                    image1 = getImageId(s5);
                    currentButton1 = findViewById(R.id.dugme5);
                    currentImage1 = findViewById(R.id.slika5);
                }else {
                    image2 = getImageId(s5);
                    currentButton2 = findViewById(R.id.dugme5);
                    currentImage2 = findViewById(R.id.slika5);
                }
        }else if(view.getId() == R.id.dugme6){
                b6.setVisibility(View.INVISIBLE);
                s6.setVisibility(View.VISIBLE);
                counter++;
                if(image1 == 0){
                    image1 = getImageId(s6);
                    currentButton1 = findViewById(R.id.dugme6);
                    currentImage1 = findViewById(R.id.slika6);
                }else {
                    image2 = getImageId(s6);
                    currentButton2 = findViewById(R.id.dugme6);
                    currentImage2 = findViewById(R.id.slika6);
                }
        }else if(view.getId() == R.id.dugme7){
                b7.setVisibility(View.INVISIBLE);
                s7.setVisibility(View.VISIBLE);
                counter++;
                if(image1 == 0){
                    image1 = getImageId(s7);
                    currentButton1 = findViewById(R.id.dugme7);
                    currentImage1 = findViewById(R.id.slika7);
                }else {
                    image2 = getImageId(s7);
                    currentButton2 = findViewById(R.id.dugme7);
                    currentImage2 = findViewById(R.id.slika7);
                }
        }else if(view.getId() == R.id.dugme8){
                b8.setVisibility(View.INVISIBLE);
                s8.setVisibility(View.VISIBLE);
                counter++;
                if(image1 == 0){
                    image1 = getImageId(s8);
                    currentButton1 = findViewById(R.id.dugme8);
                    currentImage1 = findViewById(R.id.slika8);
                }else {
                    image2 = getImageId(s8);
                    currentButton2 = findViewById(R.id.dugme8);
                    currentImage2 = findViewById(R.id.slika8);
                }
        }else if(view.getId() == R.id.dugme9){
                b9.setVisibility(View.INVISIBLE);
                s9.setVisibility(View.VISIBLE);
                counter++;
                if(image1 == 0){
                    image1 = getImageId(s9);
                    currentButton1 = findViewById(R.id.dugme9);
                    currentImage1 = findViewById(R.id.slika9);
                }else {
                    image2 = getImageId(s9);
                    currentButton2 = findViewById(R.id.dugme9);
                    currentImage2 = findViewById(R.id.slika9);
                }
        }else if(view.getId() == R.id.dugme10){
                b10.setVisibility(View.INVISIBLE);
                s10.setVisibility(View.VISIBLE);
                counter++;
                if(image1 == 0){
                    image1 = getImageId(s10);
                    currentButton1 = findViewById(R.id.dugme10);
                    currentImage1 = findViewById(R.id.slika10);
                }else {
                    image2 = getImageId(s10);
                    currentButton2 = findViewById(R.id.dugme10);
                    currentImage2 = findViewById(R.id.slika10);
                }
        }else if(view.getId() == R.id.dugme11){
                b11.setVisibility(View.INVISIBLE);
                s11.setVisibility(View.VISIBLE);
                counter++;
                if(image1 == 0){
                    image1 = getImageId(s11);
                    currentButton1 = findViewById(R.id.dugme11);
                    currentImage1 = findViewById(R.id.slika11);
                }else {
                    image2 = getImageId(s11);
                    currentButton2 = findViewById(R.id.dugme11);
                    currentImage2 = findViewById(R.id.slika11);
                }
        }else if(view.getId() == R.id.dugme12){
                b12.setVisibility(View.INVISIBLE);
                s12.setVisibility(View.VISIBLE);
                counter++;
                if(image1 == 0){
                    image1 = getImageId(s12);
                    currentButton1 = findViewById(R.id.dugme12);
                    currentImage1 = findViewById(R.id.slika12);
                }else {
                    image2 = getImageId(s12);
                    currentButton2 = findViewById(R.id.dugme12);
                    currentImage2 = findViewById(R.id.slika12);
                }
        }else if(view.getId() == R.id.dugme13){
                b13.setVisibility(View.INVISIBLE);
                s13.setVisibility(View.VISIBLE);
                counter++;
                if(image1 == 0){
                    image1 = getImageId(s13);
                    currentButton1 = findViewById(R.id.dugme13);
                    currentImage1 = findViewById(R.id.slika13);
                }else {
                    image2 = getImageId(s13);
                    currentButton2 = findViewById(R.id.dugme13);
                    currentImage2 = findViewById(R.id.slika13);
                }
        }else if(view.getId() == R.id.dugme14){
                b14.setVisibility(View.INVISIBLE);
                s14.setVisibility(View.VISIBLE);
                counter++;
                if(image1 == 0){
                    image1 = getImageId(s14);
                    currentButton1 = findViewById(R.id.dugme14);
                    currentImage1 = findViewById(R.id.slika14);
                }else {
                    image2 = getImageId(s14);
                    currentButton2 = findViewById(R.id.dugme14);
                    currentImage2 = findViewById(R.id.slika14);
                }
        }else if(view.getId() == R.id.dugme15){
                b15.setVisibility(View.INVISIBLE);
                s15.setVisibility(View.VISIBLE);
                counter++;
                if(image1 == 0){
                    image1 = getImageId(s15);
                    currentButton1 = findViewById(R.id.dugme15);
                    currentImage1 = findViewById(R.id.slika15);
                }else {
                    image2 = getImageId(s15);
                    currentButton2 = findViewById(R.id.dugme15);
                    currentImage2 = findViewById(R.id.slika15);
                }
        }else if(view.getId() == R.id.dugme16){
                b16.setVisibility(View.INVISIBLE);
                s16.setVisibility(View.VISIBLE);
                counter++;
                if(image1 == 0){
                    image1 = getImageId(s16);
                    currentButton1 = findViewById(R.id.dugme16);
                    currentImage1 = findViewById(R.id.slika16);
                }else {
                    image2 = getImageId(s16);
                    currentButton2 = findViewById(R.id.dugme16);
                    currentImage2 = findViewById(R.id.slika16);
                }
        }
        if(max_pairs == 8){
            Element element = new Element(username, username + "@yahoo.com", String.valueOf(score), String.valueOf(id));
            postScore(username, score);
            elements.add(element);
        }
        if(view.getId() == R.id.dugmeStatistics) {
            if(max_pairs != 8){
                Element element = new Element(username, username + "@yahoo.com", String.valueOf(score), String.valueOf(id));
                elements.add(element);
                postScore(username, score);
            }
            Intent toStatistics = new Intent(getApplicationContext(), StatisticsActivity.class);
           // if(elements.size() != 0) {
                toStatistics.putExtra("played", (Serializable) elements);
                startActivity(toStatistics);
           // }
//            boolean check = elements.isEmpty();
//            if (!check)
//
//            else
//                Toast.makeText(getApplicationContext(), "No games played yet!!!", Toast.LENGTH_SHORT).show();

//            s1.setVisibility(View.INVISIBLE);
//            s2.setVisibility(View.INVISIBLE);
//            s3.setVisibility(View.INVISIBLE);
//            s4.setVisibility(View.INVISIBLE);
//            s5.setVisibility(View.INVISIBLE);
//            s6.setVisibility(View.INVISIBLE);
//            s7.setVisibility(View.INVISIBLE);
//            s8.setVisibility(View.INVISIBLE);
//            s9.setVisibility(View.INVISIBLE);
//            s10.setVisibility(View.INVISIBLE);
//            s11.setVisibility(View.INVISIBLE);
//            s12.setVisibility(View.INVISIBLE);
//            s13.setVisibility(View.INVISIBLE);
//            s14.setVisibility(View.INVISIBLE);
//            s15.setVisibility(View.INVISIBLE);
//            s16.setVisibility(View.INVISIBLE);
//
//            b1.setVisibility(View.VISIBLE);
//            b2.setVisibility(View.VISIBLE);
//            b3.setVisibility(View.VISIBLE);
//            b4.setVisibility(View.VISIBLE);
//            b5.setVisibility(View.VISIBLE);
//            b6.setVisibility(View.VISIBLE);
//            b7.setVisibility(View.VISIBLE);
//            b8.setVisibility(View.VISIBLE);
//            b9.setVisibility(View.VISIBLE);
//            b10.setVisibility(View.VISIBLE);
//            b11.setVisibility(View.VISIBLE);
//            b12.setVisibility(View.VISIBLE);
//            b13.setVisibility(View.VISIBLE);
//            b14.setVisibility(View.VISIBLE);
//            b15.setVisibility(View.VISIBLE);
//            b16.setVisibility(View.VISIBLE);
        }
    }
}