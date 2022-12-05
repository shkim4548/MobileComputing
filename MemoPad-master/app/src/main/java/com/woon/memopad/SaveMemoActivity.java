package com.woon.memopad;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.woon.memopad.Room.AppDatabase;
import com.woon.memopad.Room.User;

import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class SaveMemoActivity extends AppCompatActivity {

    private final int REQUEST_CODE = 200;
    private EditText description;
    private TextView result;
    private AppDatabase db;

    final private String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_memo);

        initialized();
    }

    private void initialized() {
        description = findViewById(R.id.description);
        result = findViewById(R.id.result);

        db = AppDatabase.getInstance(this);
    }

    //메모저장하는 버튼
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_memo_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.save:
                make_title();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void make_title() {

        EditText editText = new EditText(getApplicationContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("제목을 입력하세요");
        builder.setView(editText);

        builder.setPositiveButton("저장", (dialog, which) -> {
            String s = editText.getText().toString();
            // db에 저장하기
            User memo = new User(s, description.getText().toString());
            db.userDao().insert(memo);
            Toast.makeText(getApplicationContext(),"저장되었습니다",Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            finish();
        });

        builder.setNegativeButton("취소", (dialog, which) -> {
            dialog.dismiss();
        });

        builder.show();
    }

    class SaveMemoTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "OnPreExecute");
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result.equals("success")) {

            } else {

            }
        }

        @Override
        protected String doInBackground(String... params) {
            String title = params[0];
            String content = params[1];


            //회원가입 API로 연결한다.
            String server_url = "http://10.0.2.2:5288/api/Board/InsertBoard";

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
                //이부분은 API와의 통일을 위하여 변수명 수정 userid -> nickname, passwd -> pw
                //Log.d(nickname, "execute");
                Uri.Builder builder = new Uri.Builder().appendQueryParameter("title", title).appendQueryParameter("content", content);
                String query = builder.build().getEncodedQuery();
                Log.d("query", query);
                Log.d("title", title);
                Log.d("content", content);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                conn.connect();
                Log.d("param1", params[0]);
                Log.d("param2", params[1]);
                int responseCode = conn.getResponseCode();
                //여기부터 작동하지 않는다. -> 서버에서 응답이 오지 않음
                Log.d("respCode", "respCode");

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        response += line;
                        Log.d("respOk", response);
                        Intent intent = new Intent(SaveMemoActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                } else {
                    response = "";
                    Log.d("respElse", response);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;

        }
    }
}
