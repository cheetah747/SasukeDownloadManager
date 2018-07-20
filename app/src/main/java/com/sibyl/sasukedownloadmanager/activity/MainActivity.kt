package com.sibyl.sasukedownloadmanager.activity

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.github.dfqin.grantor.PermissionListener
import com.github.dfqin.grantor.PermissionsUtil
import com.sibyl.sasukedownloadmanager.R
import com.sibyl.sasukedownloadmanager.tools.DownloadUtil
import com.sibyl.sasukedownloadmanager.tools.FilePropertyGetter
import com.sibyl.sasukedownloadmanager.tools.tranSizeText
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.async
import org.jetbrains.anko.uiThread

class MainActivity : AppCompatActivity() {
    //    val downloadUrl = "https://oalxfnrvo.qnssl.com/V4.5.0_ShengYiGuanJia180717.apk"
    var url: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        start()
    }

    fun init() {
        requestPermissions()
        url = getTextFromClipboard().apply {
            if (this.isNullOrEmpty() || !this.startsWith("http"))
                Snackbar.make(mainTv,"URL有误",Snackbar.LENGTH_LONG)
                finish()
        }
//        val url = "https://oalxfnrvo.qnssl.com/V4.5.0_ShengYiGuanJia180717.apk"
//        url = "https://d-07.winudf.com/b/apk/Y29tLnRlbmNlbnQubW9iaWxlcXFpXzY1MDBfMzk1NDYxNDU?_fn=UVEgSW50ZXJuYXRpb25hbCBDaGF0IENhbGxfdjYuMC4wX2Fwa3B1cmUuY29tLmFwaw&_p=Y29tLnRlbmNlbnQubW9iaWxlcXFp&as=43903923c1038afbaf02653841c18d255b517f21&c=1%7CCOMMUNICATION%7CZGV2PVRlbmNlbnQlMjBUZWNobm9sb2d5JTIwKFNoZW56aGVuKSUyMENvbXBhbnklMjBMdGQuJnZuPTYuMC4wJnZjPTY1MDA&k=61defa0ad92f9e1aca28dbef6b40b6755b54027d"
    }

    fun start() {
        //先把弹窗显示出来
        val dialog = AlertDialog.Builder(this@MainActivity)
                .setTitle("少々お待ちください")
                .setView(R.layout.dialog_layout)
                .setPositiveButton("はい", { dialog, which ->
                    DownloadUtil().download(this@MainActivity, url)
                    finish()
                })
                .setCancelable(false)
                .setNegativeButton("いいえ", { dialog, which ->
                    finish()
                }).create().apply { takeUnless { this@MainActivity.isFinishing }?.show() }
        dialog.findViewById<TextView>(R.id.url)?.text = url

        //再获取文件名和大小
        async {
            val fileName = FilePropertyGetter.INSTANCE.getFileProperties(url).fileName
            val filesizeText = tranSizeText(FilePropertyGetter.INSTANCE.getFileProperties(url).fileSize)
            uiThread {
                dialog?.setTitle("ダウンロードしますか？")
                //显示文件名，大小
                arrayOf(R.id.fileNameTv, R.id.fileSizeTv).forEach {
                    when (it) {
                        R.id.fileNameTv -> dialog?.findViewById<TextView>(it)?.apply { text = fileName;visibility = View.VISIBLE }
                        R.id.fileSizeTv -> dialog?.findViewById<TextView>(it)?.apply { text = filesizeText;visibility = View.VISIBLE }
                    }
                }
                //隐藏进度
                dialog?.findViewById<ProgressBar>(R.id.progressBar)?.visibility = View.GONE
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

    /**
     * 从剪切板获取内容
     */
    fun getTextFromClipboard(): String {
        val myClipboard = (getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
        val abc: ClipData? = myClipboard.getPrimaryClip()
        return abc?.getItemAt(0)?.getText().toString()
    }
}
