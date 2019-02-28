package com.root.hping2

class Main {
    //external fun callMainFromJNI(pString: String): String
    external fun callMainFromJNI()
    external fun callMainFromJNI2(pChar: Char)
    external fun callMainFromJNI3(pCharArray: CharArray)
    external fun callMainFromJNI4(pString: String)
    companion object {

        init {
            System.loadLibrary("Main")
        }
    }
}