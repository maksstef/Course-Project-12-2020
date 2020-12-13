package com.example.courseproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;


import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public final class MailForNewUser extends AsyncTask<Void,Void,Void> {
    private Context context;
    private Session session;

    private String toUser;
    private String subject;
    private String text;

    private static final String from = "android.test.2020@mail.ru";
    private static final String fromPass = "androidtest";

    public MailForNewUser(Context context, String toUser, String subject, String text) {
        this.context = context;
        this.toUser = toUser;
        this.subject = subject;
        this.text = text;
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    @Override
    protected Void doInBackground(Void... params) {
        Properties props = new Properties();

        //Configuring properties for mail.ru
        props.put("mail.smtp.host", "smtp.mail.ru");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        //Creating a new session
        session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    //Authenticating the password
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(from, fromPass);
                    }
                });
        try {
            MimeMessage mm = new MimeMessage(session);
            mm.setFrom(new InternetAddress(from));
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(toUser));
            mm.setSubject(subject);
            mm.setText(text);

            Transport.send(mm);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
