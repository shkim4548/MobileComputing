package com.woon.memopad;
//package com.roopre.simpleboard;

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

public class JoinActivity extends AppCompatActivity{
    //로그 찍을 때 사용하는 TAG 변수
    final private String TAG = getClass().getSimpleName();

    //사용할 컴포넌트 선언
    EditText userid_et, passwd_et;
    Button join_button;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        //컴포넌트 초기화
        userid_et =findViewById(R.id.userid_et);
        passwd_et = findViewById(R.id.passwd_et);
        join_button = findViewById(R.id.join_button);

        join_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //회원가입 함수 호출
                JoinTask joinTask = new JoinTask();
                joinTask.execute(userid_et.getText().toString(), passwd_et.getText().toString());
            }
        });
    }
    //회원가입하는 부분, 단순 Post로 처리
    class JoinTask extends AsyncTask<String, Void, String>{
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            Log.d(TAG, "OnPreExecute");
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);

            Log.d(TAG, "onPostExecute, "+result);

            //결과값이 success로 나오면
            if(result.equals("success")){
                Toast.makeText(getApplicationContext(), "성공적으로 회원가입 되었습니다.",Toast.LENGTH_SHORT).show();
                finish();
            }
            else{
                Toast.makeText(JoinActivity.this, result, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected String doInBackground(String... params){
            String nickname =params[0];
            String pw=params[1];


            //회원가입 API로 연결한다.
            String server_url ="http://10.0.2.2:5116/api/Member/InsertUser";

            URL url;
            String response="";

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
                Log.d(nickname, "execute");
                Uri.Builder builder = new Uri.Builder().appendQueryParameter("nickname", nickname).appendQueryParameter("pw", pw);
                String query = builder.build().getEncodedQuery();
                Log.d("query", query);
                Log.d("queryNickname", nickname);
                Log.d("queryPw", pw);

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
                Log.d("respCode","respCode");

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        response += line;
                        Log.d("respOk",response);
                        Intent intent =new Intent(JoinActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                } else {
                    response = "";
                    Log.d("respElse",response);
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
            return response;

        }
    }
}