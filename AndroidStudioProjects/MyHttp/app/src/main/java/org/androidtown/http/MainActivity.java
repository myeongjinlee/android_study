package org.androidtown.http;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    TextView textView;
    String urlStr;
    Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.editText);
        textView = (TextView) findViewById(R.id.textView);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                urlStr = editText.getText().toString();   //url 적힌 editText에서 url 읽어옴
                RequestThread thread = new RequestThread();    //쓰레드 생성
                thread.start();
            }
        });
    }

    class RequestThread extends Thread {
        public void run(){

            try {
                URL url = new URL(urlStr); //url 객체 생성
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();   //자바와 동일
                if(conn != null){
                    conn.setConnectTimeout(10000); //10초 동안 기달렸다가 응답 없으면 timeout
                    conn.setRequestMethod("GET"); //GET 방식으로 통신
                    conn.setDoInput(true);   // server에서 데이터 받을 수 있음
                    conn.setDoOutput(true);  // server로 data 보낼 수 있음
                    int resCode = conn.getResponseCode();  // 이때 연결

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream())); //들어오는 데이터를 받을 수 있는 통로 만듬
                    //한줄씩 읽어들일 수 있음
                    String line = null;
                    while(true){
                        line = reader.readLine();   //한줄씩 읽어들임
                        if(line == null) {
                            break;
                        }
                        println(line);
                    }
                    reader.close();
                    conn.disconnect();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    public void println(final String data){
        handler.post(new Runnable() {
            @Override
            public void run() {
                textView.append(data + "\n");
            }
        });
    }

}
