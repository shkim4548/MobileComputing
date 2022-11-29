package com.example.simpleboard;
//package com.roopre.simpleboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
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

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class LoginActivity extends AppCompatActivity {

    //로그에 사용할 TAG 변수 선언, final 키워드로 선언하여 변수의 값이 런타임에서 변화하지 못하도록 함
    final private String TAG = getClass().getSimpleName();
    
    //사용할 컴포넌트 선언
    EditText userid_et, passwd_et;
    Button login_button, join_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //사용할 컴포넌트 초기화
        userid_et = findViewById(R.id.userid_et);
        passwd_et = findViewById(R.id.passwd_et);
        login_button = findViewById(R.id.login_button);
        join_button = findViewById(R.id.join_button);
        
        //로그인 버튼 이벤트 추가
        login_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                LoginTask loginTask = new LoginTask();
                loginTask.execute(userid_et.getText().toString(), passwd_et.getText().toString());
            }
        });
    }

    //여기가 서버상에 쏘는 부분, 서버 URL은 .net core로 만든 backendAPI로 넣고
    class LoginTask extends AsyncTask<String, Void, String>{
        //서버로 쏘기 전에 쏘았다는 로그를 남기는 부분
        @Override
        protected void onPreExecute(){
            super.onPreExecute();

            Log.d(TAG, "onPreExecute");
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            Log.d(TAG, "OnPostExecute, " + result);

            if(result.equals("success")){
                //결과가 success -> 토스트 메시지 전송 -> userid 값을 가지고 ListActivity로 이동
                Toast.makeText(LoginActivity.this, "로그인 되었습니다", Toast.LENGTH_SHORT).show();

                //Intent는 컴포넌트간의 통신을 위해 사용한다.
                Intent intent = new Intent(LoginActivity.this, ListActivity.class);
                intent.putExtra("userid", userid_et.getText().toString());
                startActivity(intent);
            }
            else{
                Toast.makeText(LoginActivity.this, result, Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        protected  String doInBackground(String... params){
            String nickname = params[0];
            String pw = params[1];

            String server_url = "34.64.194.119/api/Member/LoginUser";

            URL url;
            String response="";

            //서버에 연결을 시도하는 부분, API에 포스트 방식으로 API변수 userid, passwd 변수를 호출하여 넘긴다.
            try{
                url = new URL(server_url);

                HttpURLConnection conn =(HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                Uri.Builder builder = new Uri.Builder().appendQueryParameter("nickname", nickname).appendQueryParameter("pw", pw);

                String query = builder.build().getEncodedQuery();
                Log.d("query", query);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                conn.connect();
                int responseCode = conn.getResponseCode();

                if(responseCode == HttpsURLConnection.HTTP_OK){
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while((line=br.readLine())!=null){
                        response+=line;
                    }
                }
                else{
                    response="";
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
            return response;
        }
    }
}