package com.root.hping2

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View

import android.content.Intent
import android.content.pm.PackageManager
import android.widget.*
import com.root.hping2.R.id.*
import java.io.*
import javax.xml.transform.Source

class SettingsScreenActivity : AppCompatActivity() {

    private lateinit var ipAddress:EditText
    private lateinit var SourcePort:EditText
    private lateinit var DestPort:EditText
    private lateinit var IncrementDestPort: CheckBox
    private lateinit var IncrementDestPortSent: CheckBox
    private lateinit var TCPOptions:TextView
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
    private lateinit var XmasCheckbox: CheckBox
    private lateinit var YmasCheckbox: CheckBox
    private lateinit var TimestampCheckbox: CheckBox
    private lateinit var SequenceCheckbox: CheckBox
    private lateinit var BadcksumCheckbox: CheckBox
    private lateinit var ProtocolChoice: String
    private lateinit var packetCount: EditText
    private lateinit var TCPWindowSize: EditText
    private lateinit var TCPSequenceNumber: EditText
    private lateinit var TCPOffset: EditText
    private lateinit var RandDest: CheckBox
    private lateinit var RandSource: CheckBox
    private lateinit var Spoof: EditText
    private lateinit var TTL: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        ipAddress = findViewById(IPAddress)
        SourcePort = findViewById(R.id.SourcePort)
        DestPort = findViewById(R.id.DestPort)
        IncrementDestPort = findViewById(IncrementPort)
        IncrementDestPortSent = findViewById(IncrementPortSent)
        radioGroup = findViewById(ProtocolGroup)
        TCPOptions = findViewById(R.id.TCPOptions)
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
        XmasCheckbox = findViewById(Xmas)
        YmasCheckbox = findViewById(Ymas)
        TimestampCheckbox = findViewById(Timestamp)
        SequenceCheckbox = findViewById(Sequence)
        BadcksumCheckbox = findViewById(Badcksum)
        TCPWindowSize = findViewById(R.id.TCPWindowSize)
        TCPSequenceNumber = findViewById(TCPSequence)
        TCPOffset = findViewById(R.id.TCPOffset)
        Spoof = findViewById(R.id.Spoof)
        TTL = findViewById(R.id.TTL)

