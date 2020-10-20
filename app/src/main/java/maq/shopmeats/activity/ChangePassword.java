package maq.shopmeats.activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import maq.shopmeats.R;

import static maq.shopmeats.activity.MainActivity.checkInternet;
import static maq.shopmeats.activity.MainActivity.showErrorDialog;

public class ChangePassword extends AppCompatActivity {

    private static final String MY_PREFS_NAME = "Shopmeats";
    EditText field_old_pwd, field_new_pwd, field_cNew_pwd;
    Button btn_submit;
    TextInputLayout inputLayoutOldPwd, inputLayoutNewPwd, inputLayoutCNewPwd;

    String oldPwd, newPwd, cNewPwd;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);


        field_old_pwd = findViewById(R.id.edit_old_pwd);
        field_new_pwd = findViewById(R.id.edit_new_pwd);
        field_cNew_pwd = findViewById(R.id.edit_cnew_pwd);

        inputLayoutOldPwd = findViewById(R.id.input_layout_old_password);
        inputLayoutNewPwd = findViewById(R.id.input_layout_new_password);
        inputLayoutCNewPwd = findViewById(R.id.input_layout_cnew_password);

        btn_submit = findViewById(R.id.btn_submit);

        field_old_pwd.addTextChangedListener(new MyTextWatcher(field_old_pwd));
        field_new_pwd.addTextChangedListener(new MyTextWatcher(field_new_pwd));
        field_cNew_pwd.addTextChangedListener(new MyTextWatcher(field_cNew_pwd));

        //back press
        ImageButton ib_back = findViewById(R.id.ib_back);
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                submitform();
            }
        });

         prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

    }

    private void submitform() {
        if (!validateOldPwd()) {
            return;
        }

        if (!validateNewPwd()) {
            return;
        }

        if (!validateCNewPwd()) {
            return;
        }


        if (checkInternet(this))
            changePasswordRqst();
        else {
            showErrorDialog(this);
        }

    }

    private boolean validateOldPwd() {
        oldPwd = field_old_pwd.getText().toString().trim();
        if (oldPwd.isEmpty()) {
            inputLayoutOldPwd.setError(getString(R.string.err_msg_password));
            requestFocus(field_old_pwd);
            return false;
        }else if (!oldPwd.equals( prefs.getString("userpassword", ""))) {
            inputLayoutOldPwd.setError(getString(R.string.err_msg_password_incorrect));
            requestFocus(field_old_pwd);
            return false;
        } else {
            inputLayoutOldPwd.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateNewPwd() {
        newPwd = field_new_pwd.getText().toString().trim();
        if (newPwd.isEmpty()) {
            inputLayoutNewPwd.setError(getString(R.string.err_msg_password));
            requestFocus(field_new_pwd);
            return false;
        } else {
            inputLayoutNewPwd.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateCNewPwd() {
        cNewPwd = field_cNew_pwd.getText().toString().trim();
        if (cNewPwd.isEmpty()) {
            inputLayoutCNewPwd.setError(getString(R.string.err_msg_password));
            requestFocus(field_cNew_pwd);
            return false;
        }else if (!cNewPwd.equals(newPwd)) {
            inputLayoutCNewPwd.setError(getString(R.string.err_msg_password_not_matched));
            requestFocus(field_cNew_pwd);
            return false;
        } else {
            inputLayoutCNewPwd.setErrorEnabled(false);
        }
        return true;
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private final View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.edit_old_pwd:
                    validateOldPwd();
                    break;

                case R.id.edit_new_pwd:
                    validateNewPwd();
                    break;

                case R.id.edit_cnew_pwd:
                    validateCNewPwd();
                    break;
            }
        }
    }

    private void changePasswordRqst() {
        final ProgressDialog dialog = new ProgressDialog(ChangePassword.this);
        dialog.setMessage(getString(R.string.please_wait));
        dialog.setCancelable(false);
        dialog.show();

        //creating a string request to send request to the url
        String hp = getString(R.string.link) + getString(R.string.servicepath) + "changepassword.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, hp,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //hiding the progressbar after completion
                        dialog.dismiss();

                        Log.e("Response123", response);
                        //    {"data":{"success":"1"}}

                        try {
                            JSONObject jo_main = new JSONObject(response);
                            JSONObject jo_data = jo_main.getJSONObject("data");
                            if (jo_data.getString("success").equals("1")) {
                                Toast.makeText(ChangePassword.this, getString(R.string.pwd_change_success), Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(ChangePassword.this, getString(R.string.unable_to_change), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurs
                        dialog.dismiss();
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null) {
                            Log.e("Status code", String.valueOf(networkResponse.statusCode));
                            Toast.makeText(getApplicationContext(), String.valueOf(networkResponse.statusCode), Toast.LENGTH_SHORT).show();
                        }
                    }
                }) {
            protected Map<String, String> getParams() {

                Map<String, String> MyData = new HashMap<>();
                MyData.put("emailid", prefs.getString("usermailid", null));
                MyData.put("newpassword",newPwd);
                return MyData;
            }
        };
        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //adding the string request to request queue
        requestQueue.add(stringRequest);
    }
}