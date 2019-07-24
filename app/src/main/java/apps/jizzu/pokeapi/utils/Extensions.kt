package apps.jizzu.pokeapi.utils

import android.app.Activity
import android.view.View
import android.widget.Toast

fun View?.gone() {
    this?.visibility = View.GONE
}

fun View?.visible() {
    this?.visibility = View.VISIBLE
}

fun Activity?.toast(text: String) {
    if (this != null) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}