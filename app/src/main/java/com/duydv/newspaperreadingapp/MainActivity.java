package com.duydv.newspaperreadingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.AndroidException;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView lvTitle;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> listTitle,listLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addControls();
        addEvents();
    }

    private void addControls() {
        lvTitle = findViewById(R.id.lvTitle);
        listTitle = new ArrayList<>();
        listLink = new ArrayList<>();
        adapter = new ArrayAdapter<>(
                MainActivity.this,
                android.R.layout.simple_list_item_1,
                listTitle
        );
        lvTitle.setAdapter(adapter);
    }

    private void addEvents() {
        new ReadRSS().execute("https://vnexpress.net/rss/the-thao.rss");
        lvTitle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this,NewspaperSecond.class);
                intent.putExtra("link",listLink.get(i));
                startActivity(intent);
            }
        });
    }

    public class ReadRSS extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder content = new StringBuilder();

            try {
                URL url = new URL(strings[0]);

                InputStream inputStream = url.openConnection().getInputStream();

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String line = "";
                while((line = bufferedReader.readLine()) != null){
                    content.append(line);
                }

                bufferedReader.close();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return content.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            XMLDOMParser xmldomParser = new XMLDOMParser();

            Document document = xmldomParser.getDocument(s);

            NodeList nodeList = document.getElementsByTagName("item");

            String title = "";
            String link = "";
            for(int i=0;i < nodeList.getLength();i++){
                Element element = (Element) nodeList.item(i);
                title = xmldomParser.getValue(element,"title");
                link = xmldomParser.getValue(element,"link");
                listTitle.add(title);
                listLink.add(link);
            }

            adapter.notifyDataSetChanged();
        }
    }
}