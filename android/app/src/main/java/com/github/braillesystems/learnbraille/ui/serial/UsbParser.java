package com.github.braillesystems.learnbraille.ui.serial;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

import com.github.braillesystems.learnbraille.R;
import com.github.braillesystems.learnbraille.data.entities.BrailleDots;

import java.util.regex.Pattern;

import timber.log.Timber;

public class UsbParser {
    private static UsbSignalHandler signalHandler = new UsbSignalHandler() {
    };
    private static UsbWrapper usbWrapper = null;

    // call once
    public static void init(Activity activity) {
        if (usbWrapper == null)
            usbWrapper = new UsbWrapper(new SerialSignalHandler(), activity);
    }

    public static void setSignalHandler(UsbSignalHandler handler) {
        signalHandler = handler;
    }

    public static void trySend(BrailleDots symbol) {
        if (usbWrapper == null)
            return;
        Timber.i(symbol.toString());
        usbWrapper.send(symbol.toString().replace("F", "1").replace("E", "0"));
    }

    private static class SerialSignalHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what != UsbService.MESSAGE_FROM_SERIAL_PORT)
                return;
            if (signalHandler == null)
                return;
            String data = (String) msg.obj;
            if (Pattern.matches("[0-1]+", data) && data.length() == 6) {
                BrailleDots symbol = new BrailleDots(data.replace("1", "F").replace("0", "E"));
                signalHandler.onSymbolInput(symbol);
                return;
            }
            switch (data.charAt(0)) {
                case 'l':
                    signalHandler.onJoystickLeft();
                    break;
                case 'r':
                    signalHandler.onJoystickRight();
                    break;
                case 'u':
                    signalHandler.onJoystickUp();
                    break;
                case 'd':
                    signalHandler.onJoystickDown();
                    break;
                default:
                    usbWrapper.send("?");
                    return;
            }
            try {
                Thread.sleep(500);
                usbWrapper.send("?");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

final class UsbWrapper {
    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case UsbService.ACTION_USB_PERMISSION_GRANTED: // USB PERMISSION GRANTED
                    Toast.makeText(context, R.string.usb_permission_granted, Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_USB_PERMISSION_NOT_GRANTED: // USB PERMISSION NOT GRANTED
                    Toast.makeText(context, R.string.usb_permission_denied, Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_NO_USB: // NO USB CONNECTED
                    break;
                case UsbService.ACTION_USB_DISCONNECTED: // USB DISCONNECTED
                    Toast.makeText(context, R.string.usb_device_disconnected, Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_USB_NOT_SUPPORTED: // USB NOT SUPPORTED
                    Toast.makeText(context, R.string.usb_unknown_device, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    private UsbService usbService;
    private Handler mHandler;
    private long birthTimeMillis = System.currentTimeMillis();

    private void startService(ServiceConnection serviceConnection, Activity parentActivity) {
        if (!UsbService.SERVICE_CONNECTED) {
            Intent startService = new Intent(parentActivity, UsbService.class);
            parentActivity.startService(startService);
        }
        Intent bindingIntent = new Intent(parentActivity, UsbService.class);
        parentActivity.bindService(bindingIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void setFilters(Activity parentActivity) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbService.ACTION_USB_PERMISSION_GRANTED);
        filter.addAction(UsbService.ACTION_NO_USB);
        filter.addAction(UsbService.ACTION_USB_DISCONNECTED);
        filter.addAction(UsbService.ACTION_USB_NOT_SUPPORTED);
        filter.addAction(UsbService.ACTION_USB_PERMISSION_NOT_GRANTED);
        parentActivity.registerReceiver(mUsbReceiver, filter);
    }

    void send(String data) {
        long pauseAfterConnection = 5000;
        if (System.currentTimeMillis() - birthTimeMillis < pauseAfterConnection) {
            return;
        }
        if (usbService != null) { // if UsbService was correctly binded, Send data
            usbService.write(data.getBytes());
        }
    }

    UsbWrapper(Handler handler, Activity parentActivity) {
        mHandler = handler;
        setFilters(parentActivity);  // Start listening notifications from UsbService
        ServiceConnection usbConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName arg0, IBinder arg1) {
                usbService = ((UsbService.UsbBinder) arg1).getService();
                usbService.setHandler(mHandler);
            }

            @Override
            public void onServiceDisconnected(ComponentName arg0) {
                usbService = null;
            }
        };
        startService(usbConnection, parentActivity); // Start UsbService
    }
}
