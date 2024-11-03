package aleksa.janjic.memorygame;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


public class MyService extends Service {
    private boolean mRun = true;
    private HTTPHelper httpHelper;
    public static String BASE_URL = "http://192.168.85.1:3000/";
    public static String POST_ONE = "score";
    private int id = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        httpHelper = new HTTPHelper();
        ArrayList<Element> statArrayList = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mRun) {
                    Log.d("ServiceTAG", "Hello from service thread");
                    try {
                        JSONArray jsonArray = httpHelper.getJSONArrayFromURL(BASE_URL + POST_ONE);

                        for(int i = 0; i < jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String name = jsonObject.getString("username");
                            int score = jsonObject.getInt("score");
                            String email = name + "@email.com";
                            Element element = new Element(name, email, String.valueOf(score), String.valueOf(id));
                            id++;
                            statArrayList.add(element);
                        }
                        sendMessageToActivity(statArrayList);

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


    }

    private void sendMessageToActivity(ArrayList<Element> arrayList) {
        Intent intent = new Intent("Server data");
        // You can also include some extra data.
        intent.putExtra("listaRez", arrayList);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    /* kad god se pozove startservice (klik na startservice dugme) poziva se ova metoda
    i startId se poveca za 1
    stopService unistava servis
    moze npr da se pozove stopSelf sa odredjenim id kako bi se unistio odredjeni request
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service is starting", Toast.LENGTH_SHORT).show();


        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mRun = false;
        Toast.makeText(this, "Service is done", Toast.LENGTH_SHORT).show();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not implemented yet !!!");
    }
}
