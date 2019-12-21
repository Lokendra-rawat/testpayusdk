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
  SafeAreaView,
  StyleSheet,
  ScrollView,
  View,
  Text,
  StatusBar,
  NativeModules,
  Button,
} from 'react-native';

import {
  Header,
  LearnMoreLinks,
  Colors,
  DebugInstructions,
  ReloadInstructions,
} from 'react-native/Libraries/NewAppScreen';

const { payu } = NativeModules;
var sha512 = require('js-sha512').sha512;

const key = 'gtKFFx';
const txnId = Math.floor(1000000000 + Math.random() * 900000000).toString();
const amount = '15.0';
const firstName = 'lokendra';
const email = 'rawat';
const phone = '9999999999';
const productInfo = 'productInfo';
const merchantSUrl = 'https://google.co.in';
const merchantFUrl = 'https://yahoo.co.in';
let hash = sha512('gtKFFx|' + txnId + '|' + amount + '|' + productInfo + '|' + firstName + '|' + email + '|||||||||||eCwWELxi').toString('hex');

const App = () => {
  console.warn(Object.keys(payu));
  return (
    <>
      <StatusBar backgroundColor="navy" barStyle="light-content" />
      <View style={{
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
      }}>
        <Button
          onPress={() => {
            payu.makePayment(
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
            );
            console.warn('payment button');
          }}
          title="Click to pay" />
      </View>
    </>
  );
};

const styles = StyleSheet.create({
  scrollView: {
    backgroundColor: Colors.lighter,
  },
  engine: {
    position: 'absolute',
    right: 0,
  },
  body: {
    backgroundColor: Colors.white,
  },
  sectionContainer: {
    marginTop: 32,
    paddingHorizontal: 24,
  },
  sectionTitle: {
    fontSize: 24,
    fontWeight: '600',
    color: Colors.black,
  },
  sectionDescription: {
    marginTop: 8,
    fontSize: 18,
    fontWeight: '400',
    color: Colors.dark,
  },
  highlight: {
    fontWeight: '700',
  },
  footer: {
    color: Colors.dark,
    fontSize: 12,
    fontWeight: '600',
    padding: 4,
    paddingRight: 12,
    textAlign: 'right',
  },
});

export default App;
