package aleksa.janjic.memorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity {

    TextView username;
    ListView list;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        username = findViewById(R.id.u_name);
        list = findViewById(R.id.bestRez);

        Bundle bundle = getIntent().getExtras();
        ArrayList<String> rez = new ArrayList<>();
        rez = bundle.getStringArrayList("listaRez");
        String ime = bundle.getString("username");

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        username.setText(ime);
        list.setAdapter(adapter);
        adapter.addAll(rez);
    }
}