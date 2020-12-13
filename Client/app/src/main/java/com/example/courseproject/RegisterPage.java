package com.example.courseproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class RegisterPage extends AppCompatActivity {

    IAPI iapi;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    public static SQLiteHelper sqLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        //screenshot will be blocked
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        iapi = RetrofitClient.getInstance().create(IAPI.class);

        sqLiteHelper = new SQLiteHelper(this, "app.db", null, 1);
        sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS users " +
                "(u_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " name TEXT," +
                " login TEXT," +
                " password TEXT," +
                " email TEXT," +
                " phone TEXT)");
        sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS events " +
                "(e_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " c_id INTEGER," +
                " title TEXT," +
                " description TEXT," +
                " date TEXT)");
        sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS members " +
                "(e_id INTEGER," +
                " u_id INTEGER)" );

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    public void SignUp(View view){
        EditText name = (EditText)findViewById(R.id.name);
        EditText login = (EditText)findViewById(R.id.login);
        EditText password = (EditText)findViewById(R.id.password);
        EditText password2 = (EditText)findViewById(R.id.password2);
        EditText phone = (EditText)findViewById(R.id.phone);
        EditText email = (EditText)findViewById(R.id.email);

        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
        Cursor query = db.rawQuery("SELECT * FROM users where login = '"+login.getText().toString().trim()+"';", null);

        if(login.getText().toString().trim().equals("") || password.getText().toString().trim().equals("") ||
                name.getText().toString().trim().equals("") || phone.getText().toString().trim().equals("") ||
                email.getText().toString().trim().equals("")){
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
        }
        else if(query.getCount() != 0){
            Toast.makeText(this, "Данный логин уже используется", Toast.LENGTH_SHORT).show();
        }
        else if(!password.getText().toString().trim().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{5,15}$")){
            Toast.makeText(this, "Проблемы с паролем", Toast.LENGTH_SHORT).show();
        }
        else if(!login.getText().toString().trim().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{5,15}$")) {
            Toast.makeText(this, "Проблемы с логином", Toast.LENGTH_SHORT).show();
        }
        else if(!email.getText().toString().trim().matches("^([\\w-\\.]+){1,64}@([\\w&&[^_]]+){2,255}.[a-z]{2,}$")){
            Toast.makeText(this, "Проблемы с email", Toast.LENGTH_SHORT).show();
        }
        else if(!password.getText().toString().equals(password2.getText().toString())) {
            Toast.makeText(this, "Пароли должны совпадать", Toast.LENGTH_SHORT).show();
        }
        else{
            try{
                sqLiteHelper.SignUp(
                        name.getText().toString().trim(),
                        login.getText().toString().trim(),
                        password.getText().toString().trim(),
                        email.getText().toString().trim(),
                        phone.getText().toString().trim()
                );

                final AlertDialog dialog = new SpotsDialog.Builder()
                        .setContext(RegisterPage.this)
                        .build();
                dialog.show();

                Users user = new Users(0, name.getText().toString(),
                        login.getText().toString(),
                        encrypt(password.getText().toString().getBytes(), ("0123000000000215").getBytes()),
                        phone.getText().toString(),
                        email.getText().toString());

                compositeDisposable.add(iapi.registerUser(user)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String s) throws Exception {
                                if(!s.equals("User is existing!"))
                                {
                                    finish();
                                }
                                //Toast.makeText(RegisterPage.this, s, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                dialog.dismiss();
                                //Toast.makeText(RegisterPage.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }));

                MailForNewUser sm = new MailForNewUser(this, email.getText().toString().trim(), "You are welcome!", "Thank for registration in our app)");
                sm.execute();


                //Toast.makeText(getApplicationContext(), "Вы успешно зарегестрированы!", Toast.LENGTH_SHORT).show();
            }
            catch (Exception e){
                e.printStackTrace();
            }
            Intent intent = new Intent(this, LoginPage.class);
            startActivity(intent);
        }


        query.close();
        db.close();
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
