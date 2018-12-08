package com.root.hping2

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {

        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }



    fun openSettingsScreen(view: View)
    {
        val intent = Intent(this, SettingsScreenActivity::class.java).apply{

        }
        startActivity(intent)

    }

    fun openHelpScreen(view: View)
    {
        val intent = Intent(this, HelpScreenActivity::class.java).apply{

        }
        startActivity(intent)

    }

    fun openAboutScreen(view: View)
    {
        val intent = Intent(this, AboutActivity::class.java).apply{

        }
        startActivity(intent)

    }
}
