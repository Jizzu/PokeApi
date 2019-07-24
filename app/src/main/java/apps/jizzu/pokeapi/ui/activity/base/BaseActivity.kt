package apps.jizzu.pokeapi.ui.activity.base

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import apps.jizzu.pokeapi.R

abstract class BaseActivity : AppCompatActivity() {

    fun initToolbar(toolbar: Toolbar, showBackButton: Boolean = false) {
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(showBackButton)
    }
}