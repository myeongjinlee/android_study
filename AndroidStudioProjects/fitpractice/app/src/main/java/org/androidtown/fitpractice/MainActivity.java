package org.androidtown.fitpractice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OnDataPointListener {
    int GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = 0533;
    private GoogleApiClient mFitnessClient;

    SQLiteDatabase database;    //sql 요청시 계속 사용
    EditText editText;
    EditText editText2;
    EditText editText3;
    EditText editText4;
    EditText editText5;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.editText);
        editText2 = (EditText) findViewById(R.id.editText2);
        editText3 = (EditText) findViewById(R.id.editText3);
        editText4 = (EditText) findViewById(R.id.editText4);
        editText5 = (EditText) findViewById(R.id.editText5);

        textView = (TextView) findViewById(R.id.textView);

        //////////////////////////데이터베이스오픈//////////////////////
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String databaseName = editText.getText().toString();
                openDatabase(databaseName);
            }
        });
        ////////////////////////////////////////////////////////////////
        //////////////////////////테이블만들기//////////////////////////
        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tableName = editText2.getText().toString();
                createTable(tableName);
            }
        });
        ////////////////////////////////////////////////////////////////

        //////////////////////////데이터추가하기//////////////////////////
        Button button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editText3.getText().toString().trim();
                String ageStr = editText4.getText().toString().trim();  //trim은 age에 공백있을시 공백제거
                String mobile = editText5.getText().toString().trim();

                int age = -1;
                try {
                    age = Integer.parseInt(ageStr);
                } catch (Exception e) {
                }

                insertData(name, age, mobile);
            }
        });
        ////////////////////////////////////////////////////////////////

        //////////////////////////데이터 조회하기//////////////////////
        Button button4 = (Button) findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tableName = editText2.getText().toString();
                selectData(tableName);
            }
        });
        ////////////////////////////////////////////////////////////////

        mFitnessClient = new GoogleApiClient.Builder(this)
                .addApi(Fitness.SENSORS_API)
                .addApi(Fitness.HISTORY_API)
                .addScope(FitnessScopes)
    }


    public void openDatabase(String databaseName){ //데이터 베이스 오픈
        println("openDatabase() 호출됨.");

        database = openOrCreateDatabase(databaseName, MODE_PRIVATE, null);  //보안 떄문에 mode는 private 많이 사용
        if(database != null){
            println("데이터베이스 오픈됨.");
        }

        //  DatabaseHelper helper = new DatabaseHelper(this, databaseName , null,1);
        //  database =  helper.getWritableDatabase();
    }

    public void createTable(String tableName){
        println("createTable() 호출됨.");

        if(database != null) {
            String sql = "create table " + tableName + "(_id integer PRIMARY KEY autoincrement, name text, age integer, mobile text)";
            database.execSQL(sql);   //결과값 받지 않는 sql 문 사용시 execSQL 메소드 사용

            println("테이블 생성됨.");
        }else {
            println("먼저 데이터베이스를 오픈 하세요");
        }
    }

    public void insertData(String name, int age, String mobile){
        println("insertData() 호출됨.");

        if(database != null){
            String sql = "insert into customer(name,age,mobile) values(?,?,?)";
            Object[] params = {name,age,mobile};

            database.execSQL(sql, params);

            println("데이터 추가함.");
        }else {
            println("먼저 데이터베이스를 오픈 하세요");
        }
    }

    public void selectData(String tableName) {
        println("selectData() 호출 됨.");

        if(database != null){
            String sql = "select name, age, mobile from " + tableName;
            //Cursor로 조회된 데이터 받음
            Cursor cursor = database.rawQuery(sql,null); //결과 값을 받아야 하므로 rawQuery 사용
            println("조회된 데이터 개수 : " + cursor.getCount());

            for(int i=0;i<cursor.getCount();i++){
                cursor.moveToNext();
                String name = cursor.getString(0);
                int age = cursor.getInt(1);
                String mobile = cursor.getString(2);

                println("#" + i + " -> "+ name + ", " + age + ", "+ mobile);
            }

            cursor.close();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onDataPoint(DataPoint dataPoint) {

    }

    class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }
        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            println("onCreate() 호출됨.");

            String tableName = "customer";
            String sql = "create table if not exists " + tableName + "(_id integer PRIMARY KEY autoincrement, name text, age integer, mobile text)";
            sqLiteDatabase.execSQL(sql);   //결과값 받지 않는 sql 문 사용시 execSQL 메소드 사용

            println("테이블 생성됨.");
        }
        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
            println("onUpgrade 호출됨 : " + oldVersion + ", " + newVersion);

            if(newVersion > 1) {
                String tableName = "customer";
                sqLiteDatabase.execSQL("drop table if exists " + tableName); // alter table을 이용함!!!
            }


        }
    }

    public void println(String data) {
        textView.append(data + "\n");
    }
}
