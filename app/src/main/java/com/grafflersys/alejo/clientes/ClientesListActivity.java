package com.grafflersys.alejo.clientes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ClientesListActivity extends AppCompatActivity {

    ArrayList<Model> nList;
    ClientesListAdapter nAdapter = null;
    ListView mListView;
    ImageView imageViewIcon;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes_list);

        mListView = findViewById(R.id.listView);

    }
}
