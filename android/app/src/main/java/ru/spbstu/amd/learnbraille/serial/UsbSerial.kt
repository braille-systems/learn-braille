package ru.spbstu.amd.learnbraille.serial

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbDeviceConnection
import android.hardware.usb.UsbManager
import com.felhr.usbserial.UsbSerialDevice
import com.felhr.usbserial.UsbSerialInterface
import ru.spbstu.amd.learnbraille.database.BrailleDots
import timber.log.Timber


/**********************************/
/********** TODO refactor *********/
/**********************************/


class UsbSerial(usbManager: UsbManager, context: Context) {
    private var mUsbManager: UsbManager = usbManager
    private var invokingContext: Context = context
    var mDevice: UsbDevice? = null
    var mSerial: UsbSerialDevice? = null
    var mConnection: UsbDeviceConnection? = null
    private var birthTime: Long = System.currentTimeMillis()

    companion object {
        const val arduinoNanoVendorId = 6790
        const val ACTION_USB_PERMISSION = "permission"
        const val USB_SERVICE = "usb" // TODO why error: USB_SERVICE = Context.USB_SERVICE
    }

    init {
        startUsbConnection()
    }

    fun trySend(brailleDots: BrailleDots) {
        val setupTimeMillis = 5_000
        val data = brailleDots.toString().replace("F", "1").replace("E", "0")
        if (System.currentTimeMillis() - birthTime > setupTimeMillis) {
            sendData(data)
        }
    }


    private fun startUsbConnection() {
        val usbDevices: HashMap<String, UsbDevice>? = mUsbManager.deviceList
        if (!usbDevices?.isEmpty()!!) {
            var keep = true
            usbDevices.forEach { entry ->
                mDevice = entry.value
                val deviceVendorId: Int? = mDevice?.vendorId
                if (deviceVendorId == arduinoNanoVendorId) {
                    val intent: PendingIntent =
                        PendingIntent.getBroadcast(
                            invokingContext,
                            0,
                            Intent(ACTION_USB_PERMISSION),
                            0
                        )
                    mUsbManager.requestPermission(mDevice, intent)
                    keep = false
                    debugLog("connection successful")
                } else {
                    mConnection = null
                    mDevice = null
                    debugLog("unable to connect " + deviceVendorId.toString())
                }
            }
            if (!keep) {
                return
            }
        } else {
            debugLog("no usb connected")
        }
    }

    private fun sendData(input: String) {
        mSerial?.write(input.toByteArray())
        debugLog("sending data: $input")
    }

    private fun disconnectUsb() {
        mSerial?.close()
    }

    private fun debugLog(msg: String) {
        Timber.i(msg)
    }

    val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                UsbManager.ACTION_USB_ACCESSORY_ATTACHED -> {
                    startUsbConnection()
                    return
                }
                UsbManager.ACTION_USB_ACCESSORY_DETACHED -> {
                    disconnectUsb()
                    return
                }
                ACTION_USB_PERMISSION -> {
                }
                else -> return
            }
            val granted: Boolean =
                intent.extras!!.getBoolean(UsbManager.EXTRA_PERMISSION_GRANTED)
            if (!granted) {
                debugLog("permission is not granted")
                return
            }
            mConnection = mUsbManager.openDevice(mDevice)
            mSerial = UsbSerialDevice.createUsbSerialDevice(mDevice, mConnection)
            if (mSerial == null) {
                debugLog("port is none")
                return
            }
            if (!mSerial!!.open()) {
                debugLog("port not open")
                return
            }
            mSerial!!.setBaudRate(9600)
            mSerial!!.setDataBits(UsbSerialInterface.DATA_BITS_8)
            mSerial!!.setStopBits(UsbSerialInterface.STOP_BITS_1)
            mSerial!!.setParity(UsbSerialInterface.PARITY_NONE)
            mSerial!!.setFlowControl(UsbSerialInterface.FLOW_CONTROL_OFF)
        }
    }
}