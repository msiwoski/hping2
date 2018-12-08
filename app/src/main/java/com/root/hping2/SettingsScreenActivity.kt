package com.root.hping2

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import com.root.hping2.R.id.IPAddress
import com.root.hping2.R.id.PortNumber

import android.content.Intent
import android.widget.Button


class SettingsScreenActivity : AppCompatActivity() {

    private lateinit var ipAddress:EditText
    private lateinit var host:EditText
    //private lateinit var btn:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        //btn = findViewById<Button>(com.root.hping2.R.id.ScanButton)
        ipAddress = findViewById<EditText>(IPAddress)
        host = findViewById<EditText>(PortNumber)

        ipAddress.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                ipAddress.setText(s)
            }
        })

        host.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                host.setText(s)
            }
        })
    }

    fun startButton(view: View)
    {
        val intent = Intent(this, ScanActivity::class.java).apply{ }
        val ipBundle = Bundle()
        ipBundle.putString("IPAddress", ipAddress.text.toString())
        ipBundle.putString("PortNumber", host.text.toString())
        intent.putExtra("IPBundle", ipBundle)
        startActivity(intent)
    }

}
