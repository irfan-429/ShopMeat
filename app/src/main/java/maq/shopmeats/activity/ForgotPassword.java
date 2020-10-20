package maq.shopmeats.activity;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import maq.shopmeats.R;

import static maq.shopmeats.activity.MainActivity.checkInternet;
import static maq.shopmeats.activity.MainActivity.showErrorDialog;

public class ForgotPassword extends AppCompatActivity {

    EditText edt_email;
    Button btn_submit;
    String username;
    TextInputLayout inputLayoutEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        edt_email = findViewById(R.id.edit_email);
        inputLayoutEmail = findViewById(R.id.input_layout_email);
        btn_submit = findViewById(R.id.btn_submit);

        edt_email.addTextChangedListener(new MyTextWatcher(edt_email));

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                submitform();
            }
        });
    }

    private void submitform() {
        if (!validateEmail()) {
            return;
        }

        username = edt_email.getText().toString();
        if (checkInternet(this))
            new getForgotPwd().execute();
        else {
            showErrorDialog(this);
        }

    }

    private boolean validateEmail() {
        String email = edt_email.getText().toString().trim();
        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(edt_email);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }
        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    class getForgotPwd extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog = new ProgressDialog(ForgotPassword.this);

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            dialog.setMessage(getString(R.string.please_wait));
            dialog.setCancelable(false);
            dialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            try {
                URL url = new URL(getString(R.string.link) + getString(R.string.servicepath) + "userlogin.php?email=" + username);

                Log.e("URLdetail", "" + url);
                URLConnection hpCon = url.openConnection();
                hpCon.connect();
                InputStream input = hpCon.getInputStream();
                Log.d("input", "" + input);
                BufferedReader r = new BufferedReader(new InputStreamReader(input));
                String x;
                x = r.readLine();
                StringBuilder total = new StringBuilder();
                while (x != null) {
                    total.append(x);
                    x = r.readLine();
                }
                Log.e("URL", "" + total);
                JSONArray jsonArray = new JSONArray(total.toString());
                final JSONObject Obj = jsonArray.getJSONObject(0);
                Log.e("Obj", Obj.toString());

                if (Obj.getString("status").equals("Success")) {
                    JSONObject data = Obj.getJSONObject("user_detail");
//                    user2 = data.getString("id");
//                    email2 = data.getString("email");
//                    phonenumberfull2 = data.getString("phone_no");
//                    username2 = data.getString("fullname");
//                    imageprofile = data.getString("image");
                    Log.e("Obj1", Obj.toString());
                } else if (Obj.getString("status").equals("Failed")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ForgotPassword.this, getString(R.string.servererror), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });

                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                e.getMessage();
            } catch (NullPointerException e) {
                // TODO: handle exception
                e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
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
                case R.id.input_email:
                    validateEmail();
                    break;
            }
        }
    }
}