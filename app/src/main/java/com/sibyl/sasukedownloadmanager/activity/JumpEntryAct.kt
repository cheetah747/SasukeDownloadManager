package com.sibyl.sasukedownloadmanager.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.sibyl.sasukedownloadmanager.tools.setText2Clipboard
import org.jetbrains.anko.toast

/**
 * @author Sasuke on 2018/7/23.
 */
class JumpEntryAct : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getIntentData()
        jump2Main()
    }

    fun getIntentData() {
        val dataString = intent.getStringExtra(Intent.EXTRA_TEXT)
        if (dataString.isNullOrEmpty()){
            toast("何も伝わって来ないよ")
            finish()
        }

        //写入剪切板，后面直接跳就行，MainActivity会自动从剪切板里获取数据的
        setText2Clipboard(this, dataString)
    }

    fun jump2Main(){
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }
}