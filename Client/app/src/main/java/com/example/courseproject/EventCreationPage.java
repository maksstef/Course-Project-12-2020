package com.example.courseproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class EventCreationPage extends AppCompatActivity {

    public static SQLiteHelper sqLiteHelper;
    String id;

    TextView tvDate;
    int currentYear = Calendar.getInstance().get(Calendar.YEAR);
    int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
    int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

    IAPI iapi;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_creation);

        iapi = RetrofitClient.getInstance().create(IAPI.class);

        sqLiteHelper = new SQLiteHelper(this, "app.db", null, 1);
        sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS events " +
                "(e_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " c_id INTEGER," +
                " title TEXT," +
                " description TEXT," +
                " date TEXT)");

        sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS members " +
                "(e_id INTEGER," +
                " u_id INTEGER)" );

        Intent intent = getIntent();
        //Toast.makeText(getApplicationContext(), "id " + intent.getStringExtra(LoginPage.EXTRA_ID) , Toast.LENGTH_SHORT).show();
        id = intent.getStringExtra(LoginPage.EXTRA_ID);
        //id = intent.getStringExtra(MainActivity.EXTRA_EV_ID);
        //Log.d("tag", id);


        tvDate = findViewById(R.id.tvDate);

        //screenshot will be blocked
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
    }


    public void CreateEvent(View view){
        EditText title = (EditText)findViewById(R.id.event_title);
        EditText description = (EditText)findViewById(R.id.description);
        //EditText date = (EditText)findViewById(R.id.event_date);

        //LoginPage loginPage = new LoginPage();
        //int id2 = Integer.decode(MainActivity.user_enterence_id);
        int id2 = LoginPage.user_enterence_id;

        //int id2 = Integer.decode(id);

        Log.d("tag", id2+" "+title.getText().toString().trim()+" "+
                " "+description.getText().toString().trim()+
                " "+tvDate);
        try{
            sqLiteHelper.CreateEvent(
                    //Integer.getInteger(id),
                    id2,
                    title.getText().toString().trim(),
                    description.getText().toString().trim(),
                    tvDate.getText().toString()
            );

            Events event = new Events(0,id2,title.getText().toString().trim(),description.getText().toString().trim(),tvDate.getText().toString());
            compositeDisposable.add(iapi.insertEvent(event)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            //Toast.makeText(EventCreationPage.this, s, Toast.LENGTH_SHORT).show();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            //Toast.makeText(EventCreationPage.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }));
        }
        catch (Exception e){
            e.printStackTrace();
        }

        //sqLiteHelper.queryData("insert into events (e_id, c_id, title, description) values (99,99,'title','description')");
//        String str = "";
//        SQLiteDatabase db2 = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
//        Cursor query2 = db2.rawQuery("SELECT * FROM events;", null);
//        if(query2.moveToFirst()){
//            do{
//                str += query2.getLong(0)+" "+query2.getLong(1)  +" " +query2.getString(2)+" "+query2.getString(3);
//            }
//            while(query2.moveToNext());
//        }
//
//        String str2 = "";
//        SQLiteDatabase db3 = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
//        Cursor query3 = db3.rawQuery("SELECT * FROM members;", null);
//        if(query3.moveToFirst()){
//            do{
//                str2 += query3.getLong(0)+" "+query3.getLong(1)+ "|";
//            }
//            while(query3.moveToNext());
//        }
//        Log.d("tag", str2);
        //Toast.makeText(getApplicationContext(), "chto ne tak "+str, Toast.LENGTH_SHORT).show();



        String str = "";

        int event_id = 0;
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
        Cursor query = db.rawQuery("SELECT e_id FROM events where c_id = '"+id2+"';", null);
        if(query.moveToFirst()){
            do{
                event_id = query.getInt(0);
                str += query.getInt(0) + " ";
            }
            while(query.moveToNext());
        }
        Log.d("tag", "eventcreation "+str +" u_id  "+id2 +"  e_id "+event_id);
//        try{
//            sqLiteHelper.CreateMember(
//                    event_id,
//                    id2
//                    //Integer.getInteger(id)
//
//            );
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }


        db.execSQL("insert into members values ("+event_id+","+id2+")");

        Members member = new Members(event_id, id2);
        compositeDisposable.add(iapi.insertMember(member)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        //Toast.makeText(EventCreationPage.this, s, Toast.LENGTH_SHORT).show();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //Toast.makeText(EventCreationPage.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }));

        //Toast.makeText(getApplicationContext(), "Событие успешно создано!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }




    //test calendar
    public void datePickerHandler(View view) {
        showDialog(1);
    }

    protected Dialog onCreateDialog(int id) {
        if (id == 1) {
            DatePickerDialog tpd = new DatePickerDialog(this, myCallBack, currentYear, currentMonth, currentDay);
            return tpd;
        }
        return super.onCreateDialog(id);
    }

    DatePickerDialog.OnDateSetListener myCallBack = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            currentYear = year;
            currentMonth = monthOfYear + 1;
            currentDay = dayOfMonth;
            tvDate.setText(currentDay + "." + currentMonth + "." + currentYear);
        }
    };
}
