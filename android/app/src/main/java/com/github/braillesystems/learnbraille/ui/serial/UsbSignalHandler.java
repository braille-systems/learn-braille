package com.github.braillesystems.learnbraille.ui.serial;

import com.github.braillesystems.learnbraille.data.entities.BrailleDots;

public interface UsbSignalHandler {
    default void onJoystickUp(){}
    default void onJoystickDown(){}
    default void onJoystickLeft(){}
    default void onJoystickRight(){}
    default void onSymbolInput(BrailleDots symbol){}
}
