package maq.shopmeats.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import maq.shopmeats.R;

import static maq.shopmeats.activity.MainActivity.changeStatsBarColor;
import static maq.shopmeats.activity.MainActivity.tf_opensense_regular;

public class Profile extends AppCompatActivity {
    private static final String MY_PREFS_NAME = "Shopmeats";


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView( R.layout.activity_profile);

        changeStatsBarColor(Profile.this);


        initView();


    }

    private void initView() {
        Button change_pwd = findViewById(R.id.tv_change_pwd);
        EditText input_name = findViewById(R.id.input_name);
        EditText input_email = findViewById(R.id.input_email);
        ImageView img_user = findViewById(R.id.img_user);
        ImageView img = findViewById(R.id.img12);
        ((TextView) findViewById(R.id.txt_title)).setTypeface(tf_opensense_regular);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String uservalue = prefs.getString("userid", null);
        String image = prefs.getString("imagepath", null);
        String profileimage = prefs.getString("imageprofile", null);
        Log.d("profileimg", "" + getString(R.string.link) + getString(R.string.imagepath) + profileimage);

        String mail_id = prefs.getString("usermailid", null);
        String uname = prefs.getString("username", null);
        Log.d("profileimg", "" + uname);
        input_name.setText(uname);
        input_email.setText(mail_id);
        try {
            Picasso.with( getApplicationContext() )
                    .load(profileimage)
                    .into(img_user);
            Picasso.with(getApplicationContext())
                    .load(profileimage)
                    .into(img);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();

        }
        ImageButton ib_back = findViewById(R.id.ib_back);
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        change_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Profile.this, ChangePassword.class));
            }
        });

    }

}
