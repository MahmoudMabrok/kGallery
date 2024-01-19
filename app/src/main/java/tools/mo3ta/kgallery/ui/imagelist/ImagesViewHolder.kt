package tools.mo3ta.kgallery.ui.imagelist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import tools.mo3ta.kgallery.R
import tools.mo3ta.kgallery.model.ImageItem

class ImagesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val imageView: ImageView by lazy { itemView.findViewById(R.id.imageView)}
    private val tvCaption: TextView by lazy { itemView.findViewById(R.id.tvCaption)}

    companion object{
        fun create(parent: ViewGroup): ImagesViewHolder {
            return ImagesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false))
        }
    }

    fun bind(item: ImageItem) {
        imageView.load(item.url)

       // tvCaption.text = if (item.caption.isEmpty()) tvCaption.context.getText(R.string.no_caption) else item.caption
        tvCaption.text = item.caption.ifEmpty { tvCaption.context.getText(R.string.no_caption) }
    }
}