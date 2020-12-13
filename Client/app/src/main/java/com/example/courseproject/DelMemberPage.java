package com.example.courseproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class DelMemberPage extends AppCompatActivity {

    String title;
    String description;
    String date;
    TextView date_del_member_page;
    int id;

    IAPI iapi;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_del_member_page);

        iapi = RetrofitClient.getInstance().create(IAPI.class);

        Intent intent = getIntent();
        id = Integer.decode(intent.getStringExtra(MainActivity.EXTRA_EV_ID));
        title = intent.getStringExtra(MainActivity.EXTRA_EV_TITLE);
        description = intent.getStringExtra(MainActivity.EXTRA_EV_DESCR);
        date = intent.getStringExtra(MainActivity.EXTRA_EV_DATE);
        EditText title_text = (EditText)findViewById(R.id.title_del_member);
        title_text.setText(title);
        EditText title_descr = (EditText)findViewById(R.id.description_del_member);
        title_descr.setText(description);

        date_del_member_page = findViewById(R.id.date_del_member_page);
        date_del_member_page.setText(date);

        //screenshot will be blocked
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
    }

    public void UnregisterMember(View view){
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);

        db.execSQL("delete from members where e_id = "+id+" and u_id = "+LoginPage.user_enterence_id+";");

        compositeDisposable.add(iapi.delMember(id,LoginPage.user_enterence_id)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                //Toast.makeText(DelMemberPage.this, s, Toast.LENGTH_SHORT).show();
                Log.d("tag", s);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.d("tag", throwable.getMessage());
                //Toast.makeText(DelMemberPage.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }));

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
