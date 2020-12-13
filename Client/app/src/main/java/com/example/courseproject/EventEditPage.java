package com.example.courseproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EventEditPage extends AppCompatActivity {

    String title;
    String description;
    String date;
    String u_name;
    String u_phone;
    String u_email;

    TextView date_edit_page;
    int currentYear = Calendar.getInstance().get(Calendar.YEAR);
    int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
    int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

    IAPI iapi;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    int id;
    private List<Events> events = new ArrayList();
    ListView eventsList;
    public static SQLiteHelper sqLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit_page);

        //screenshot will be blocked
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        iapi = RetrofitClient.getInstance().create(IAPI.class);

        date_edit_page = findViewById(R.id.date_edit_page);

        Intent intent = getIntent();
        //Toast.makeText(getApplicationContext(), intent.getStringExtra(MainActivity.EXTRA_EV_ID)  , Toast.LENGTH_SHORT).show();
        id = Integer.decode(intent.getStringExtra(MainActivity.EXTRA_EV_ID));
        title = intent.getStringExtra(MainActivity.EXTRA_EV_TITLE);
        description = intent.getStringExtra(MainActivity.EXTRA_EV_DESCR);
        date = intent.getStringExtra(MainActivity.EXTRA_EV_DATE);
        EditText title_text = (EditText)findViewById(R.id.event_title_edit);
        title_text.setText(title);
        EditText title_descr = (EditText)findViewById(R.id.description_edit);
        title_descr.setText(description);
//        EditText date_text = (EditText)findViewById(R.id.event_date_edit);
//        date_text.setText(date);
        date_edit_page.setText(date);


        events.clear();

        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
        Cursor query = db.rawQuery("SELECT users.name, users.email, users.phone FROM users inner join members " +
                                        "on users.u_id = members.u_id where members.e_id = "+id+";", null);

        String str = "";
        if(query.moveToFirst()){
            do{
                Events event = new Events(0,
                        0,
                        query.getString(0),
                        query.getString(1),
                        query.getString(2));
                events.add(event);
                str += query.getString(0);
            }
            while(query.moveToNext());
        }
        query.close();
        db.close();


        Log.d("tag","eventeditpage" + str);

        eventsList = (ListView) findViewById(R.id.members_list);
        EventAdapter studentAdapter = new EventAdapter(this, R.layout.list_item, events);
        eventsList.setAdapter(studentAdapter);

        AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Events selectedState = (Events) parent.getItemAtPosition(position);
                u_name = String.valueOf(selectedState.getTitle());
                u_phone = String.valueOf(selectedState.getDate());
                u_email = String.valueOf(selectedState.getDescription());

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+u_phone));
                startActivity(intent);

//                View view = new View(getApplicationContext());
//                GoToDetailsMemberPage(view);
            }
        };
        eventsList.setOnItemClickListener(itemListener);

    }

    public void EditEvent(View view){
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);

        EditText title = (EditText)findViewById(R.id.event_title_edit);
        EditText descriptiont = (EditText)findViewById(R.id.description_edit);
        //EditText date = (EditText)findViewById(R.id.event_date_edit);
        String desc = descriptiont.getText().toString();
        Log.d("tag", desc);

        if(title.getText().toString().trim().equals("") || desc.trim().equals("")){
            Toast.makeText(this, "Какое-то из полей пустое", Toast.LENGTH_SHORT).show();
        }
        else{
            db.execSQL("UPDATE events SET title = '"+title.getText()+"'," +
                    "date = '"+date_edit_page.getText()+"'," +
                    "description = '"+desc+"' WHERE e_id = "+id+";");

            Events event = new Events(id,0,title.getText().toString(),desc,date_edit_page.getText().toString());
            //Update in remote db
            Call<Events> call= iapi.updateEvent(event);
            call.enqueue(new Callback<Events>() {
                @Override
                public void onResponse(Call<Events> call, Response<Events> response) {
                    Log.d("tag", response.message()+"");
                }

                @Override
                public void onFailure(Call<Events> call, Throwable throwable) {
                    Log.d("tag", throwable.getMessage());
                }
            });

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void DeleteEvent(View view){
        sqLiteHelper = new SQLiteHelper(this, "app.db", null, 1);
        sqLiteHelper.deleteEvent(id);
        sqLiteHelper.deleteMembers(id);

        Call<Void> call = iapi.deleteEvent(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("tag2", response.message()+"");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                Log.d("tag2", throwable.getMessage());
            }
        });

        Call<Void> call2 = iapi.deleteMembers(id);
        call2.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("tag2", response.message()+"");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                Log.d("tag2", throwable.getMessage());
            }
        });

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

//    public void GoToDetailsMemberPage(View view){
//        Intent intent = new Intent(this, DetailsMemberPage.class);
//        intent.putExtra(MainActivity.EXTRA_EV_TITLE, u_name);
//        intent.putExtra(MainActivity.EXTRA_EV_DESCR, u_phone);
//        intent.putExtra(MainActivity.EXTRA_EV_DATE, u_email);
//        startActivity(intent);
//    }

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
            date_edit_page.setText(currentDay + "." + currentMonth + "." + currentYear);
        }
    };
}
