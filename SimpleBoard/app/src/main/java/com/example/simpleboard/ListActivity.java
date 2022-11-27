package com.example.simpleboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class ListActivity extends AppCompatActivity {

    //로그에 사용할 TAG 변수
    final private String TAG =getClass().getSimpleName();

    ListView listView;
    Button reg_button;
    String userid="";
    
    //리스트뷰에 사용할 제목 배열
    ArrayList<String> titleList = new ArrayList<>();
    //클릭했을 때 어떤 게시물을 클릭했는지 게시물 번호를 담기위한 배열
    ArrayList<String> seqList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        
        //LoginActivity에서 넘긴 userid값 받기
        userid=getIntent().getStringExtra("userid");

        //컴포넌트 초기화
        listView = findViewById(R.id.listView);
        
        //listView를 클릭했을 때 이벤트 추가
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
                //어떤 값을 선택했는지 토스트 메시지로 알려준다.
                Toast.makeText(ListActivity.this, adapterView.getItemAtPosition(i)+"클릭", Toast.LENGTH_SHORT).show();
                //게시물 번호와 userid를 가지고 DetailActivity로 이동
                Intent intent = new Intent(ListActivity.this, DetailActivity.class);
                intent.putExtra("board_seq", seqList.get(i));
                intent.putExtra("userid", userid);
                startActivity(intent);
            }
        });
        //버튼 컴포넌트 초기화
        reg_button = findViewById(R.id.reg_button);
        //버튼 이벤트 추가
        reg_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //userid를 가지고 RegisterActivity로 이동한다.
                Intent intent = new Intent(ListActivity.this, RegisterActivity.class);
                intent.putExtra("userid", userid);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onResume(){
        super.onResume();

        //해당 액티비티가 활성화 될 때, 게시물 리스트를 불러오는 함수를 호출
        GetBoard getBoard = new GetBoard();
        getBoard.execute();
    }

    class GetBoard extends AsyncTask<String, Void, String>{
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            Log.d(TAG, "onPreExecute");
        }
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            Log.d(TAG, "onPostExecute, "+result);
            //배열들 초기화
            titleList.clear();
            seqList.clear();

            try{
                JSONArray jsonArray = new JSONArray(result);
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String title = jsonObject.optString("title");
                    String seq = jsonObject.optString("seq");
                    
                    //title, seq 값을 변수로 받아서 배열에 추가
                    titleList.add(title);
                    seqList.add(seq);
                }
                //ListView에서 사용할 arrayAdapter를 생성하고, ListView와 연결
                ArrayAdapter arrayAdapter = new ArrayAdapter<String>(ListActivity.this, android.R.layout.simple_list_item_1, titleList);
                listView.setAdapter(arrayAdapter);

                //arrayAdapter의 데이터가 변경되었을 때 새로고침
                arrayAdapter.notifyDataSetChanged();
            }
            catch(JSONException e){
                e.printStackTrace();
            }
        }
        //게시판의 모든 게시글을 긁어오는 부분
        @Override
        protected String doInBackground(String... params){
            //게시판 불러오기에서 사용될 API를 구성
            String server_url = "34.64.194.119/api/Board/GetBoardList";

            URL url;
            String response = "";

            try{
                url = new URL(server_url);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setDoInput(true);
                conn.setDoOutput(true);
                Uri.Builder builder = new Uri.Builder().appendQueryParameter("userid","");
                String query = builder.build().getEncodedQuery();

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