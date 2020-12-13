package com.example.courseproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public final static String EXTRA_EV_ID = "EXTRA_EV_ID";
    public final static String EXTRA_EV_TITLE = "EXTRA_EV_TITLE";
    public final static String EXTRA_EV_DESCR = "EXTRA_EV_DESCR";
    public final static String EXTRA_EV_DATE = "EXTRA_EV_DATE";
    String id;
    //String name;
    String event_id;
    String event_title;

    public static String user_enterence_id;

    String event_description;
    String event_date;
    private List<Events> events = new ArrayList();
    ListView eventsList;

    public static List<Events> events2 = new ArrayList();
    ListView eventsList2;
    JSONArray jsonarray = null;
    
    String event_id2;
    String event_title2;
    String event_description2;
    String event_date2;
    public static SQLiteHelper sqLiteHelper;

    IAPI iapi;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    int i = 0;
    //возможно i2 нужно запихнуть в oncreate
    //или апдейт каждую секунду делать вместо 3х
    int i2 = -1;
    Timer timer;

    @Override
    protected void onStop() {
        super.onStop();
        timer.cancel();
        //timer.purge();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iapi = RetrofitClient.getInstance().create(IAPI.class);

        //screenshot will be blocked
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        //Intent intent = getIntent();
        //Toast.makeText(getApplicationContext(), intent.getStringExtra(LoginPage.EXTRA_NAME) + " "+intent.getStringExtra(LoginPage.EXTRA_ID) , Toast.LENGTH_SHORT).show();
        //id = intent.getStringExtra(LoginPage.EXTRA_ID);


//        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {
//            @Override
//            public void run() {
//                i++;
//                Toast.makeText(MainActivity.this, "3: "+i, Toast.LENGTH_SHORT).show();
//            }
//        }, 0, 3, TimeUnit.SECONDS);

        timer = new Timer();
        long delay = 0;
        long period = 3000;
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //time++;
                //Toast.makeText(MainActivity.this, "1: "+time, Toast.LENGTH_SHORT).show();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        iapi.synchronize().enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                String s = response.body();
                                i = Integer.parseInt(s);
                                //Log.d("tag2", i+"  ");
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {

                            }
                        });

                        if(i != i2){

                        events.clear();
                        events2.clear();

                        iapi.getEvents(LoginPage.user_enterence_id)
                                .enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        JSONArray jsonarray = null;
                                        try {
                                            events.clear();
                                            jsonarray = new JSONArray(response.body());
                                            for (int i = 0; i < jsonarray.length(); i++) {
                                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                                Events event = new Events(jsonobject.getInt("EId"),
                                                        jsonobject.getInt("CId"),
                                                        jsonobject.getString("Title"),
                                                        jsonobject.getString("Description"),
                                                        jsonobject.getString("Date"));
                                                events.add(event);
                                            }

                                            eventsList = (ListView) findViewById(R.id.registered_events);
                                            EventAdapter studentAdapter = new EventAdapter(MainActivity.this, R.layout.list_item, events2);
                                            eventsList.setAdapter(studentAdapter);
                                            AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                                                    Events selectedState = (Events) parent.getItemAtPosition(position);
                                                    event_id = String.valueOf(selectedState.getEid());
                                                    event_title = String.valueOf(selectedState.getTitle());
                                                    event_description = String.valueOf(selectedState.getDescription());
                                                    event_date = String.valueOf(selectedState.getDate());

                                                    View view = new View(getApplicationContext());

                                                    Boolean check = CheckCreator();
                                                    if(check == true){
                                                        GoToEditPage(view);
                                                    }
                                                    else{
                                                        GoToDelMemberPage(view);
                                                    }
                                                }
                                            };
                                            eventsList.setOnItemClickListener(itemListener);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
                                        //Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                        LoginPage loginPage = new LoginPage();
                        Log.d("tag2", "main activity page "+loginPage.user_enterence_id);
                        iapi.getSignUpEvents(LoginPage.user_enterence_id)
                                .enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {

                                        //List<Events> events = new ArrayList();
                                        //JSONArray jsonarray = null;
                                        try {
                                            events2.clear();
                                            jsonarray = new JSONArray(response.body());
                                            for (int i = 0; i < jsonarray.length(); i++) {
                                                JSONObject jsonobject = jsonarray.getJSONObject(i);
                                                Events event = new Events(jsonobject.getInt("EId"),
                                                        jsonobject.getInt("CId"),
                                                        jsonobject.getString("Title"),
                                                        jsonobject.getString("Description"),
                                                        jsonobject.getString("Date"));
                                                events2.add(event);
                                            }

                                            eventsList2 = (ListView) findViewById(R.id.plain_events);
                                            EventAdapter studentAdapter2 = new EventAdapter(MainActivity.this, R.layout.list_item, events);
                                            eventsList2.setAdapter(studentAdapter2);
                                            //event_description = "";
                                            AdapterView.OnItemClickListener itemListener2 = new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                                                    Events selectedState = (Events) parent.getItemAtPosition(position);
                                                    event_id2 = String.valueOf(selectedState.getEid());
                                                    event_title2 = String.valueOf(selectedState.getTitle());
                                                    event_description2 = String.valueOf(selectedState.getDescription());
                                                    event_date2 = String.valueOf(selectedState.getDate());

                                                    View view = new View(getApplicationContext());
                                                    GoToMemberPage(view);
                                                }
                                            };
                                            eventsList2.setOnItemClickListener(itemListener2);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        for (Events i : events2){
                                            Log.d("tag", "events id: "+i.getEid());
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
                                        //Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                                i2 = i;
                        }
                    }
                });
            }
        },delay,period);



