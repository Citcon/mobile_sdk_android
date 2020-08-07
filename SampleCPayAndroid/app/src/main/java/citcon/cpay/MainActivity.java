package citcon.cpay;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import citcon.cpay.R;
import sdk.CPayMode;
import sdk.CPaySDK;
import sdk.interfaces.InquireResponse;
import sdk.interfaces.OrderResponse;
import sdk.models.CPayInquireResult;
import sdk.models.CPayOrder;
import sdk.models.CPayOrderResult;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button buttonRequestOrder;
    private Button buttonInquireOrder;
    private EditText mReferenceIdEditText, mSubjectEditText, mBodyEditText, mAmountEditText,
            mCurrencyEditText, mVendorEditText, mIpnEditText, mCallbackEditText, mTransCurrency;
    private Switch mSwitch;
    private TextView mInquireResultTextView, mOrderResultTextView;
    private ScrollView mScrollView;

    private CPayOrderResult mPayOrderResult;
    private BroadcastReceiver mInquireReceiver;

    private static CPayMode ENV = CPayMode.UAT;

    private String REF_ID;
    private String AUTH_TOKEN;
    private String CALLBACK_URL;

    boolean testUSD = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonRequestOrder = findViewById(R.id.request_button);
        buttonInquireOrder = findViewById(R.id.inquire_button);

        buttonRequestOrder.setOnClickListener(this);
        buttonInquireOrder.setOnClickListener(this);

        String CURRENCY;

        if (testUSD) {
            REF_ID = "pay-mobile-test";
            AUTH_TOKEN = "9FBBA96E77D747659901CCBF787CDCF1";
            CALLBACK_URL = "https://uat.citconpay.com/payment/notify_wechatpay.php";
            CURRENCY = "USD";
        } else {
            REF_ID = "CNY-mobile-test";
            AUTH_TOKEN = "CNYAPPF6A0FE479A891BF45706A690AE";
            CALLBACK_URL = "http://52.87.248.227/ipn.php";
            CURRENCY = "CNY";
        }

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);

        mReferenceIdEditText = findViewById(R.id.reference_id_editText);
        mSubjectEditText = findViewById(R.id.subject_editText);
        mBodyEditText = findViewById(R.id.body_editText);
        mAmountEditText = findViewById(R.id.amount_editText);
        mCurrencyEditText = findViewById(R.id.currency_editText);
        mVendorEditText = findViewById(R.id.vendor_editText);
        mIpnEditText = findViewById(R.id.ipn_editText);
        mCallbackEditText = findViewById(R.id.callback_editText);
        mSwitch = findViewById(R.id.duplicate_switch);
        mInquireResultTextView = findViewById(R.id.inquire_result_textView);
        mOrderResultTextView = findViewById(R.id.order_result_textView);
        mScrollView = findViewById(R.id.scrollView);
        mTransCurrency = findViewById(R.id.trans_currency_editText);

        mReferenceIdEditText.setText(REF_ID); //Citcon Referance ID
        mSubjectEditText.setText("Test"); // order subject
        mBodyEditText.setText("Test data"); // order body
        mAmountEditText.setText("1"); // amount
        mCurrencyEditText.setText(CURRENCY); // currency USD
        mVendorEditText.setText("wechatpay"); // payment vendor wechatpay or alipay
        mIpnEditText.setText(CALLBACK_URL); //citcon payment callback url
        mCallbackEditText.setText("http://www.google.com"); // custom callback url to customization processing

        /**
         *
         * <p>Get payment success broadcasting CPayInquireResult
         *
         */
        mInquireReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                CPayInquireResult response = (CPayInquireResult) intent.getSerializableExtra("inquire_result");
                // Add your code here
            }
        };
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.request_button) {
            if (mCurrencyEditText.getText().toString().equals("USD")) {
                AUTH_TOKEN = "9FBBA96E77D747659901CCBF787CDCF1";
            } else {
                AUTH_TOKEN = "CNYAPPF6A0FE479A891BF45706A690AE";
            }
            CPaySDK.getInstance().mToken = AUTH_TOKEN;
            CPaySDK.setMode(CPayMode.UAT);

            CPayOrder order = new CPayOrder(mReferenceIdEditText.getText().toString(),
                    mSubjectEditText.getText().toString(),
                    mBodyEditText.getText().toString(),
                    mAmountEditText.getText().toString(),
                    mCurrencyEditText.getText().toString(),
                    mVendorEditText.getText().toString(),
                    mIpnEditText.getText().toString(),
                    mCallbackEditText.getText().toString(),
                    mSwitch.isChecked(),
                    mTransCurrency.getText().toString());

            CPaySDK.getInstance().requestOrder(order, new OrderResponse<CPayOrderResult>() {
                @Override
                public void gotOrderResult(final CPayOrderResult orderResult) {
                    mOrderResultTextView.setText("On Got OrderResult");
                    if (orderResult != null) {
                        mPayOrderResult = orderResult;
                        String orderResultStr = "ORDER RESULT:";
                        if (orderResult.orderId != null) {
                            orderResultStr += "ORDER ID: " + orderResult.orderId + "\n";
                        }
                        if (orderResult.orderSpec != null) {
                            orderResultStr += "ORDER SPEC: " + orderResult.orderSpec + "\n";
                        }
                        if (orderResult.status != null) {
                            orderResultStr += "STATUS: " + orderResult.status + "\n";
                        }
                        if (orderResult.message != null) {
                            orderResultStr += "MESSAGE: " + orderResult.message + "\n";
                        }
                        if (orderResult.currency != null) {
                            orderResultStr += "CURRENCY: " + orderResult.currency + "\n";
                        }

                        mOrderResultTextView.setText(orderResultStr);

                        mScrollView.post(new Runnable() {
                            @Override
                            public void run() {
                                mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
                            }
                        });
                    } else {
                        mOrderResultTextView.setText("On Got OrderResult null");
                    }
                }
            });
        } else if (v.getId() == R.id.inquire_button) {
            if (mPayOrderResult == null) {
                return;
            }

            if (mCurrencyEditText.getText().toString().equals("USD")) {
                AUTH_TOKEN = "9FBBA96E77D747659901CCBF787CDCF1";
            } else {
                AUTH_TOKEN = "CNYAPPF6A0FE479A891BF45706A690AE";
            }
            CPaySDK.getInstance().mToken = AUTH_TOKEN;
            CPaySDK.setMode(CPayMode.UAT);

            CPaySDK.getInstance().inquireOrder(mPayOrderResult, new InquireResponse<CPayInquireResult>() {
                @Override
                public void gotInquireResult(CPayInquireResult cPayInquireResult) {
                    if (cPayInquireResult != null) {
                        String inquireResultStr = "ORDER RESULT:";
                        if (cPayInquireResult.transactionId != null) {
                            inquireResultStr += "ORDER ID: " + cPayInquireResult.transactionId + "\n";
                        }
                        if (cPayInquireResult.type != null) {
                            inquireResultStr += "TYPE: " + cPayInquireResult.type + "\n";
                        }
                        if (cPayInquireResult.amount != null) {
                            inquireResultStr += "AMOUNT: " + cPayInquireResult.amount + "\n";
                        }
                        if (cPayInquireResult.time != null) {
                            inquireResultStr += "TIME: " + cPayInquireResult.time + "\n";
                        }
                        if (cPayInquireResult.referenceId != null) {
                            inquireResultStr += "REFERENCE: " + cPayInquireResult.referenceId + "\n";
                        }
                        if (cPayInquireResult.status != null) {
                            inquireResultStr += "STATUS: " + cPayInquireResult.status + "\n";
                        }
                        if (cPayInquireResult.currency != null) {
                            inquireResultStr += "CURRENCY: " + cPayInquireResult.currency + "\n";
                        }
                        if (cPayInquireResult.note != null) {
                            inquireResultStr += "NOTE: " + cPayInquireResult.note + "\n";
                        }

                        mInquireResultTextView.setText(inquireResultStr);

                        mScrollView.post(new Runnable() {
                            @Override
                            public void run() {
                                mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
                            }
                        });
                    }
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        CPaySDK.getInstance(MainActivity.this, AUTH_TOKEN).onResume();

        if (testUSD) {
            CPaySDK.setMode(CPayMode.DEV);
            // You should use UAT mode in production environment
            // CPaySDK.setMode(CPayMode.UAT);
        } else {
            CPaySDK.setMode(CPayMode.PROD);
        }
        // Register broadcast
        registerInquireReceiver();
    }

    /**
     * <p>onPause to unregister BroadcastReceiver.
     */

    @Override
    public void onPause() {
        super.onPause();

        unregisterInquireReceiver();
    }

    /**
     * Register BroadcastReceiver.
     */
    private void registerInquireReceiver() {
        if (mInquireReceiver != null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction("CPAY_INQUIRE_ORDER");
            CPaySDK.getInstance().registerReceiver(mInquireReceiver, filter);
        }
    }

    /**
     * <p>unregister BroadcastReceiver.
     */
    private void unregisterInquireReceiver() {
        if (mInquireReceiver != null)
            CPaySDK.getInstance().unregisterReceiver(mInquireReceiver);
    }
}
