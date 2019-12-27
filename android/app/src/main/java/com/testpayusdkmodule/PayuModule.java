package com.testpayusdkmodule;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import com.facebook.common.internal.Objects;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import com.payu.india.Model.PaymentParams;
import com.payu.india.Extras.PayUChecksum;
import com.payu.india.Extras.PayUSdkDetails;
import com.payu.india.Model.PaymentParams;
import com.payu.india.Model.PayuConfig;
import com.payu.india.Model.PayuHashes;
import com.payu.india.Model.PostData;
import com.payu.india.Payu.Payu;
import com.payu.india.Payu.PayuConstants;
import com.payu.india.Payu.PayuErrors;
import com.payu.payuui.Activity.PayUBaseActivity;

public class PayuModule extends ReactContextBaseJavaModule implements ActivityEventListener {
    private static final int PAYU_RESULT = 0;

    public PayuModule(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
        reactApplicationContext.addActivityEventListener(this);
    }

    @Override
    public String getName() {
        return "payu";
    }

    @ReactMethod
    public void sampleMethod(String stringArgument, int numberArgument, Callback callback) {
        // TODO: Implement some actually useful functionality
        callback.invoke("Received numberArgument: " + numberArgument + " stringArgument: " + stringArgument);
    }

    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        onActivityResult(requestCode, resultCode, data);
    }

    private void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("in activity result --------------" + requestCode + PayuConstants.PAYU_REQUEST_CODE);
        if (requestCode == PayuConstants.PAYU_REQUEST_CODE) {
            Boolean success = data.getBooleanExtra("success", false);
            if (success) {
                String payuResponse = data.getStringExtra("payuResponse");
                sendEvent("PAYU_PAYMENT_SUCCESS", payuResponse);
            } else {
                sendEvent("PAYU_PAYMENT_FAILED", "{error:true}");
            }
//            if (data != null) {
//                String payuResponse = data.getStringExtra("payu_response");
//                sendEvent("PAYU_PAYMENT_SUCCESS", payuResponse);
//            } else {
//                System.out.println("in second else ----------------------------");
//                sendEvent("PAYU_PAYMENT_FAILED", "{error:true}");
//            }
        } else {
           sendEvent("PAYU_PAYMENT_FAILED", "{error:true}");
        }
    }

    @ReactMethod
    public void makePayment(final String key,
                            final String txnId,
                            final String amount,
                            final String firstName,
                            final String email,
                            final String phone,
                            final String productInfo,
                            final String merchantSUrl,
                            final String merchantFUrl,
                            final String hash) {

        System.out.println("Payment amount is " + amount);

//         PayuHashes payuHashes = new PayuHashes();
//         payuHashes.setPaymentHash(hash);
//         payuHashes.setVasForMobileSdkHash(hash);
//         payuHashes.setPaymentRelatedDetailsForMobileSdkHash(hash);
//         payuHashes.setMerchantIbiboCodesHash(hash);
//
//         PaymentParams mPaymentParams = new PaymentParams();
//         mPaymentParams.setKey(key);
//         mPaymentParams.setAmount(amount);
//         mPaymentParams.setPhone(phone);
//         mPaymentParams.setProductInfo(productInfo);
//         mPaymentParams.setFirstName(firstName);
//         mPaymentParams.setEmail(email);
//         mPaymentParams.setTxnId(txnId);
//         mPaymentParams.setSurl(merchantSUrl);
//         mPaymentParams.setFurl(merchantFUrl);
//         mPaymentParams.setHash(hash);

//         ReactApplicationContext context = getReactApplicationContext();
        Activity currentActivity = getCurrentActivity();
        Intent intent = new Intent(currentActivity, PayuActivity.class);

        intent.putExtra("key", key);
        intent.putExtra("txnId", txnId);
        intent.putExtra("amount", amount);
        intent.putExtra("firstName", firstName);
        intent.putExtra("email", email);
        intent.putExtra("phone", phone);
        intent.putExtra("productInfo", productInfo);
        intent.putExtra("merchantSUrl", merchantSUrl);
        intent.putExtra("merchantFUrl", merchantFUrl);
        intent.putExtra("hash", hash);

//         PayuConfig payuConfig = new PayuConfig();
//         payuConfig.setEnvironment(PayuConstants.STAGING_ENV);

//         intent.putExtra(PayuConstants.PAYU_CONFIG, payuConfig);
//         intent.putExtra(PayuConstants.ENV, PayuConstants.STAGING_ENV);
//         intent.putExtra(PayuConstants.PAYMENT_PARAMS, mPaymentParams);
//         intent.putExtra(PayuConstants.SALT, "QYcSzlbk"); //TODO try removing it
//         intent.putExtra(PayuConstants.PAYU_HASHES, payuHashes);
//         assert currentActivity != null;
        currentActivity.startActivityForResult(intent, PAYU_RESULT);
    }

    public void onNewIntent(Intent intent) {
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
//    }
//
//     public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
//         onActivityResult(requestCode, resultCode, data);
//     }
//
//     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        System.out.println("PayuModule on activity result " + requestCode + resultCode);
//         if (requestCode == PAYUMONEY_RESULT) {
//             Boolean success = data.getBooleanExtra("success", false);
//             if (success) {
//                 String payuResponse = data.getStringExtra("payuResponse");
//                 sendEvent("PAYU_PAYMENT_SUCCESS", payuResponse);
//             } else {
//                 sendEvent("PAYU_PAYMENT_FAILED", "{error:true}");
//             }
//         }
//     }

     private void sendEvent(String eventName, String params) {
         ReactApplicationContext context = getReactApplicationContext();
         context.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                 .emit(eventName, params);
     }
}