package com.root.hping2

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import com.root.hping2.R.id.IPAddress
import com.root.hping2.R.id.PortNumber
import com.root.hping2.R.id.ProtocolGroup
import com.root.hping2.Hping2

import android.content.Intent
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import java.io.PipedOutputStream
import java.io.PipedInputStream
import java.io.PrintStream


class SettingsScreenActivity : AppCompatActivity() {

    private lateinit var ipAddress:EditText
    private lateinit var host:EditText
    private lateinit var radioGroup: RadioGroup
    private lateinit var TCPRadioButton: RadioButton
    private lateinit var UDPRadioButton: RadioButton
    private lateinit var ProtocolChoice: String
    private val MainCPP = Hping2()
    //private lateinit var btn:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        //btn = findViewById<Button>(com.root.hping2.R.id.ScanButton)
        ipAddress = findViewById<EditText>(IPAddress)
        host = findViewById<EditText>(PortNumber)
        //radioGroup = findViewById(ProtocolGroup)
//        TCPRadioButton = findViewById(R.id.TCPRadioButton)
//        UDPRadioButton = findViewById(R.id.UDPRadioButton)
//        //radioGroup.addView(TCPRadioButton)
        //radioGroup.check(TCPRadioButton.id)
        ProtocolChoice = "TCP"
        //radioGroup.addView(UDPRadioButton)


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

//        radioGroup.setOnCheckedChangeListener((RadioGroup.OnCheckedChangeListener { radioGroup, i ->
//            if (i == R.id.TCPRadioButton) {
//                ProtocolChoice = "TCP"
//            } else {
//                ProtocolChoice = "UDP"
//            }
//        }))
    }

    fun startButton(view: View)
    {
        val intent = Intent(this, ScanActivity::class.java).apply{ }
        val ipBundle = Bundle()
        ipBundle.putString("IPAddress", ipAddress.text.toString())
        ipBundle.putString("PortNumber", host.text.toString())
        ipBundle.putString("Protocol", ProtocolChoice)
        intent.putExtra("IPBundle", ipBundle)

        Hping2.invokeHping2(arrayOf("hping2", "--version"));

        startActivity(intent)
    }
}
