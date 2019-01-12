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
