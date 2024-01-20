package tools.mo3ta.kgallery.ui

object Utils {
    fun ellipsize(text: String, maxLength:Int = 80): String {
        return if (text.length > maxLength) {
            text.substring(0, maxLength) + "..."
        } else {
            text
        }
    }
}