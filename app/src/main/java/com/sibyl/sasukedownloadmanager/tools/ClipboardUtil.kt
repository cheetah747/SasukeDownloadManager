package com.sibyl.sasukedownloadmanager.tools

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

/**
 * @author Sasuke on 2018/7/23.
 */
/**
 * 从剪切板获取内容
 */
fun getTextFromClipboard(context: Context): String {
    val myClipboard = (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
    val abc: ClipData? = myClipboard.getPrimaryClip()
    return abc?.getItemAt(0)?.getText().toString()
}

/**
 * 把内容写入剪切板
 */
fun setText2Clipboard(context: Context,str: String){
    val myClipboard = (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
    val newData: ClipData = ClipData.newPlainText("",str)
    myClipboard.primaryClip = newData
}