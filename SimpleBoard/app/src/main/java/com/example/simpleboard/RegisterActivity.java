package com.example.simpleboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class RegisterActivity extends AppCompatActivity {

    //로그에 사용할 TAG
    final private String TAG = getClass().getSimpleName();

    //사용할 컴포넌트 선언
    EditText title_et, content_et;
    Button reg_button;

    //유저아이디 변수
    String userid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //ListActivity에서 넘긴 userid를 변수로 받음
        userid=getIntent().getStringExtra("userid");
        
        //컴포넌트 초기화
        title_et = findViewById(R.id.title_et);
        content_et = findViewById(R.id.content_et);
        reg_button = findViewById(R.id.reg_button);
        
        //버튼 이벤트 추가
        reg_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //게시물 등록 함수
                RegBoard regBoard = new RegBoard();
                regBoard.execute(userid, title_et.getText().toString(), content_et.getText().toString());
            }
        });
    }
    class RegBoard extends AsyncTask<String, Void, String>{
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            Log.d(TAG, "onPreExecute");
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(TAG, "onPostExecute " + result);

            //결과값이 성공으로 나올 경우 -> 토스트 메시지를 뿌린다. -> 이전 액티비티로 이동한다. -> 이때 ListActivity의 onResume 함수가 호출된다.
            if (result.equals("success")) {
                Toast.makeText(RegisterActivity.this, "등록되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(RegisterActivity.this, result, Toast.LENGTH_SHORT).show();
            }
        }
        //회원가입 api를 연결하는 부분
    @Override
    protected String doInBackground(String... params){
        String userid = params[0];
        String title = params[1];
        String content = params[2];

        String server_url = "34.64.194.119/api/Member/InsertUser";

        URL url;
        String response = "";
        try {
            url = new URL(server_url);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("userid", userid)
                    .appendQueryParameter("title", title)
                    .appendQueryParameter("content", content);
            String query = builder.build().getEncodedQuery();

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();

            conn.connect();
            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
            }
            else {
                response="";

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }
    }
}