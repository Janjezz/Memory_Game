package aleksa.janjic.memorygame;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;


import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Element> mResults;
    private final String DB_NAME = "games";
    public PlayerDBHelper dbHelper;
    public HTTPHelper httpHelper;
    public String username;
    public static String BASE_URL = "http://192.168.85.1:3000/";
    public static String POST_ONE = "score/?username=";

    public MyAdapter(Context mContext, String username){
        this.mContext = mContext;
        mResults = new ArrayList<Element>();
        dbHelper = new PlayerDBHelper(mContext, DB_NAME, null, 1);
        this.username = username;
        httpHelper = new HTTPHelper();
    }

    @Override
    public int getCount() {
        return mResults.size();
    }

    @Override
    public Object getItem(int i) {
        Object obj = null;
        try {
            obj = mResults.get(i);
        }catch (Exception e){
            e.printStackTrace();
        }
        return obj;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void addElement(Element e){
        mResults.add(e);
        notifyDataSetChanged();
    }
    public void removeElementByIndex(Element i){
        if(mResults.contains(i)){
            mResults.remove(i);
            notifyDataSetChanged();
        }
    }

    public void deleteAll(){
        mResults.clear();
        notifyDataSetChanged();
    }

    public ArrayList<String> getBestResults(String ime){
        ArrayList<String> rez = new ArrayList<>();
        SQLiteDatabase database;

        return rez;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null){
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_row, null);
            viewHolder = new ViewHolder();
            viewHolder.tIme = view.findViewById(R.id.imeIgraca);
            viewHolder.tEmail = view.findViewById(R.id.email);
            viewHolder.tRezB = view.findViewById(R.id.rezultatB);
            viewHolder.tRezW = view.findViewById(R.id.rezultatW);
            viewHolder.tDugme = view.findViewById(R.id.dugmeObrisi);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        Element el = (Element) getItem(i);

        viewHolder.tIme.setText(el.getmIme());
        viewHolder.tEmail.setText(el.getmEmail());
        viewHolder.tRezB.setText(el.getmRezultatB());
        viewHolder.tRezW.setText(el.getmRezultatW());

        if(viewHolder.tIme.getText().equals(username)){
            viewHolder.tDugme.setEnabled(true);
        }else {
            viewHolder.tDugme.setEnabled(false);
        }

        viewHolder.tDugme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeElementByIndex(el);
                dbHelper.delete(mResults.get(i).getmIme());
                getDelete();
                notifyDataSetChanged();
            }
        });

        return view;
    }
    static class ViewHolder {
        TextView tIme;
        TextView tEmail;
        TextView tRezB;
        TextView tRezW;
        Button tDugme;
    }
    public void getDelete(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int ret = httpHelper.httpDelete(BASE_URL + POST_ONE + username);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
