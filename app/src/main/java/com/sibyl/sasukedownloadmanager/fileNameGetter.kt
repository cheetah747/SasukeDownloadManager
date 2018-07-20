package com.sibyl.sasukedownloadmanager

import java.io.IOException
import java.io.UnsupportedEncodingException
import java.net.MalformedURLException
import java.net.URL

/**
 * @author Sasuke on 2018/7/20.
 * 通过url获取要下载的文件名名字
 */
class fileNameGetter {

    fun getName(url: String): String {
        var filename = "";
        var isok = false;
        // 从UrlConnection中获取文件名称
        try {
            val myURL = URL(url);

            val conn = myURL.openConnection()
            if (conn == null) {
                return "未知"
            }
            val hf: Map<String, List<String>> = conn.getHeaderFields()
            if (hf == null) {
                return ""
            }
            val keys: Set<String> = hf.keys
            if (keys == null) {
                return ""
            }

            for (key in keys) {
                hf.get(key)?.forEach { value ->
                    try {
                        var result = String(value.toByteArray(Charsets.ISO_8859_1))
                        result.indexOf("filename").takeIf { it >= 0 }?.let {
                            filename = result.substring(it + "filename".length).substring(result.indexOf("=") + 1)
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
        filename.takeIf { it.isNullOrEmpty() }?.let {
            filename = url.substring(url.lastIndexOf("/") + 1)
        }
        return filename
    }


}