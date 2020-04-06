package ru.spbstu.amd.learnbraille

import android.app.Application
import android.content.Context.USB_SERVICE
import android.hardware.usb.UsbManager

fun <T, R> T.side(f: (T) -> R) {
    f(this)
}

val Application.usbManager get() = getSystemService(USB_SERVICE) as UsbManager
