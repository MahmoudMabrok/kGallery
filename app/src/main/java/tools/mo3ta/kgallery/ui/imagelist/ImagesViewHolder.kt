package tools.mo3ta.kgallery.ui.imagelist

import android.provider.SyncStateContract.Helpers
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import tools.mo3ta.kgallery.R
import tools.mo3ta.kgallery.data.local.ImageLocalItem
import tools.mo3ta.kgallery.model.ImageItem
import tools.mo3ta.kgallery.ui.Utils

class ImagesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val imageView: ImageView by lazy { itemView.findViewById(R.id.imageView)}
    private val tvCaption: TextView by lazy { itemView.findViewById(R.id.tvCaption)}

    companion object{
        fun create(parent: ViewGroup): ImagesViewHolder {
            return ImagesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false))
        }
    }

    fun bind(item: ImageLocalItem) {
        imageView.load(item.uri)

        val text =  item.caption.ifEmpty { tvCaption.context.getText(R.string.no_caption) }
        tvCaption.text = Utils.ellipsize(text.toString())
    }
}