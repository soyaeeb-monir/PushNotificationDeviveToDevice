 package com.monir.pushnotificationdevivetodevice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceIdReceiver;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.installations.InstallationTokenResult;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Objects;

 public class MainActivity extends AppCompatActivity {
    private EditText editTextTitle;
    private EditText editTextMessage;
    private EditText editTextToken;
    private Button buttonSendAllUser;
    private Button buttonSendOneUser;

   private String title;
   private String message;
   private String UserToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextTitle = findViewById(R.id.edit_text_title);
        editTextMessage = findViewById(R.id.edit_text_message);
        editTextToken = findViewById(R.id.edit_text_token);

        buttonSendAllUser = findViewById(R.id.button_send_all_user);
        buttonSendOneUser = findViewById(R.id.button_send_one_user);


        // for sending notification to all

        FirebaseMessaging.getInstance().subscribeToTopic("all");


        // For sending message to a specific user
//
//        FirebaseInstallations.getInstance().getToken(true)
//                .addOnCompleteListener(new OnCompleteListener<InstallationTokenResult>() {
//            @Override
//            public void onComplete(@NonNull Task<InstallationTokenResult> task) {
//                 if(task.isSuccessful()){
//                     // Store / update this token to database while sign in every time
//                     UserToken = Objects.requireNonNull(task.getResult().getToken());
//                     editTextToken.setText(UserToken);
//                 }else{
//                     Toast.makeText(MainActivity.this, "Something went Wrong", Toast.LENGTH_SHORT).show();
//                 }
//            }
//        });



        // Alternative way to send notification to a specific user

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(task.isSuccessful()){
                    // Store / update this token to database while sign in every time
                    String token = Objects.requireNonNull(task.getResult().toString());
                    editTextToken.setText(token);
                }
            }
        });

        
//        // fcm settings for particular user
//
//        FirebaseInstanceId.getInstance().getInstanceId()
//                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
//                        if (task.isSuccessful()) {
//                            String  token = Objects.requireNonNull(task.getResult()).getToken();
//
//                        }
//
//                    }
//                });
        buttonSendAllUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = editTextTitle.getText().toString();
                message = editTextMessage.getText().toString();

                if(!TextUtils.isEmpty(editTextTitle.getText().toString()) &&
                !TextUtils.isEmpty(editTextMessage.getText().toString())){
                    FcmNotificationsSender notificationsSender = new FcmNotificationsSender(
                            "/topics/all",
                            title,
                            message,
                            getApplicationContext(),
                            MainActivity.this );
                    notificationsSender.SendNotifications();
                }else{
                    Toast.makeText(MainActivity.this, "Enter Title and Message", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonSendOneUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = editTextTitle.getText().toString();
                message = editTextMessage.getText().toString();
                UserToken = editTextToken.getText().toString();
                if(!TextUtils.isEmpty(title) &&
                        !TextUtils.isEmpty(message) &&
                        !TextUtils.isEmpty(UserToken)){
                    FcmNotificationsSender notificationsSender = new FcmNotificationsSender(
                            UserToken,
                            title,
                            message,
                            getApplicationContext(),
                            MainActivity.this );
                    notificationsSender.SendNotifications();
                }else{
                    Toast.makeText(MainActivity.this, "Enter Title and Message", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}