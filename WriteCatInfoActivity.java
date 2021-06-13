package com.example.find_cat_info;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONException;

public class WriteCatInfoActivity extends AppCompatActivity {


    private ImageView catPreview;
    private TextView nameView;

    private RadioGroup genderRadio;
    private RadioGroup neuterRadio;

    private Button saveButton;

    private String imageLink;

    private Bitmap catImage;

    private boolean isMan;
    private boolean neuter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_cat_info);

        catPreview = findViewById(R.id.cat_preview);
        nameView = findViewById(R.id.cat_name);
        genderRadio = findViewById(R.id.gender_check);
        neuterRadio = findViewById(R.id.neuter_check);
        saveButton = findViewById(R.id.save);

        catPreview.setOnClickListener(view -> {
            ImageUtil.selectImage(this);
        });

        genderRadio.setOnCheckedChangeListener((radioGroup, i) -> {
            isMan = i == R.id.man;
        });

        neuterRadio.setOnCheckedChangeListener((radioGroup, i) -> {
            neuter = i == R.id.yes;
        });

        saveButton.setOnClickListener(view -> {

            if (catImage == null) {
                return;
            }

            String imageName = ImageUtil.saveBitmap(this, catImage);
            String name = nameView.getText().toString();

            if (name.isEmpty() || genderRadio.getCheckedRadioButtonId() == -1 || neuterRadio.getCheckedRadioButtonId() == -1)
                return;

            CatData catData = new CatData(
                    imageName,
                    name,
                    neuter,
                    isMan
            );

            try {
                SharedModel.get(this).addInfo(catData);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            finish();
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            assert data != null;
            findViewById(R.id.preview_text).setVisibility(View.GONE);
            catImage = ImageUtil.getContentUriImage(this, data.getData());
            catPreview.setImageBitmap(catImage);
        }

    }
}