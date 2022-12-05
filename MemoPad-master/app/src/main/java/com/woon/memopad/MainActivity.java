package com.woon.memopad;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.icu.text.CaseMap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.woon.memopad.Recycler.RecyclerAdapter;
import com.woon.memopad.Room.AppDatabase;
import com.woon.memopad.Room.User;

import java.util.ArrayList;
import java.util.List;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends AppCompatActivity {

    private final int SAVE_MEMO_ACTIVITY = 1;
    private FloatingActionButton add;

    //리사이클러 뷰
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerAdapter adapter;
    private List<User> users;

    final private String TAG = getClass().getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialized();

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);


        add.setOnClickListener(v -> {
            move();
        });
    }


    private void initialized() {
        add = findViewById(R.id.addMemo);

        //GetListTask getListTask = new GetListTask();
        //getListTask.execute();
        recyclerView = findViewById(R.id.mainRecyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        adapter = new RecyclerAdapter();

        users = AppDatabase.getInstance(this).userDao().getAll();
        int size = users.size();
        for(int i = 0; i < size; i++){
            adapter.addItem(users.get(i));
        }

    }

    private void move() {
        Intent intent = new Intent(getApplicationContext(), SaveMemoActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        users = AppDatabase.getInstance(this).userDao().getAll();
        adapter.addItems((ArrayList) users);
        super.onStart();
    }
    class GetListTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "OnPreExecute");
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            if(result.equals("success")){
// 결과값이 success 이면
// 토스트 메시지를 뿌리고
// userid 값을 가지고 ListActivity 로 이동
                Toast.makeText(MainActivity.this, "DB로드 성공", Toast.LENGTH_SHORT).show();
            }else
            {
                Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
            }
        }


        @Override
        protected String doInBackground(String... params) {

            String title = params[0];
            String server_url = "http://10.0.2.2:5288/api/Board/GetAll";


            URL url;
            String response = "";
            try {
                Log.d("try", "try");
                url = new URL(server_url);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("title", title);
                String query = builder.build().getEncodedQuery();
                Log.d("title", title);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                conn.connect();
                int responseCode=conn.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    Log.d("HttpOk","HttpOk");
                    String line;
                    //users =
                }
                else {
                    response="";
                    Log.d("respElse", response);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return response;
        }
    }

}
