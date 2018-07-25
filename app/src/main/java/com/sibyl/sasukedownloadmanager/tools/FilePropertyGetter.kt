package com.sibyl.sasukedownloadmanager.tools

import java.io.IOException
import java.io.UnsupportedEncodingException
import java.net.MalformedURLException
import java.net.URL
import java.net.URLConnection

/**
 * @author Sasuke on 2018/7/20.
 * 通过url获取要下载的文件名名字
 */
class  FilePropertyGetter {
    companion object {
        val INSTANCE: FilePropertyGetter by lazy { FilePropertyGetter() }
    }
    data class Properties(var fileName: String = "", var fileSize: Long = 0)


    fun getFileProperties(url: String?): Properties {
        if(url.isNullOrEmpty()) return Properties()
        var filename = ""
        var isok = false
        var conn: URLConnection? = null
        // 从UrlConnection中获取文件名称
        try {
            conn = URL(url).openConnection()
            if (conn == null) {
                return Properties("未知", 0)
            }
            val hf: Map<String, List<String>> = conn.getHeaderFields()
            if (hf == null) {
                return Properties("未知", conn?.contentLengthLong)
            }
            val keys: Set<String> = hf.keys
            if (keys == null) {
                return Properties("未知", conn?.contentLengthLong)
            }

            for (key in keys) {
                hf.get(key)?.forEach { value ->
                    try {
                        var result = String(value.toByteArray(Charsets.ISO_8859_1))
                        result.indexOf("filename").takeIf { it >= 0 }?.let {
                            filename = result.split(";").filter { it.contains("filename") }[0]
                                    .replace("filename","")
                                    .replace("=","")
                                    .replace("\"","")
                                    .replace("_apkpure.com","")
                            isok = true
                        }
                    } catch (e: UnsupportedEncodingException) {
                        e.printStackTrace()
                    }// ISO-8859-1 UTF-8 gb2312
                }
                if (isok) {
                    break
                }
            }
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        //如果上面的没获取成功，那就补救一下，用直接截取“/”后面的字符串来获取。
        if(filename.isNullOrEmpty()){
            filename = url?.substring(url.lastIndexOf("/") + 1) ?: ""
        }
        //%20转义为_下划线吧
        filename = filename.replace("%20","_")
        //有些带后缀的竟然后面还加了好多乱码
        arrayOf(".mp4",".mp3",".apk",".pdf",".mkv",".torrent",".jpg",".jpeg",".bmp",".gif",".png").forEach { suffix ->
            filename.takeIf { suffix in it}?.let { filename = it.split(suffix)[0] + suffix }
        }


        return Properties(filename, conn?.contentLengthLong?: 0)
    }



}