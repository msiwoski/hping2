package com.root.hping2

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.support.annotation.RequiresApi
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.RadioGroup
import com.root.hping2.R.id.*
import org.w3c.dom.Text
import java.io.*

class SettingsScreenActivity : AppCompatActivity() {

    private lateinit var ipAddress:EditText
    private lateinit var host:EditText
    private lateinit var radioGroup: RadioGroup
    private lateinit var TCPRadioButton: RadioButton
    private lateinit var UDPRadioButton: RadioButton
    private lateinit var ICMPRadioButton: RadioButton
    private lateinit var SYNCheckbox: CheckBox
    private lateinit var ACKCheckbox: CheckBox
    private lateinit var FINCheckbox: CheckBox
    private lateinit var RSTCheckbox: CheckBox
    private lateinit var PUSHCheckbox: CheckBox
    private lateinit var URGCheckbox: CheckBox
    private lateinit var ProtocolChoice: String
    private lateinit var packetCount: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        ipAddress = findViewById(IPAddress)
        host = findViewById(PortNumber)
        radioGroup = findViewById(ProtocolGroup)
        TCPRadioButton = findViewById(R.id.TCPRadioButton)
        UDPRadioButton = findViewById(R.id.UDPRadioButton)
        ICMPRadioButton = findViewById(R.id.ICMPRadioButton)
        radioGroup.check(TCPRadioButton.id)
        ProtocolChoice = ""
        packetCount = findViewById(Count)
        SYNCheckbox = findViewById(Syn)
        ACKCheckbox = findViewById(ACK)
        FINCheckbox = findViewById(Fin)
        RSTCheckbox = findViewById(RST)
        PUSHCheckbox = findViewById(Push)
        URGCheckbox = findViewById(URG)


//
//        ipAddress.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//            }
//        })
//
//        host.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//            }
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//            }
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//            }
//        })
//
//        packetCount.addTextChangedListener(object: TextWatcher{
//            override fun afterTextChanged(s: Editable?) {
//            }
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//            }
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//            }
//        })

        radioGroup.setOnCheckedChangeListener((RadioGroup.OnCheckedChangeListener { radioGroup, i ->
            if (i == R.id.TCPRadioButton) {
                ProtocolChoice = ""
            } else if (i == R.id.UDPRadioButton){
                ProtocolChoice = "-2"
            } else if (i == R.id.ICMPRadioButton){
                ProtocolChoice = "-1"
            }
        }))

    }

    fun RunAsRoot(cmd: ArrayList<String>): String {
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
        var commandList: ArrayList<String> = arrayListOf<String>()

        val pm = packageManager
        try {
            val rootShell = Runtime.getRuntime().exec("su")
            val os = DataOutputStream(rootShell.outputStream)
            val resultReader = rootShell.inputStream.bufferedReader()

            val ai = pm.getApplicationInfo("com.root.hping2", 0)
            val hping2Executable = "${ai.nativeLibraryDir}/libhping2.so"

            commandList.add(hping2Executable)

            if(ProtocolChoice.isNotBlank()){
                commandList.add(ProtocolChoice)
            }


            if(packetCount.text.isNotEmpty())
            {
                commandList.add("-c")
                commandList.add(packetCount.text.toString())
            }

            if(ProtocolChoice.equals("")) {

                if (SYNCheckbox.isChecked) {
                    commandList.add("-S")
                }
                if (ACKCheckbox.isChecked) {
                    commandList.add("-A")
                }
                if (FINCheckbox.isChecked) {
                    commandList.add("-F")
                }
                if (RSTCheckbox.isChecked) {
                    commandList.add("-R")
                }
                if (PUSHCheckbox.isChecked) {
                    commandList.add("-P")
                }
                if (URGCheckbox.isChecked) {
                    commandList.add("-U")
                }
            }

            if(ipAddress.text.isNullOrBlank()){
                commandList.add("8.8.8.8")
            }else{
                commandList.add(ipAddress.text.toString())
            }


            val result = RunAsRoot(commandList)
            ipBundle.putString("IPAddress", result)
            intent.putExtra("IPBundle", ipBundle)
            println(result)
        } catch (e : PackageManager.NameNotFoundException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }

        startActivity(intent)
    }
}
