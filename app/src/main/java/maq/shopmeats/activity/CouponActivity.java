package maq.shopmeats.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import maq.shopmeats.Getset.Coupon;
import maq.shopmeats.R;

public class CouponActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private float total = 0;
    private int quantity;
    ImageButton ib_back;
    String couponCode, couponDiscount;
    double val_totalAmt = 0.0;
    String val_couponDiscount = "0";
    double val_deliveryCharges = 0.0;
    double val_amtToPaid = 0.0;
    private static final String MyPREFERENCES = "Shopmeats";

    EditText field_coupon;
    TextView tv_totalAmt, tv_couponDiscount, tv_deliveryCharges, tv_toBePaid, tvBtm_toBePaid, tv_applyCoupon;
    RelativeLayout layout_checkout;
    List<Coupon> listCoupon = new ArrayList<>();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);

        ib_back = findViewById(R.id.ib_back);
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        field_coupon = findViewById(R.id.edit_coupon);
        tv_applyCoupon = findViewById(R.id.tv_apply_coupon);
        tv_totalAmt = findViewById(R.id.tv_total_amt);
        tv_couponDiscount = findViewById(R.id.tv_coupon_discount);
        tv_deliveryCharges = findViewById(R.id.tv_delivery_changes);
        tv_toBePaid = findViewById(R.id.tv_to_be_paid);
        tvBtm_toBePaid = findViewById(R.id.tv_to_be_paid_btm);
        layout_checkout = findViewById(R.id.rl_checkout);

        tv_applyCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                couponCode = field_coupon.getText().toString().trim();
                if (TextUtils.isEmpty(couponCode)) {
                    field_coupon.requestFocus();
                    field_coupon.setError(getString(R.string.text_must_be_filled));
                    return;
                }


                boolean isCouponFound = false;
                for (Coupon coupon : listCoupon) {
//                    Log.e(TAG, "onClick: "+  coupon.getCode()+" "+couponCode);
                    if (coupon.getCode().equalsIgnoreCase(couponCode)) {
                        if (validateDate(coupon.getFromdate())) {
                            if (val_totalAmt > Double.parseDouble(coupon.getMincartvalue())) {
                                if (coupon.getType().equals("Fixed")) {
                                    val_couponDiscount = getResources().getString(R.string.currency) + coupon.getValue();
                                    val_amtToPaid = val_totalAmt - Double.parseDouble(coupon.getValue()) - val_deliveryCharges;
                                } else {
                                    val_couponDiscount = coupon.getValue() + "%";
                                    val_amtToPaid = val_totalAmt - (val_totalAmt * Double.parseDouble(coupon.getValue()) / 100) - val_deliveryCharges;

                                }
                                couponDiscount = coupon.getValue();
                                setSummary();
                                isCouponFound = true;
                            }
                        }
                        break;
                    }
                }

                if (!isCouponFound) {
                    Toast.makeText(CouponActivity.this, "Coupon not available!", Toast.LENGTH_SHORT).show();
                    couponCode = null;
                }
            }
        });

        layout_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkingSignIn()) {
                    Intent iv = new Intent(CouponActivity.this, PlaceOrder.class);
                    iv.putExtra("order_price", getIntent().getStringExtra("order_price"));
                    iv.putExtra("res_id", getIntent().getStringExtra("res_id"));
                    //some extra info
                    iv.putExtra("delcharge", String.valueOf(val_deliveryCharges));
                    iv.putExtra("couponcode", String.valueOf(couponCode));
                    iv.putExtra("coupondisc", String.valueOf(couponDiscount));
                    iv.putExtra("price", String.valueOf(val_amtToPaid));
                    startActivity(iv);
                } else {
                    Intent iv = new Intent(CouponActivity.this, Login.class);
                    iv.putExtra("key", "PlaceOrder");
                    //some extra info
                    iv.putExtra("delcharge", String.valueOf(val_deliveryCharges));
                    iv.putExtra("couponcode", String.valueOf(couponCode));
                    iv.putExtra("coupondisc", String.valueOf(couponDiscount));
                    iv.putExtra("price", String.valueOf(val_amtToPaid));
                    iv.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(iv);
                    Toast.makeText(CouponActivity.this, R.string.loginmsg, Toast.LENGTH_SHORT).show();
                }

            }
        });

        //init intent data
        val_deliveryCharges = Double.parseDouble(getIntent().getStringExtra("delivery_charge"));
        val_totalAmt = Double.parseDouble(getIntent().getStringExtra("order_price"));
        val_amtToPaid = val_totalAmt - val_deliveryCharges;


        //setting data
        setSummary();
        new fetchCoupons().execute();
    }

    @SuppressLint("SetTextI18n")
    private void setSummary() {
        tv_totalAmt.setText(getResources().getString(R.string.currency) + val_totalAmt);
        tv_couponDiscount.setText(getResources().getString(R.string.currency) + val_couponDiscount);
        tv_deliveryCharges.setText(getResources().getString(R.string.currency) + val_deliveryCharges);
        tv_toBePaid.setText(getResources().getString(R.string.currency) + val_amtToPaid);
        tvBtm_toBePaid.setText(getResources().getString(R.string.currency) + val_amtToPaid);

    }


    private boolean checkingSignIn() {
        //getting shared preference
        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        Log.e("user", "" + prefs.getString("userid", null));
        // check user is created or not
        // if user is already logged in
        if (prefs.getString("userid", null) != null) {
            String userid = prefs.getString("userid", null);
            return !userid.equals("delete");
        } else {
            return false;
        }
    }

    class fetchCoupons extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog = new ProgressDialog(CouponActivity.this);

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
                URL url = new URL(getString(R.string.link) + getString(R.string.servicepath) + "restaurant_coupon.php");

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
                JSONObject jsonObject = new JSONObject(total.toString());
                JSONArray jsonArray = jsonObject.getJSONArray("coupon");
                if (jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String code = object.getString("code");
                        String type = object.getString("type");
                        String value = object.getString("value");
                        String mincartvalue = object.getString("mincartvalue");
                        String userLimit = object.getString("user_limit");
                        String fromdate = object.getString("fromdate");
                        String todate = object.getString("todate");

                        listCoupon.add(new Coupon(code, type, value, mincartvalue, userLimit, fromdate, todate));
                    }
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

    @SuppressLint("SimpleDateFormat")
    public boolean validateDate(String minDate) {
//        String endDate = "21-10-2019";
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd");


        Date currentSysDate = Calendar.getInstance().getTime(); //get current sys date
        String currentDate = dateFormatter.format(currentSysDate); //first format and get String val
//        String selectedFormattedDate = dateFormatter.format(selectedDate); //first format and get String val

        boolean dateFlag = false;

        try {
            if (dateFormatter.parse(currentDate).before(dateFormatter.parse(minDate)))
                dateFlag = false;  // If selected date is before current date.
            else if (dateFormatter.parse(currentDate).equals(dateFormatter.parse(minDate)))
                dateFlag = true;  // If two dates are equal.
            else dateFlag = false; // If selected date is after the current date.

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateFlag;
    }

}