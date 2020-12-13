package com.example.courseproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginPage extends AppCompatActivity {


    public final static String EXTRA_ID = "EXTRA_ID";
    public final static String EXTRA_NAME = "EXTRA_NAME";
    int id;
    public static int user_enterence_id;

    IAPI iapi;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        iapi = RetrofitClient.getInstance().create(IAPI.class);

        //screenshot will be blocked
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

//        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
//
//        db.execSQL("drop table users");
//        db.execSQL("drop table events");
//        db.execSQL("drop table members");
//        db.execSQL("delete from users");
//        db.execSQL("delete from  members");
//        db.execSQL("delete from  events");




    }

    public void SignIn(View view){

//        final String username = "vjiklik@gmail.com";
//        final String password2 = "517327maks";
//        Properties props = new Properties();
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.smtp.host", "smtp.gmail.com");
//        props.put("mail.smtp.port", "587");
//
//        Session session = Session.getInstance(props,
//                new javax.mail.Authenticator() {
//                    protected PasswordAuthentication getPasswordAuthentication() {
//                        return new PasswordAuthentication(username, password2);
//                    }
//                });
//        try {
//            Message message = new MimeMessage(session);
//            message.setFrom(new InternetAddress(username));
//            message.setRecipients(Message.RecipientType.TO,
//                    InternetAddress.parse("boss.canta@mail.ru"));
//            message.setSubject("Testing Subject");
//            message.setText("Dear Mail Crawler,"
//                    + "\n\n No spam to my email, please!");
//            Transport.send(message);
//        } catch (MessagingException e) {
//            throw new RuntimeException(e);
//        }





        Intent intent = new Intent(this, MainActivity.class);
        EditText login = (EditText)findViewById(R.id.login);
        EditText password = (EditText)findViewById(R.id.password);

        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);

        //Log.d("tag2", "login:"+login.getText().toString()+",password:"+password.getText().toString()+".");
        if(login.getText().toString().trim().equals("") || password.getText().toString().trim().equals("")){
            Toast.makeText(this, "Логин или пароль пустой", Toast.LENGTH_SHORT).show();
        }
        else{

            final AlertDialog dialog = new SpotsDialog.Builder()
                    .setContext(LoginPage.this)
                    .build();
            dialog.show();

            try {
            Users user = new Users(0, "", login.getText().toString().trim(), encrypt(password.getText().toString().getBytes(), ("0123000000000215").getBytes()), "", "");

            compositeDisposable.add(iapi.loginUser(user)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String s) throws Exception {
                            //Toast.makeText(LoginPage.this, "Success!", Toast.LENGTH_SHORT).show();
                            LoginPage loginPage = new LoginPage();
                            loginPage.user_enterence_id = Integer.parseInt(s); //Integer.getInteger(s);
                            Log.d("tag2", "login page     " + user_enterence_id);
                            dialog.dismiss();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            dialog.dismiss();
                            //attempt to invoke virtual method
                            Toast.makeText(LoginPage.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }));
            }catch(Exception e){

            }


            Cursor query = db.rawQuery("SELECT * FROM users where login = '"+login.getText().toString().trim()+"' and password = '"+password.getText().toString().trim()+"';", null);

            if(query.moveToFirst()){
                do{
                    intent.putExtra(EXTRA_NAME, query.getString(1));
                    intent.putExtra(EXTRA_ID, String.valueOf(query.getLong(0)));
                    id = (int)query.getLong(0);
                    //user_enterence_id = (int)query.getLong(0);
                }
                while(query.moveToNext());
            }

            if(query.getCount() != 0){
                startActivity(intent);
            }

            query.close();
            db.close();

            LoginPage loginPage = new LoginPage();
            //Log.d("tag2", "login page "+loginPage.user_enterence_id);

            //удалять данные после входа
            login.setText("");
            password.setText("");

            //Log.d("tag2", "login page2 "+loginPage.user_enterence_id);

        }
    }

    public void SignUp(View view){
        Intent intent = new Intent(this, RegisterPage.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void onDestroy() {
        moveTaskToBack(true);
        super.onDestroy();
        System.runFinalizersOnExit(true);
        System.exit(0);
    }

    private static String encrypt(byte[] key, byte[] clear) throws Exception
    {
        MessageDigest md = MessageDigest.getInstance("md5");
        byte[] digestOfPassword = md.digest(key);

        SecretKeySpec skeySpec = new SecretKeySpec(digestOfPassword, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(clear);
        return Base64.encodeToString(encrypted,Base64.DEFAULT);
    }
}
