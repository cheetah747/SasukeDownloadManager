package com.sibyl.sasukedownloadmanager

import android.Manifest
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import com.github.dfqin.grantor.PermissionListener
import com.github.dfqin.grantor.PermissionsUtil

class MainActivity : AppCompatActivity() {
    val downloadUrl = "https://oalxfnrvo.qnssl.com/V4.5.0_ShengYiGuanJia180717.apk"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    fun init() {
        requestPermissions()
        val url = "https://oalxfnrvo.qnssl.com/V4.5.0_ShengYiGuanJia180717.apk"
        AlertDialog.Builder(this).setTitle("ダウンロードを始めますか？")
                .setMessage(url)
                .setPositiveButton("はい", DialogInterface.OnClickListener { dialog, which ->
                    DownloadUtil().download(this,url)
                    finish()
                })
                .setNegativeButton("いいえ", DialogInterface.OnClickListener { dialog, which ->
                    finish()
                })
                .create().show()
    }

    fun requestPermissions() {
        val permissions = arrayOf(
                Manifest.permission.INTERNET,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        )
        if (!PermissionsUtil.hasPermission(this, *permissions)) {
            PermissionsUtil.requestPermission(this, object : PermissionListener {
                override fun permissionGranted(permissions: Array<String>) {
                    //用户授予了权限
                }

                override fun permissionDenied(permissions: Array<String>) {
                    //用户拒绝了权限
                    requestPermissions()
                }
            }, *permissions)
        }
    }
}
