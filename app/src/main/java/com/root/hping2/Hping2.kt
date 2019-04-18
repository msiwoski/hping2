package com.root.hping2

class Hping2 {
    companion object {

        init {
            System.loadLibrary("hping2glue")
        }

        external fun invokeHping2(commandLine: Array<String>)
    }
}