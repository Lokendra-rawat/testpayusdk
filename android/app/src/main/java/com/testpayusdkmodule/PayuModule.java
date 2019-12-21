package com.testpayusdkmodule;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

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
    private static final int PAYUMONEY_RESULT = 0;

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
         mPaymentParams.setHash(hash);

         ReactApplicationContext context = getReactApplicationContext();
         Activity currentActivity = getCurrentActivity();
         Intent intent = new Intent(currentActivity, PayUBaseActivity.class);

         PayuConfig payuConfig = new PayuConfig();
         payuConfig.setEnvironment(PayuConstants.STAGING_ENV);

         intent.putExtra(PayuConstants.ENV, PayuConstants.STAGING_ENV);
         intent.putExtra(PayuConstants.PAYMENT_PARAMS, mPaymentParams);
         intent.putExtra(PayuConstants.SALT, "eCwWELxi");
         intent.putExtra(PayuConstants.PAYU_HASHES, hash);
         currentActivity.startActivity(intent);
     }

     public void onNewIntent(Intent intent) {
     }

     public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
         onActivityResult(requestCode, resultCode, data);
     }

     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
         if (requestCode == PAYUMONEY_RESULT) {
             Boolean success = data.getBooleanExtra("success", false);
             if (success) {
                 String payuResponse = data.getStringExtra("payuResponse");
                 sendEvent("PAYU_PAYMENT_SUCCESS", payuResponse);
             } else {
                 sendEvent("PAYU_PAYMENT_FAILED", "{error:true}");
             }
         }
     }

     private void sendEvent(String eventName, String params) {
         ReactApplicationContext context = getReactApplicationContext();
         context.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                 .emit(eventName, params);
     }
}