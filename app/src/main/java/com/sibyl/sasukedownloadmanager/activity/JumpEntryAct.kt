package com.sibyl.sasukedownloadmanager.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
 * @author Sasuke on 2018/7/23.
 */
class JumpEntryAct : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getIntentData()

    }

    fun getIntentData() {
        val dataString = intent.getStringExtra(Intent.EXTRA_TEXT)
        if(dataString.isNullOrEmpty()) finish()


    }
}