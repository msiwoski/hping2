package com.root.hping2

class main {
    external fun callMainFromJNI(pString: String): String

    companion object {

        init {
            System.loadLibrary("main")
        }
    }
}