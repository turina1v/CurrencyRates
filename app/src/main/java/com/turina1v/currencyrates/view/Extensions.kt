package com.turina1v.currencyrates.view

import androidx.core.view.children
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup

fun MaterialButtonToggleGroup.checkButtonByText(text: String, onNotFound: () -> Unit) {
    (this.children.firstOrNull {
        (it as MaterialButton).text == text
    } as? MaterialButton)?.let {
        it.isChecked = true
    } ?: onNotFound.invoke()
}

fun MaterialButtonToggleGroup.getFocusedButtonTextOrEmpty(): String {
    return (this.children.firstOrNull {
        (it as? MaterialButton)?.isChecked == true
    } as? MaterialButton)?.text?.toString() ?: ""
}