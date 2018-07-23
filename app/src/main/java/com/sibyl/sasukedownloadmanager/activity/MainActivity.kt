package com.sibyl.sasukedownloadmanager.activity

import android.Manifest
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.github.dfqin.grantor.PermissionListener
import com.github.dfqin.grantor.PermissionsUtil
import com.sibyl.sasukedownloadmanager.R
import com.sibyl.sasukedownloadmanager.tools.*
import org.jetbrains.anko.async
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread

class MainActivity : BaseActivity() {
    //    val downloadUrl = "https://oalxfnrvo.qnssl.com/V4.5.0_ShengYiGuanJia180717.apk"
    var url: String = ""
    var downloadUtil: DownloadUtil? = null
    var homeDialog: AlertDialog? = null
    var isWithFinish: Boolean = true//是否一同关掉activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        start()
    }

    fun init() {
        requestPermissions()
        url = getTextFromClipboard(this).apply {
            when (true) {
                isNullOrEmpty() -> toast("クリップボードは空っぽだよ").apply { homeDialog?.dismiss() }
                !startsWith("http") -> toast("URLは間違ってる").apply { homeDialog?.dismiss() }
            }
        }
        downloadUtil = DownloadUtil(url)
//        val url = "https://oalxfnrvo.qnssl.com/V4.5.0_ShengYiGuanJia180717.apk"
//        url = "https://d-07.winudf.com/b/apk/Y29tLnRlbmNlbnQubW9iaWxlcXFpXzY1MDBfMzk1NDYxNDU?_fn=UVEgSW50ZXJuYXRpb25hbCBDaGF0IENhbGxfdjYuMC4wX2Fwa3B1cmUuY29tLmFwaw&_p=Y29tLnRlbmNlbnQubW9iaWxlcXFp&as=43903923c1038afbaf02653841c18d255b517f21&c=1%7CCOMMUNICATION%7CZGV2PVRlbmNlbnQlMjBUZWNobm9sb2d5JTIwKFNoZW56aGVuKSUyMENvbXBhbnklMjBMdGQuJnZuPTYuMC4wJnZjPTY1MDA&k=61defa0ad92f9e1aca28dbef6b40b6755b54027d"
    }

    fun start() {
        //先把弹窗显示出来
        homeDialog = AlertDialog.Builder(this@MainActivity)
                .setTitle("少々お待ちください")
                .setView(R.layout.dialog_layout)
                .setPositiveButton("はい", { dg, which ->
                    downloadUtil?.run {
                        fileName = homeDialog?.findViewById<TextView>(R.id.fileNameTv)?.text.toString().trim()
                        download(this@MainActivity)
                    }
                    homeDialog?.dismiss()
                })
                .setCancelable(false)
                .setNegativeButton("いいえ", { dialog, which ->
                    homeDialog?.dismiss()
                })
                .setNeutralButton("リンクを見る", { dialog, which ->
                    isWithFinish = false//不要关掉Activity
                    AlertDialog.Builder(this@MainActivity)
                            .setMessage(url)
                            .setTitle("")
                            .setCancelable(false)
                            .setPositiveButton("分かった", { dialog, which -> isWithFinish = true;homeDialog?.show() })
                            .create().show()
                })
                .create().apply {
                    setOnDismissListener { if (isWithFinish) finish() }//在dismiss的监听里执行Activity的finish，如果直接finish()或者把dismiss和finish同时执行，会闪屏
                    setOnShowListener {
                        homeDialog?.run {
                            //更改按钮颜色
                            getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#f4511e"))
                            getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#00b294"))
                            getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(Color.parseColor("#039be5"))
                        }
                    }
                    //如果界面没销毁，就显示
                    takeUnless { this@MainActivity.isFinishing }?.show()
                }

//        dialog.findViewById<TextView>(R.id.url)?.text = url

        //再获取文件名和大小
        async {
            val fileName = FilePropertyGetter.INSTANCE.getFileProperties(url).fileName
            val filesizeText = tranSizeText(FilePropertyGetter.INSTANCE.getFileProperties(url).fileSize)
            uiThread {
                homeDialog?.setTitle("ダウンロードしますか？")
                homeDialog?.findViewById<LinearLayout>(R.id.infoLayout)?.apply { visibility = View.VISIBLE }
                //显示文件名，大小
                arrayOf(R.id.fileNameTv, R.id.fileSizeTv).forEach {
                    when (it) {
                        R.id.fileNameTv -> homeDialog?.findViewById<EditText>(it)?.apply {
                            setText(fileName)
                            visibility = View.VISIBLE
                            text.toString().lastIndexOf(".").takeIf { it != -1 }?.let { setSelection(0,it) } ?: selectAll()
                        }
                        R.id.fileSizeTv -> homeDialog?.findViewById<TextView>(it)?.apply { text = filesizeText;visibility = View.VISIBLE }
                    }
                }
                homeDialog?.findViewById<EditText>(R.id.fileNameTv)?.requestFocus()
                showKeyboard(homeDialog?.findViewById(R.id.fileNameTv)!!)
                //隐藏进度
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
