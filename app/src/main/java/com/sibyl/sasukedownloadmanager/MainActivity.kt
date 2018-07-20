package com.sibyl.sasukedownloadmanager

import android.Manifest
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import com.github.dfqin.grantor.PermissionListener
import com.github.dfqin.grantor.PermissionsUtil
import org.jetbrains.anko.async
import org.jetbrains.anko.uiThread

class MainActivity : AppCompatActivity() {
    val downloadUrl = "https://oalxfnrvo.qnssl.com/V4.5.0_ShengYiGuanJia180717.apk"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    fun init() {
        requestPermissions()
//        val url = "https://oalxfnrvo.qnssl.com/V4.5.0_ShengYiGuanJia180717.apk"
        val url = "https://d-07.winudf.com/b/apk/Y29tLnRlbmNlbnQubW9iaWxlcXFpXzY1MDBfMzk1NDYxNDU?_fn=UVEgSW50ZXJuYXRpb25hbCBDaGF0IENhbGxfdjYuMC4wX2Fwa3B1cmUuY29tLmFwaw&_p=Y29tLnRlbmNlbnQubW9iaWxlcXFp&as=43903923c1038afbaf02653841c18d255b517f21&c=1%7CCOMMUNICATION%7CZGV2PVRlbmNlbnQlMjBUZWNobm9sb2d5JTIwKFNoZW56aGVuKSUyMENvbXBhbnklMjBMdGQuJnZuPTYuMC4wJnZjPTY1MDA&k=61defa0ad92f9e1aca28dbef6b40b6755b54027d"
        AlertDialog.Builder(this@MainActivity).setTitle("少々お待ちください").

        async {
            val fileName = FilePropertyGetter.INSTANCE.getFileProperties(downloadUrl).fileName
            val filesizeText = tranSizeText(FilePropertyGetter.INSTANCE.getFileProperties(downloadUrl).fileSize)
            uiThread {
                AlertDialog.Builder(this@MainActivity).setTitle("ダウンロードを始めますか？")
                        .setMessage(url)
                        .setPositiveButton("はい", { dialog, which ->
                            DownloadUtil().download(this@MainActivity, url, fileName)
                            finish()
                        })
                        .setCancelable(false)
                        .setNegativeButton("いいえ", { dialog, which ->
                            finish()
                        })
                        .create().takeUnless { this@MainActivity.isFinishing }?.show()
            }
        }

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
