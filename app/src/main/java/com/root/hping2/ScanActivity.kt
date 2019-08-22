package com.root.hping2

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView


class ScanActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        var bundle = getIntent().extras.getBundle("IPBundle")
        var ipAddress:String = ""
        var portNumber:String = ""
        var protocol:String = ""
        if(bundle != null) {
            if (!bundle.getString("IPAddress").isNullOrEmpty()) {
                ipAddress = bundle.getString(("IPAddress"))
            }
            if (!bundle.getString("PortNumber").isNullOrEmpty()) {
                portNumber = bundle.getString("PortNumber")
            }
            if (!bundle.getString("Protocol").isNullOrEmpty()) {
                protocol = bundle.getString("Protocol")
            }
        }
        var textView = TextView(this)
        textView.textSize = 40f
        textView.setText(ipAddress )// + " Port:" + portNumber + "Protocol" + protocol)

        // Set the text view as the activity layout
        setContentView(textView)

    }
}
