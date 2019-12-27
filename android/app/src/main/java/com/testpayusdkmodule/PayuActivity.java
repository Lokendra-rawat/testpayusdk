package com.testpayusdkmodule;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.payu.india.Extras.PayUChecksum;
import com.payu.india.Model.PaymentParams;
import com.payu.india.Model.PayuConfig;
import com.payu.india.Model.PayuHashes;
import com.payu.india.Model.PostData;
import com.payu.india.Payu.Payu;
import com.payu.india.Payu.PayuConstants;
import com.payu.india.Payu.PayuErrors;
import com.payu.payuui.Activity.PayUBaseActivity;

public class PayuActivity extends AppCompatActivity {
    private static final int PAYU_RESULT = 0;

    // Used when generating hash from SDK
    private PayUChecksum checksum;

    String salt = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        Bundle extras = getIntent().getExtras();
        //TODO Must write below code in your activity to set up initial context for PayU
        Payu.setInstance(this);
        String key = extras.getString("key");
        String txnId = extras.getString("txnId");
        String amount = extras.getString("amount");
        String firstName = extras.getString("firstName");
        String email = extras.getString("email");
        String phone = extras.getString("phone");
        String productInfo = extras.getString("productInfo");
        String merchantSUrl = extras.getString("merchantSUrl");
        String merchantFUrl = extras.getString("merchantFUrl");
        String hash = extras.getString("hash");
        makePayment(key, txnId, amount, firstName, email, phone, productInfo, merchantSUrl, merchantFUrl, hash);
    }

    private void makePayment(final String key,
                             final String txnId,
                             final String amount,
                             final String firstName,
                             final String email,
                             final String phone,
                             final String productInfo,
                             final String merchantSUrl,
                             final String merchantFUrl,
                             final String hash) {

        PayuHashes payuHashes = new PayuHashes();
        payuHashes.setPaymentHash(hash);
        payuHashes.setVasForMobileSdkHash(hash);
        payuHashes.setPaymentRelatedDetailsForMobileSdkHash(hash);

        PaymentParams mPaymentParams = new PaymentParams();

        mPaymentParams.setKey(key);
        mPaymentParams.setAmount(amount);
        mPaymentParams.setPhone(phone);
        mPaymentParams.setProductInfo(productInfo);
        mPaymentParams.setFirstName(firstName);
        mPaymentParams.setEmail(email);
        mPaymentParams.setTxnId(txnId);
        mPaymentParams.setSurl(merchantSUrl);
        mPaymentParams.setFurl(merchantFUrl);
        mPaymentParams.setHash(payuHashes.getPaymentHash());

        System.out.println("getting payment hash from pay u hash " + payuHashes.getPaymentHash());

        Activity currentActivity = PayuActivity.this;
        Intent intent = new Intent(currentActivity, PayUBaseActivity.class);

        PayuConfig payuConfig = new PayuConfig();
        payuConfig.setEnvironment(PayuConstants.PRODUCTION_ENV);

        intent.putExtra(PayuConstants.PAYU_CONFIG, payuConfig);
//        intent.putExtra(PayuConstants.ENV, PayuConstants.PRODUCTION_ENV);
        intent.putExtra(PayuConstants.PAYMENT_PARAMS, mPaymentParams);
        intent.putExtra(PayuConstants.SALT, "eCwWELxi"); //TODO try commenting it
        intent.putExtra(PayuConstants.PAYU_HASHES, payuHashes);
        this.startActivityForResult(intent, PayuConstants.PAYU_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent intent = new Intent();
        if(requestCode == PayuConstants.PAYU_REQUEST_CODE) {
            if(data != null) {
                String payuResponse = data.getStringExtra("payu_response");
                intent.putExtra("success", true);
                intent.putExtra("payuResponse", payuResponse);
            } else {
                intent.putExtra("success", false);
            }
        } else {
            intent.putExtra("success", false);
        }

//        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data != null) {
//            TransactionResponse transactionResponse = data.getParcelableExtra(PayUmoneyFlowManager.INTENT_EXTRA_TRANSACTION_RESPONSE);
//            ResultModel resultModel = data.getParcelableExtra(PayUmoneyFlowManager.ARG_RESULT);
//            if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {
//                if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.SUCCESSFUL)) {
//                    intent.putExtra("success", true);
//                } else {
//                    intent.putExtra("success", false);
//                }
//
//                String payuResponse = transactionResponse.getPayuResponse();
//                intent.putExtra("payuResponse", payuResponse);
//
//            } else if (resultModel != null && resultModel.getError() != null) {
//                intent.putExtra("success", false);
//            } else {
//                intent.putExtra("success", false);
//            }
//        }
//        intent.putExtra("payuResponse", payuResponse);

        setResult(RESULT_OK, intent);
        finish();
    }
}
