package aleksa.janjic.memorygame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationChannelCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;


public class StatisticsActivity extends AppCompatActivity {

    ListView mLista;
    Button obrisi, osvezi;
    MyAdapter adapter;
    String username;
    HTTPHelper httpHelper;
    public static String BASE_URL = "http://192.168.85.1:3000/";
    public static String POST_ONE = "score";
    public static String CLIENT = "/?username=";
    private static int id = 0;
    private ArrayList<Element> poruka;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        Intent intent = new Intent(this, MyService.class);
        poruka = new ArrayList<>();
        startService(intent);
        LocalBroadcastManager.getInstance(StatisticsActivity.this).registerReceiver(
                mMessageReceiver, new IntentFilter("Server data"));

        Bundle bundle = getIntent().getExtras();
        ArrayList<Element> odigrane = (ArrayList<Element>) bundle.get("played");

        mLista = findViewById(R.id.lista);
        username = odigrane.get(0).getmIme();
        obrisi = findViewById(R.id.dugmeObrisi);
        osvezi = findViewById(R.id.refresh);

        httpHelper = new HTTPHelper();
        adapter = new MyAdapter(this, username.toString());
        mLista.setAdapter(adapter);

//        adapter.dbHelper.insert(new Element("Jontra","jontra@aa","10" ,"2"));
//        adapter.dbHelper.insert(new Element("Ziko","ziko@z","13" ,"3"));
//        adapter.dbHelper.insert(new Element("Mina","mina@aa","20" ,"10"));
//        adapter.dbHelper.insert(new Element("Ognjen", "gio@aa","25" ,"3"));
//        adapter.dbHelper.insert(new Element("Stanislav", "stan@aa","23" ,"4"));
//        adapter.dbHelper.insert(new Element("Trezege", "trez@aa","17" ,"2"));
//        adapter.dbHelper.insert(new Element("Marija", "mara@aa","19" ,"6"));
//        adapter.dbHelper.insert(new Element("Evangelina", "lina@aa","8" ,"0"));
//        adapter.dbHelper.insert(new Element("Janko","jnk@aa","15" ,"8"));
//        adapter.dbHelper.insert(new Element("Predrage", "djape@aa", "30","12"));

        for(int i = 0; i < odigrane.size(); i++){
            adapter.dbHelper.insert(odigrane.get(i));
        }

        Element[] elements = adapter.dbHelper.readPlayers();
        for (int i = 0; i < elements.length; i++){
            adapter.addElement(elements[i]);
        }

        osvezi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.dbHelper.deleteAll();
                adapter.deleteAll();
                try {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONArray jsonArray = httpHelper.getJSONArrayFromURL(BASE_URL + POST_ONE);
                                for(int i = 0; i < jsonArray.length(); i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String username = jsonObject.getString("username");
                                    int score = jsonObject.getInt("score");
                                    String email = username + "@email.com";
                                    Element element = new Element(username, email, String.valueOf(score), String.valueOf(id));
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            adapter.dbHelper.insert(element);
                                        }
                                    });
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Element[] elements = adapter.dbHelper.readPlayers();
                                        for(int i = 0; i < elements.length; i++){
                                            adapter.addElement(elements[i]);
                                        }
                                    }
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mLista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent toDetails = new Intent(getApplicationContext(), DetailsActivity.class);
                Element e = (Element) adapter.getItem(i);
                toDetails.putExtra("username", e.getmIme());
//                int[] rez;
//                rez = adapter.dbHelper.getRezults(e.getmIme().toString());
//                ArrayList<String> arrayList = new ArrayList<>();
//                for(int k = 0; k < rez.length; k++){
//                    arrayList.add(String.valueOf(rez[k]));
//                }
//                toDetails.putExtra("listaRez",(Serializable) arrayList);
//                startActivity(toDetails);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ArrayList<String> arrayList = new ArrayList<>();
                            JSONArray jsonArray = httpHelper.getJSONArrayFromURL(BASE_URL+POST_ONE+CLIENT+e.getmIme());
                            for(int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                int score = jsonObject.getInt("score");
                                arrayList.add(String.valueOf(score));
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    toDetails.putExtra("listaRez",(Serializable) arrayList);
                                    startActivity(toDetails);
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            Bundle b = intent.getExtras();
            ArrayList<Element> message = (ArrayList<Element>) b.get("listaRez");
            if(message != null && message != poruka){
                poruka = message;
                adapter.dbHelper.deleteAll();
                adapter.deleteAll();
                for(int i = 0; i < message.size(); i++){
                    adapter.dbHelper.insert(message.get(i));
                }
                Element[] elements = adapter.dbHelper.readPlayers();
                for(int i = 0; i < elements.length; i++){
                    adapter.addElement(elements[i]);
                }
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NotificationChannelCompat.DEFAULT_CHANNEL_ID);
                builder.setSmallIcon(R.drawable.dexter);
                builder.setContentTitle(getString(R.string.obavestenje));
                builder.setContentText("Lista je osvezena.");
                builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

                NotificationManagerCompat managerCompat = NotificationManagerCompat.from(StatisticsActivity.this);
                managerCompat.notify(1, builder.build());

            }
            // Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    };
}