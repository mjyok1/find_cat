package com.example.find_cat_info;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;

public class CatInfoActivity extends AppCompatActivity {

    public static final String CAT_DATA_KEY = "CATDATA";

    private int catIndex;
    private CatData catData;

    private ImageView catPreview;
    private TextView nameView;
    private TextView descriptionView;

    private RadioGroup genderRadio;
    private RadioGroup neuterRadio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_info);

        try {
            catIndex = getIntent().getIntExtra(CAT_DATA_KEY, -1);
            catData = SharedModel.get(this).getInfo().get(catIndex);
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }

        catPreview = findViewById(R.id.cat_preview);
        nameView = findViewById(R.id.cat_name);
        descriptionView = findViewById(R.id.cat_description);
        genderRadio = findViewById(R.id.gender_check);
        neuterRadio = findViewById(R.id.neuter_check);

        catPreview.setImageBitmap(ImageUtil.getDataImage(this, catData.image));
        nameView.setText(catData.name);
        descriptionView.setText(catData.description);

        if (catData.isMan) {
            genderRadio.check(R.id.man);
        } else {
            genderRadio.check(R.id.woman);
        }

        if (catData.neuter) {
            neuterRadio.check(R.id.yes);
        } else {
            neuterRadio.check(R.id.no);
        }

        genderRadio.setOnCheckedChangeListener((radioGroup, i) -> {
            catData.isMan = i == R.id.man;
        });

        neuterRadio.setOnCheckedChangeListener((radioGroup, i) -> {
            catData.neuter = i == R.id.yes;
        });

    }


    @Override
    protected void onPause() {
        super.onPause();

        ArrayList<CatData> infoList = null;
        try {
            infoList = SharedModel.get(this).getInfo();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        assert infoList != null;
        infoList.remove(catIndex);

        catData.name = nameView.getText().toString();
        catData.description = descriptionView.getText().toString();
        infoList.add(0, catData);

        SharedModel.get(this).updateInfoList(infoList);
        catIndex = 0;
    }
}