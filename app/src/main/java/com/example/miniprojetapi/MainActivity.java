package com.example.miniprojetapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONObject;

import java.io.Console;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listChampView = (ListView) findViewById(R.id.listchamp);
        TextView inputSearch = (TextView) findViewById((R.id.inputSearch));
        ArrayList< Champion > listChamp = new ArrayList< Champion >();

        final ChampionAdapter mAdapter = new ChampionAdapter(this,listChamp);

        listChampView.setAdapter(mAdapter);


        Ion.with(getBaseContext())
                .load("http://ddragon.leagueoflegends.com/cdn/9.23.1/data/en_US/champion.json")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        if(result!=null)
                        Log.d("test",result.get("version").getAsString());
                        JsonObject data = result.get("data").getAsJsonObject();



                        for (Map.Entry<String, JsonElement> entry : data.entrySet()){

                            String key = entry.getValue().getAsJsonObject().get("key").getAsString();
                            String name = entry.getValue().getAsJsonObject().get("name").getAsString();
                            String title = entry.getValue().getAsJsonObject().get("title").getAsString();

                            JsonObject img = (JsonObject) entry.getValue().getAsJsonObject().get("image");
                            String imgName = img.get("full").getAsString();

                            Champion champ = new Champion ( key, name,title,imgName);

                           // Log.println(Log.DEBUG,"tag",champ.getName());
                            mAdapter . add ( champ );

                        }

                        listChampView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                //String item = (String) listChampView.getItemAtPosition(position).toString();
                                //Log.d("Click", "You selected : " + item);

                                Intent intent = new Intent(MainActivity.this, ChampionFocusActivity.class);
                                intent.putExtra("champion", (Serializable) listChampView.getItemAtPosition(position));
                                startActivity(intent);
                            }
                        });
                        // do stuff with the result or error
                    }
                });
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (count < before) {
                    mAdapter.resetData();
                }

                mAdapter.getFilter().filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
