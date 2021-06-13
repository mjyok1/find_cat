package com.example.find_cat_info;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CatListAdapter.OnClickCatInfo {

    private RecyclerView catListView;
    private CatListAdapter catListAdapter;

    private ArrayList<CatData> catDataArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        catListView = findViewById(R.id.cat_list);

        catListAdapter = new CatListAdapter(this, this, catDataArrayList);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);

        catListView.setAdapter(catListAdapter);
        catListView.setLayoutManager(gridLayoutManager);

        FloatingActionButton floatingActionButton = findViewById(R.id.add);
        floatingActionButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), WriteCatInfoActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("TAG", "onStart: ");
        try {
            catDataArrayList.clear();
            catDataArrayList.addAll(SharedModel.get(this).getInfo());
            catListAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(int position, CatData catData) {
        Intent intent = new Intent(getApplicationContext(), CatInfoActivity.class);
        intent.putExtra(CatInfoActivity.CAT_DATA_KEY, position);
        startActivity(intent);
    }
}