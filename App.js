/* eslint-disable quotes */
/* eslint-disable semi */
/* eslint-disable no-unused-vars */
/* eslint-disable react-native/no-inline-styles */
/* eslint-disable prettier/prettier */
/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow
 */

import React from 'react';
import {
  View,
  StatusBar,
  NativeModules,
  Button,
  NativeEventEmitter,
} from 'react-native';

const PayuPay = NativeModules.payu;
const PayuEvent = new NativeEventEmitter(PayuPay);

const removeSubscriptions = () => {
  PayuEvent.removeAllListeners('PAYU_PAYMENT_SUCCESS');
  PayuEvent.removeAllListeners('PAYU_PAYMENT_FAILED');
};

class PayuMoney {
  static pay(options) {
    let data = {
      key: options.key,
      txnId: options.txnId,
      amount: options.amount,
      firstName: options.firstName,
      email: options.email,
      phone: options.phone,
      productInfo: options.productInfo,
      merchantSUrl: options.merchantSUrl,
      merchantFUrl: options.merchantFUrl,
      hash: options.hash,
    };
    return new Promise(function (resolve, reject) {
      PayuEvent.addListener('PAYU_PAYMENT_SUCCESS', (response) => {
        resolve(response);
        removeSubscriptions();
      });
      PayuEvent.addListener('PAYU_PAYMENT_FAILED', (response) => {

        console.warn('err res from native side', response);

        reject({ success: false });
        removeSubscriptions();
      });
      PayuPay.makePayment(data.key, data.txnId, data.amount, data.firstName, data.email, data.phone, data.productInfo, data.merchantSUrl, data.merchantFUrl, data.hash);
    });
  }
}

var sha512 = require('js-sha512').sha512;


// mPaymentParams.setKey("gtKFFx");
// mPaymentParams.setAmount("15.0");
// mPaymentParams.setProductInfo("Tshirt");
// mPaymentParams.setFirstName("Guru");
// mPaymentParams.setEmail("guru@gmail.com");
// mPaymentParams.setTxnId("0123479543689");
// mPaymentParams.setSurl("https://payu.herokuapp.com/success");
// mPaymentParams.setFurl("https://payu.herokuapp.com/failure");
// mPaymentParams.setUdf1("udf1");
// mPaymentParams.setUdf2("udf2");
// mPaymentParams.setUdf3("udf3");
// mPaymentParams.setUdf4("udf4");
// mPaymentParams.setUdf5("udf5");
// import PayuSdk from 'react-native-payu-sdk';

const key = 'gtKFFx';
const salt = 'eCwWELxi'// 'eCwWELxi';
const txnId = "0123479543689";
const amount = '15.0';
const firstName = 'Guru';
const email = 'guru@gmail.com';
const phone = '9999999999';
const productInfo = 'Tshirt';
const merchantSUrl = 'https://payu.herokuapp.com/success';
const merchantFUrl = 'https://payu.herokuapp.com/failure';

let hash = sha512(key + '|' + txnId + '|' + amount + '|' + productInfo + '|' + firstName + '|' + email + '|||||||||||' + salt).toString('hex');
console.warn("Hash from react native is", hash);

const App = () => {
  return (
    <>
      <StatusBar backgroundColor="navy" barStyle="light-content" />
      <View style={{
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
      }}>
        <Button
          onPress={async () => {
            try {
              let data = await PayuMoney.pay(
                {
                  key,
                  txnId,
                  amount,
                  firstName,
                  email,
                  phone,
                  productInfo,
                  merchantSUrl,
                  merchantFUrl,
                  hash,
                }
              );
              console.warn('final data is ', data);
            } catch (e) {
              console.warn('error is ', e);
            }
          }}
          title="Click to pay" />
      </View>
    </>
  );
};

export default App;