//        events.clear();
//        events2.clear();
//
//        iapi.getEvents(LoginPage.user_enterence_id)
//                .enqueue(new Callback<String>() {
//                    @Override
//                    public void onResponse(Call<String> call, Response<String> response) {
//                        JSONArray jsonarray = null;
//                        try {
//                            events.clear();
//                            jsonarray = new JSONArray(response.body());
//                            for (int i = 0; i < jsonarray.length(); i++) {
//                                JSONObject jsonobject = jsonarray.getJSONObject(i);
//                                Events event = new Events(jsonobject.getInt("EId"),
//                                        jsonobject.getInt("CId"),
//                                        jsonobject.getString("Title"),
//                                        jsonobject.getString("Description"),
//                                        jsonobject.getString("Date"));
//                                events.add(event);
//                            }
//
//                            eventsList = (ListView) findViewById(R.id.registered_events);
//                            EventAdapter studentAdapter = new EventAdapter(MainActivity.this, R.layout.list_item, events2);
//                            eventsList.setAdapter(studentAdapter);
//                            AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
//                                @Override
//                                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//                                    Events selectedState = (Events) parent.getItemAtPosition(position);
//                                    event_id = String.valueOf(selectedState.getEid());
//                                    event_title = String.valueOf(selectedState.getTitle());
//                                    event_description = String.valueOf(selectedState.getDescription());
//                                    event_date = String.valueOf(selectedState.getDate());
//
//                                    View view = new View(getApplicationContext());
//
//                                    Boolean check = CheckCreator();
//                                    if(check == true){
//                                        GoToEditPage(view);
//                                    }
//                                    else{
//                                        GoToDelMemberPage(view);
//                                    }
//                                }
//                            };
//                            eventsList.setOnItemClickListener(itemListener);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<String> call, Throwable t) {
//                        //Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//        LoginPage loginPage = new LoginPage();
//        Log.d("tag2", "main activity page "+loginPage.user_enterence_id);
//        iapi.getSignUpEvents(LoginPage.user_enterence_id)
//                .enqueue(new Callback<String>() {
//                    @Override
//                    public void onResponse(Call<String> call, Response<String> response) {
//
//                        //List<Events> events = new ArrayList();
//                        //JSONArray jsonarray = null;
//                        try {
//                            events2.clear();
//                            jsonarray = new JSONArray(response.body());
//                            for (int i = 0; i < jsonarray.length(); i++) {
//                                JSONObject jsonobject = jsonarray.getJSONObject(i);
//                                Events event = new Events(jsonobject.getInt("EId"),
//                                        jsonobject.getInt("CId"),
//                                        jsonobject.getString("Title"),
//                                        jsonobject.getString("Description"),
//                                        jsonobject.getString("Date"));
//                                events2.add(event);
//                            }
//
//                            eventsList2 = (ListView) findViewById(R.id.plain_events);
//                            EventAdapter studentAdapter2 = new EventAdapter(MainActivity.this, R.layout.list_item, events);
//                            eventsList2.setAdapter(studentAdapter2);
//                            //event_description = "";
//                            AdapterView.OnItemClickListener itemListener2 = new AdapterView.OnItemClickListener() {
//                                @Override
//                                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//                                    Events selectedState = (Events) parent.getItemAtPosition(position);
//                                    event_id2 = String.valueOf(selectedState.getEid());
//                                    event_title2 = String.valueOf(selectedState.getTitle());
//                                    event_description2 = String.valueOf(selectedState.getDescription());
//                                    event_date2 = String.valueOf(selectedState.getDate());
//
//                                    View view = new View(getApplicationContext());
//                                    GoToMemberPage(view);
//                                }
//                            };
//                            eventsList2.setOnItemClickListener(itemListener2);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        for (Events i : events2){
//                            Log.d("tag", "events id: "+i.getEid());
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<String> call, Throwable t) {
//                        //Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });


//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

//        name = intent.getStringExtra(LoginPage.EXTRA_NAME);
//        Toast.makeText(this, "Добрый день, "+name+"!", Toast.LENGTH_SHORT).show();

//        user_enterence_id =  intent.getStringExtra(LoginPage.EXTRA_ID);
//        Log.d("tag", "main "+user_enterence_id);



//        events.clear();
//        events2.clear();

//        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
//        //Cursor query2 = db.rawQuery("insert into events(e_id, c_id, title, description) values(6,6,'event','event description');", null);
//        //Cursor query = db.rawQuery("SELECT * FROM events;", null);
//        Cursor query = db.rawQuery("SELECT * FROM events inner join members " +
//                "on events.e_id = members.e_id where members.u_id = "+LoginPage.user_enterence_id+";", null);
//
//        String str = "";
//        if(query.moveToFirst()){
//            do{
//                Events event = new Events(query.getInt(0),
//                                        query.getInt(1),
//                                        query.getString(2),
//                                        query.getString(3),
//                                        query.getString(4));
//                events.add(event);
//                str += query.getString(3)+" ";
//            }
//            while(query.moveToNext());
//        }
//        //Log.d("tag", "something wrong - "+str);
//
//        //db.execSQL("delete from events");
//        //db.execSQL("delete from members");
//        //Toast.makeText(getApplicationContext(),str, Toast.LENGTH_LONG ).show();
//
//        //query2.close();
//        query.close();
//        db.close();

//    iapi.getEvents(LoginPage.user_enterence_id)
//            .enqueue(new Callback<String>() {
//                @Override
//                public void onResponse(Call<String> call, Response<String> response) {
//                    JSONArray jsonarray = null;
//                    try {
//                        jsonarray = new JSONArray(response.body());
//                        for (int i = 0; i < jsonarray.length(); i++) {
//                            JSONObject jsonobject = jsonarray.getJSONObject(i);
//                            Events event = new Events(jsonobject.getInt("EId"),
//                                    jsonobject.getInt("CId"),
//                                    jsonobject.getString("Title"),
//                                    jsonobject.getString("Description"),
//                                    jsonobject.getString("Date"));
//                            events.add(event);
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<String> call, Throwable t) {
//                    Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            });

//        eventsList = (ListView) findViewById(R.id.registered_events);
//        EventAdapter studentAdapter = new EventAdapter(this, R.layout.list_item, events2);
//        eventsList.setAdapter(studentAdapter);

        //event_description = "";
//        AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//                Events selectedState = (Events) parent.getItemAtPosition(position);
//                event_id = String.valueOf(selectedState.getEid());
//                event_title = String.valueOf(selectedState.getTitle());
//                event_description = String.valueOf(selectedState.getDescription());
//                event_date = String.valueOf(selectedState.getDate());
//
//                View view = new View(getApplicationContext());
//
//                Boolean check = CheckCreator();
//                if(check == true){
//                    GoToEditPage(view);
//                }
//                else{
//                    GoToDelMemberPage(view);
//                }
//            }
//        };
//        eventsList.setOnItemClickListener(itemListener);



//        SQLiteDatabase db2 = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
////        Cursor query2 = db2.rawQuery("select * from events inner join members " +
////                                            "on events.e_id = members.e_id except " +
////                                            "SELECT * FROM events inner join members " +
////                                            "on events.e_id = members.e_id where members.u_id = "+LoginPage.user_enterence_id+";", null);
//
//        Cursor query2 = db2.rawQuery("SELECT * FROM events where e_id not in (select distinct e_id from members where u_id = "+LoginPage.user_enterence_id+");", null);
//
//
//        if(query2.moveToFirst()){
//            do{
//                Events event = new Events(query2.getInt(0),
//                        query2.getInt(1),
//                        query2.getString(2),
//                        query2.getString(3),
//                        query2.getString(4));
//                events2.add(event);
//                //str += query.getString(3)+" ";
//            }
//            while(query2.moveToNext());
//
//
//        }
//        query2.close();
//        db.close();

//        LoginPage loginPage = new LoginPage();
//        Log.d("tag2", "main activity page "+loginPage.user_enterence_id);
//        iapi.getSignUpEvents(LoginPage.user_enterence_id)
//                .enqueue(new Callback<String>() {
//                    @Override
//                    public void onResponse(Call<String> call, Response<String> response) {
//
//                        //List<Events> events = new ArrayList();
//                        //JSONArray jsonarray = null;
//                        try {
//                            jsonarray = new JSONArray(response.body());
//                            for (int i = 0; i < jsonarray.length(); i++) {
//                                JSONObject jsonobject = jsonarray.getJSONObject(i);
//                                Events event = new Events(jsonobject.getInt("EId"),
//                                        jsonobject.getInt("CId"),
//                                        jsonobject.getString("Title"),
//                                        jsonobject.getString("Description"),
//                                        jsonobject.getString("Date"));
//                                events2.add(event);
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        for (Events i : events2){
//                            Log.d("tag", "events id: "+i.getEid());
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<String> call, Throwable t) {
//                        Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });

        Log.d("tag","now values after using method");
        for (Events i : events2){
            Log.d("tag", "events id after method: "+i.getEid());
        }

//        eventsList2 = (ListView) findViewById(R.id.plain_events);
//        EventAdapter studentAdapter2 = new EventAdapter(this, R.layout.list_item, events);
//        eventsList2.setAdapter(studentAdapter2);
//
//        //event_description = "";
//        AdapterView.OnItemClickListener itemListener2 = new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//                Events selectedState = (Events) parent.getItemAtPosition(position);
//                event_id2 = String.valueOf(selectedState.getEid());
//                event_title2 = String.valueOf(selectedState.getTitle());
//                event_description2 = String.valueOf(selectedState.getDescription());
//                event_date2 = String.valueOf(selectedState.getDate());
//
//                View view = new View(getApplicationContext());
//                GoToMemberPage(view);
//            }
//        };
//        eventsList2.setOnItemClickListener(itemListener2);
    }

    public void GoToEditPage(View view){
        Intent intent = new Intent(this, EventEditPage.class);
        intent.putExtra(EXTRA_EV_ID, event_id);
        intent.putExtra(EXTRA_EV_TITLE, event_title);
        intent.putExtra(EXTRA_EV_DESCR, event_description);
        intent.putExtra(EXTRA_EV_DATE, event_date);
        startActivity(intent);
    }

    public void GoToMemberPage(View view){
        Intent intent = new Intent(this, MemberPage.class);
        intent.putExtra(EXTRA_EV_ID, event_id2);
        intent.putExtra(EXTRA_EV_TITLE, event_title2);
        intent.putExtra(EXTRA_EV_DESCR, event_description2);
        intent.putExtra(EXTRA_EV_DATE, event_date2);
        startActivity(intent);
    }

    public void GoToDelMemberPage(View view){
        Intent intent = new Intent(this, DelMemberPage.class);
        intent.putExtra(EXTRA_EV_ID, event_id);
        intent.putExtra(EXTRA_EV_TITLE, event_title);
        intent.putExtra(EXTRA_EV_DESCR, event_description);
        intent.putExtra(EXTRA_EV_DATE, event_date);
        startActivity(intent);
    }

    public void OpenCreateForm(View view){
        Intent intent = new Intent(this, EventCreationPage.class);
        intent.putExtra(LoginPage.EXTRA_ID, id);
        startActivity(intent);
    }

    public Boolean CheckCreator(){
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
        Cursor query = db.rawQuery("SELECT * FROM events where e_id = "+event_id+" and c_id = "+LoginPage.user_enterence_id+";", null);

        if(query.getCount() == 1){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, LoginPage.class);
        startActivity(intent);
    }
}
