package com.example.larkmessage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.larkmessage.entity.UserItem;
import com.example.larkmessage.unit.loginUnit;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;

public class WelcomeActivity extends AppCompatActivity {

    private EditText welEMailEditView;
    private EditText welPasswordEditView;
    private Button welRegisterButton;
    private Button welSignInButton;
    private String email;
    private String password;
    private Boolean mem=true;
    private CheckBox memCheck;
    private final static String PREFS = "PREFS";
    private static  final  String KEY_PASSWORD="key_password";
    private static  final  String KEY_MEMORY="key_memory";
    private  static  final  String KEY_EMAIL ="key_email";
    private SharedPreferences  preferences;
    private loginUnit loginunit =new loginUnit();
    private TextView forgetTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.hide();
        }
        welEMailEditView =findViewById(R.id.welcome_email_editText);
        welPasswordEditView =findViewById(R.id.welcome_password_editText);
        welRegisterButton =findViewById(R.id.welcome_register_button);
        welSignInButton = findViewById(R.id.welcome_login_button);
        memCheck =findViewById(R.id.mem_CheckBox);
        if(getSharedPreferences(PREFS, MODE_PRIVATE)!=null) {

            SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
            String email = prefs.getString(KEY_EMAIL,null);
            String password = prefs.getString(KEY_PASSWORD,null);
            mem =prefs.getBoolean(KEY_MEMORY,true);
            if(email!=null)welEMailEditView.setText(email);
            if(password!=null)welPasswordEditView.setText(password);
        }
        email=welEMailEditView.getText().toString();
        password=welPasswordEditView.getText().toString();

        welSignInButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        email=welEMailEditView.getText().toString();
                        password=welPasswordEditView.getText().toString();
                       loginunit.signInWithEmailAndPassword(email,password,WelcomeActivity.this);

                    }
                }
        );
        welRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this,RegisterActivity.class);
                startActivity(intent);

            }
        });
        memCheck.setChecked(mem);

        memCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true)
                {
                    mem=true;
                }
                else
                {
                    mem=false;
                }
            }
        });
      /*  if(email.length()>0&&password.length()>0)
        {
            loginunit.signInWithEmailAndPassword(email,password,WelcomeActivity.this);
        }*/
      forgetTextView=findViewById(R.id.welcome_forget_passwords);
      forgetTextView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              forgetDialog();
          }
      });
    }

    protected void forgetDialog()
    {
        final EditText emailEdit = new EditText(WelcomeActivity.this);
        final EditText phoneEdit = new EditText(WelcomeActivity.this);
        TextView emailText =new TextView(WelcomeActivity.this);
        emailText.setText("Email:");
        TextView phoneText = new TextView(WelcomeActivity.this);
        phoneText.setText("Phone Number:");
        LinearLayout linearLayout = new LinearLayout(WelcomeActivity.this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(emailText);
        linearLayout.addView(emailEdit);
        linearLayout.addView(phoneText);
        linearLayout.addView(phoneEdit);
        final AlertDialog.Builder alterDiaglog = new AlertDialog.Builder(WelcomeActivity.this);
        alterDiaglog.setIcon(R.mipmap.ic_launcher);
        alterDiaglog.setTitle("Forget password");
        alterDiaglog.setMessage("Please input your email and phone Number:");
        alterDiaglog.setView(linearLayout);
        alterDiaglog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alterDiaglog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                searchByEmail(emailEdit.getText().toString(),phoneEdit.getText().toString());
            }
        });
        alterDiaglog.show();
    }
    protected void searchByEmail(final String fEmail, final String fPhone)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("UserList").document(fEmail)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()) {
                            UserItem userItem = documentSnapshot.toObject(UserItem.class);
                            if (userItem.getPhoneNumber().equals(fPhone)) {
                                String result = "your password :" + userItem.getPassword();
                                welEMailEditView.setText(userItem.getEmail());
                                welPasswordEditView.setText(userItem.getPassword());
                                passwordDialog(result);
                            } else {
                                String result = "incorrect phone number";
                                passwordDialog(result);
                            }
                        }
                        else
                        {
                            String result = "incorrect email";
                            passwordDialog(result);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String result = "no internet!";
                        passwordDialog(result);
                    }
                });
    }
    protected void passwordDialog(String result)
    {

        final AlertDialog.Builder alterDiaglog = new AlertDialog.Builder(WelcomeActivity.this);
        alterDiaglog.setIcon(R.mipmap.ic_launcher);
        alterDiaglog.setTitle("Result:");
        alterDiaglog.setMessage(result);
        alterDiaglog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alterDiaglog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alterDiaglog.show();
    }
    @Override
    protected void onPause() {
        super.onPause();
        if(mem==true) {
            email = welEMailEditView.getText().toString();
            password = welPasswordEditView.getText().toString();
        }
        else
        {
            email="";
            password="";
        }
        preferences = getSharedPreferences(PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(KEY_MEMORY,mem);
        editor.putString(KEY_EMAIL,email);
        editor.putString(KEY_PASSWORD,password);
        editor.commit();
    }
}
