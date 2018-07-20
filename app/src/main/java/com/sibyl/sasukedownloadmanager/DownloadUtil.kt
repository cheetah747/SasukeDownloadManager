package com.sibyl.sasukedownloadmanager

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import org.jetbrains.anko.async
import java.io.File

/**
 * @author Sasuke on 2018/7/20.
 */
class DownloadUtil {
    val downloadUrl = "https://oalxfnrvo.qnssl.com/V4.5.0_ShengYiGuanJia180717.apk"

    fun download(context: Context) {
        async {
            val fileName = FileNameGetter().getName(downloadUrl)
            // 创建下载请求
            val request: DownloadManager.Request = DownloadManager.Request(Uri.parse(downloadUrl)).apply {
                //VISIBILITY_VISIBLE:                   下载过程中可见, 下载完后自动消失 (默认)
                // VISIBILITY_VISIBLE_NOTIFY_COMPLETED:  下载过程中和下载完成后均可见
                // VISIBILITY_HIDDEN:                    始终不显示通知
                setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                setTitle(fileName)
                setDescription("このファイルについて。。。")
                setDestinationUri(Uri.fromFile(File("${Environment.getExternalStorageDirectory()}/Download", fileName)))
                // 获取下载管理器服务的实例, 添加下载任务，并返回一个id
                (context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager).enqueue(this)

            }
        }
    }


/*
 * 设置允许使用的网络类型, 可选值:
 *     NETWORK_MOBILE:      移动网络
 *     NETWORK_WIFI:        WIFI网络
 *     NETWORK_BLUETOOTH:   蓝牙网络
 * 默认为所有网络都允许
 */
// request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);

// 添加请求头
// request.addRequestHeader("User-Agent", "Chrome Mozilla/5.0");

// 设置下载文件的保存位置
//    File saveFile = new File(Environment.getExternalStorageDirectory(), "demo.apk");
//    request.setDestinationUri(Uri.fromFile(saveFile));

/*
 * 2. 获取下载管理器服务的实例, 添加下载任务
// */
//    DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
//
//// 将下载请求加入下载队列, 返回一个下载ID
//    long downloadId = manager.enqueue(request);

// 如果中途想取消下载, 可以调用remove方法, 根据返回的下载ID取消下载, 取消下载后下载保存的文件将被删除
// manager.remove(downloadId);
}