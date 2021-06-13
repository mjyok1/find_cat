package com.example.find_cat_info;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;

public class SharedModel {

    private final static String TAG = SharedModel.class.getName();
    private static SharedModel instance = null;

    private static final Gson gson = new Gson();

    public static SharedModel get(Context context) {

        if (instance == null) {
            Log.e(TAG, "get: instance not init");
            instance = new SharedModel(context);
        }

        return instance;
    }

    @SuppressLint("CommitPrefEdits")
    private SharedModel(Context context) {
        shared = context.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE);
        editor = shared.edit();
    }

    private final String SHARED_NAME = "data";
    private final String DATA_NAME = "data_list";

    private SharedPreferences shared;
    private SharedPreferences.Editor editor;


    public void addInfo(CatData catData) throws JSONException {
        String dataText = shared.getString(DATA_NAME, null);

//        ArrayList<CatData> dataList;
//        if (dataText == null) {
//            dataList = new ArrayList<>();
//        } else {
//            dataList = gson.fromJson(dataText, ArrayList.class);
//        }

        JSONArray dataList;
        if (dataText == null) {
            dataList = new JSONArray();
        } else {
            dataList = new JSONArray(dataText);
        }

        Log.d(TAG, "addInfo: dataList = " + dataList.toString());
        dataList.put(gson.toJson(catData));
        Log.d(TAG, "addInfo: dataList = " + dataList.toString());
        editor.putString(DATA_NAME, dataList.toString());
        editor.apply();
    }


    public ArrayList<CatData> getInfo() throws JSONException {
        String dataText = shared.getString(DATA_NAME, null);

        JSONArray dataList;
        if (dataText == null) {
            dataList = new JSONArray();
        } else {
            dataList = new JSONArray(dataText);
        }

        ArrayList<CatData> returnList = new ArrayList();

        for (int i = 0; i < dataList.length(); i++) {
            returnList.add(0, gson.fromJson(dataList.getString(i), CatData.class));
        }

        return returnList;
    }

    public void updateInfoList(ArrayList<CatData> dataList) {
        JSONArray jsonList = new JSONArray();

        Collections.reverse(dataList);
        for (CatData catData : dataList) {
            jsonList.put(gson.toJson(catData));
        }

        Log.d(TAG, "updateInfoList: " + jsonList.toString());

        editor.putString(DATA_NAME, jsonList.toString());
        editor.commit();
    }

}
