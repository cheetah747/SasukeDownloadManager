package com.sibyl.sasukedownloadmanager

import java.math.BigDecimal

/**
 * @author Sasuke on 2018/7/20.
 */
    /**
     * 自动转换文件大小显示
     */
   fun tranSizeText(size: Long): String {
        // 如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        var value = size.toDouble()
        if (value < 1024) {
            return value.toString() + "B"
        } else {
            value = BigDecimal(value / 1024).setScale(2, BigDecimal.ROUND_DOWN).toDouble()
        }
        // 如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
        // 因为还没有到达要使用另一个单位的时候
        // 接下去以此类推
        if (value < 1024) {
            return value.toString() + "KB"
        } else {
            value = BigDecimal(value / 1024).setScale(2, BigDecimal.ROUND_DOWN).toDouble()
        }
        if (value < 1024) {
            return value.toString() + "MB"
        } else {
            // 否则如果要以GB为单位的，先除于1024再作同样的处理
            value = BigDecimal(value / 1024).setScale(2, BigDecimal.ROUND_DOWN).toDouble()
            return value.toString() + "GB"
        }
    }