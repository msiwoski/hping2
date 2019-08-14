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
import android.content.pm.PackageManager
import android.os.Build
import android.support.annotation.RequiresApi
import android.widget.RadioButton
import android.widget.RadioGroup
import java.io.*

class SettingsScreenActivity : AppCompatActivity() {

    private lateinit var ipAddress:EditText
    private lateinit var host:EditText
    private lateinit var radioGroup: RadioGroup
    private lateinit var TCPRadioButton: RadioButton
    private lateinit var UDPRadioButton: RadioButton
    private lateinit var ProtocolChoice: String
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

    fun RunAsRoot(cmd: Array<String>): String {
        val theRun: String?
        try {
            val cmdString = cmd.joinToString(" ", "", "\n")

            val rootShell = Runtime.getRuntime().exec(arrayOf("/system/bin/sh", "-c","su"))
            val rootShellCommandStream = DataOutputStream(rootShell.outputStream)

            rootShellCommandStream.writeBytes(cmdString)
            rootShellCommandStream.writeBytes("exit\n")
            rootShellCommandStream.flush()

            rootShell.waitFor()

            val allOutText = rootShell.inputStream.bufferedReader().use(BufferedReader::readText)
            val allErrorText = rootShell.errorStream.bufferedReader().use(BufferedReader::readText)
            val result = StringBuilder()
            result.append(allOutText)
            result.append(allErrorText)
            theRun = result.toString()
        } catch (e: IOException) {
            throw RuntimeException(e)
        } catch (e: InterruptedException) {
            throw RuntimeException(e)
        }

        return theRun//return output
    }

    fun startButton(view: View)
    {
        val intent = Intent(this, ScanActivity::class.java).apply{ }
        val ipBundle = Bundle()
        //ipBundle.putString("IPAddress", ipAddress.text.toString())
        //ipBundle.putString("PortNumber", host.text.toString())
        //ipBundle.putString("Protocol", ProtocolChoice)
        //intent.putExtra("IPBundle", ipBundle)

        val pm = packageManager
        try {
            val rootShell = Runtime.getRuntime().exec("su")
            val os = DataOutputStream(rootShell.outputStream)
            val resultReader = rootShell.inputStream.bufferedReader()

            val ai = pm.getApplicationInfo("com.root.hping2", 0)
            val hping2Executable = "${ai.nativeLibraryDir}/libhping2.so"
            val result = RunAsRoot(arrayOf(hping2Executable, "8.8.8.8", "-c", "1"))
            intent.putExtra("IPBundle", result)
            println(result)
        } catch (e : PackageManager.NameNotFoundException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }

        startActivity(intent)
    }
}