        radioGroup.setOnCheckedChangeListener((RadioGroup.OnCheckedChangeListener { radioGroup, i ->
            if (i == R.id.TCPRadioButton) {
                ProtocolChoice = ""
                SourcePort.visibility = View.VISIBLE
                DestPort.visibility = View.VISIBLE
                IncrementDestPort.visibility = View.VISIBLE
                IncrementDestPortSent.visibility = View.VISIBLE
                TCPOptions.visibility = View.VISIBLE
                SYNCheckbox.visibility = View.VISIBLE
                SYNCheckbox.visibility = View.VISIBLE
                ACKCheckbox.visibility = View.VISIBLE
                FINCheckbox.visibility = View.VISIBLE
                RSTCheckbox.visibility = View.VISIBLE
                PUSHCheckbox.visibility = View.VISIBLE
                URGCheckbox.visibility = View.VISIBLE
                XmasCheckbox.visibility = View.VISIBLE
                YmasCheckbox.visibility = View.VISIBLE
                TimestampCheckbox.visibility = View.VISIBLE
                SequenceCheckbox.visibility = View.VISIBLE
                BadcksumCheckbox.visibility = View.VISIBLE
                TCPWindowSize.visibility = View.VISIBLE
                TCPSequenceNumber.visibility = View.VISIBLE
                TCPOffset.visibility = View.VISIBLE
            } else if (i == R.id.UDPRadioButton){
                ProtocolChoice = "-2"
                SourcePort.visibility = View.VISIBLE
                DestPort.visibility = View.VISIBLE
                IncrementDestPort.visibility = View.VISIBLE
                IncrementDestPortSent.visibility = View.VISIBLE
                TCPOptions.visibility = View.VISIBLE
                SYNCheckbox.visibility = View.VISIBLE
                SYNCheckbox.visibility = View.VISIBLE
                ACKCheckbox.visibility = View.VISIBLE
                FINCheckbox.visibility = View.VISIBLE
                RSTCheckbox.visibility = View.VISIBLE
                PUSHCheckbox.visibility = View.VISIBLE
                URGCheckbox.visibility = View.VISIBLE
                XmasCheckbox.visibility = View.VISIBLE
                YmasCheckbox.visibility = View.VISIBLE
                TimestampCheckbox.visibility = View.VISIBLE
                SequenceCheckbox.visibility = View.VISIBLE
                BadcksumCheckbox.visibility = View.VISIBLE
                TCPWindowSize.visibility = View.VISIBLE
                TCPSequenceNumber.visibility = View.VISIBLE
                TCPOffset.visibility = View.VISIBLE
            } else if (i == R.id.ICMPRadioButton){
                ProtocolChoice = "-1"
                TCPOptions.visibility = View.GONE
                SYNCheckbox.visibility = View.GONE
                ACKCheckbox.visibility = View.GONE
                FINCheckbox.visibility = View.GONE
                RSTCheckbox.visibility = View.GONE
                PUSHCheckbox.visibility = View.GONE
                URGCheckbox.visibility = View.GONE
                XmasCheckbox.visibility = View.GONE
                YmasCheckbox.visibility = View.GONE
                TimestampCheckbox.visibility = View.GONE
                SequenceCheckbox.visibility = View.GONE
                BadcksumCheckbox.visibility = View.GONE
                TCPWindowSize.visibility = View.GONE
                TCPSequenceNumber.visibility = View.GONE
                TCPOffset.visibility = View.GONE
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
            }else{
                commandList.add("-c 1") //send only 1 packet as default
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
                if (XmasCheckbox.isChecked) {
                    commandList.add("-X")
                }
                if (YmasCheckbox.isChecked) {
                    commandList.add("-Y")
                }
                if (SequenceCheckbox.isChecked) {
                    commandList.add("-Q")
                }
                if (BadcksumCheckbox.isChecked) {
                    commandList.add("-b")
                }
                if (YmasCheckbox.isChecked) {
                    commandList.add("--tcp-timestamp")
                }
            }
            if(TCPRadioButton.isSelected || UDPRadioButton.isSelected) {
                if (SourcePort.text.isNotBlank()){
                    commandList.add("-s")
                    commandList.add(SourcePort.text.toString())
                }
                if (DestPort.text.isNotBlank()){
                    commandList.add("-p")
                    if(IncrementDestPort.isChecked){
                        commandList.add("+" + DestPort.text.toString())
                    }else if(IncrementDestPortSent.isChecked){
                        commandList.add("++" + DestPort.text.toString())
                    }else {
                        commandList.add(DestPort.text.toString())
                    }
                }
            }

            if(RandDest.isChecked){
                commandList.add("--rand-dest")
            }
            if(RandSource.isChecked){
                commandList.add("--rand-dest")
            }

            if(Spoof.text.isNotEmpty())
            {
                commandList.add("-a")
                commandList.add(Spoof.text.toString())
            }

            if(TTL.text.isNotEmpty())
            {
                commandList.add("-t")
                commandList.add(TTL.text.toString())
            }


            if(TCPWindowSize.text.isNotEmpty())
            {
                commandList.add("-w")
                commandList.add(TCPWindowSize.text.toString())
            }
            if(TCPSequenceNumber.text.isNotEmpty())
            {
                commandList.add("-M")
                commandList.add(TCPSequenceNumber.text.toString())
            }
            if(TCPOffset.text.isNotEmpty())
            {
                commandList.add("-O")
                commandList.add(TCPOffset.text.toString())
            }


            if(ipAddress.text.isNullOrBlank()){
                commandList.add("8.8.8.8")
            }else{
                commandList.add(ipAddress.text.toString())
            }

            commandList.add("1>&2") //redirect stdout to stderr

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
