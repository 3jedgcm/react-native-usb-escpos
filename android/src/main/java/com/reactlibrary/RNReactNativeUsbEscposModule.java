
package com.reactlibrary;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;


import com.dantsu.escposprinter.EscPosPrinter;
import com.dantsu.escposprinter.connection.usb.UsbConnection;
import com.dantsu.escposprinter.connection.usb.UsbPrintersConnections;
import com.dantsu.escposprinter.exceptions.EscPosBarcodeException;
import com.dantsu.escposprinter.exceptions.EscPosConnectionException;
import com.dantsu.escposprinter.exceptions.EscPosEncodingException;
import com.dantsu.escposprinter.exceptions.EscPosParserException;
import com.dantsu.escposprinter.textparser.PrinterTextParserImg;



public class RNReactNativeUsbEscposModule extends ReactContextBaseJavaModule {

  public final ReactApplicationContext reactContext;
  private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
  private final BroadcastReceiver usbReceiver = new BroadcastReceiver() {
    public void onReceive(Context context, Intent intent) {
      String action = intent.getAction();
      if (RNReactNativeUsbEscposModule.ACTION_USB_PERMISSION.equals(action)) {
        synchronized (this) {
          UsbManager usbManager = (UsbManager)  context.getSystemService(Context.USB_SERVICE);
          UsbDevice usbDevice = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
          if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
            if (usbManager != null && usbDevice != null) {
              EscPosPrinter printer = null;
              try {
                printer = new EscPosPrinter(new UsbConnection(usbManager, usbDevice), 203, 48f, 32);
                printer.printFormattedText(intent.getExtras().getString("message"));
              } catch (EscPosConnectionException e) {
                e.printStackTrace();
              } catch (EscPosParserException e) {
                e.printStackTrace();
              } catch (EscPosEncodingException e) {
                e.printStackTrace();
              } catch (EscPosBarcodeException e) {
                e.printStackTrace();
              }
            }
          }
        }
      }
    }
  };

  public void printUsb(String message) {
    UsbConnection usbConnection = UsbPrintersConnections.selectFirstConnected(this.reactContext);
    UsbManager usbManager = (UsbManager) this.reactContext.getSystemService(Context.USB_SERVICE);
    if (usbConnection != null && usbManager != null) {
      PendingIntent permissionIntent = PendingIntent.getBroadcast(this.reactContext, 0, new Intent(RNReactNativeUsbEscposModule.ACTION_USB_PERMISSION), 0);
      IntentFilter filter = new IntentFilter(RNReactNativeUsbEscposModule.ACTION_USB_PERMISSION);
      Intent i = this.reactContext.registerReceiver(this.usbReceiver, filter);
      Bundle b = i.getExtras();
      b.putString("message",message);
      usbManager.requestPermission(usbConnection.getDevice(), permissionIntent);
    }
  }

  public RNReactNativeUsbEscposModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  public ReactApplicationContext getContext() {
      return this.reactContext;
  }

  @ReactMethod
  public void show(String message, int duration) {

  }

  @Override
  public String getName() {
    return "RNReactNativeUsbEscpos";
  }
}