package tools.mo3ta.kgallery.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import androidx.core.graphics.drawable.toBitmap
import java.math.BigInteger
import java.security.MessageDigest


object Utils {
    fun ellipsize(text: String, maxLength:Int = 80): String {
        return if (text.length > maxLength) {
            text.substring(0, maxLength) + "..."
        } else {
            text
        }
    }

    fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    fun md5(input:String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }

    /**
     * Get dominant color from bitmap
     */
    fun getDominantColorSimple(drawable: Drawable): Int {
        val bitmap = drawable.toBitmap()
        val newBitmap = Bitmap.createScaledBitmap(bitmap, 1, 1, true)
        val color = newBitmap.getPixel(0, 0)
        newBitmap.recycle()
        return color
    }

    /**
     * Get dominant color from bitmap
     */
    fun getDominantColor(drawable: Drawable): Int {
        val bitmap = drawable.toBitmap()
        val width = bitmap.width
        val height = bitmap.height
        val size = width * height
        val pixels = IntArray(size)
        val mappedBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, false);
        mappedBitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        val filteredPixels = pixels.filter { Color.alpha(it) > 0 }
        val (red, green, blue) = filteredPixels.fold(Triple(0, 0, 0)) { acc, i ->
            Triple(
                acc.first + Color.red(i),
                acc.second + Color.green(i),
                acc.third + Color.blue(i)
            )
        }
        val count = filteredPixels.count()
        val finalRed = (red / count) shl 16 and 0x00FF0000
        val finalGreen = (green / count) shl 8 and 0x0000FF00
        val finalBlue = (blue / count) and 0x000000FF
        return -0x1000000 or finalRed or finalGreen or finalBlue
    }




}