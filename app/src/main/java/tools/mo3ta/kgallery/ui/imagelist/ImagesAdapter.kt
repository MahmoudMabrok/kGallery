package tools.mo3ta.kgallery.ui.imagelist

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tools.mo3ta.kgallery.data.local.ImageLocalItem
import tools.mo3ta.kgallery.model.ImageItem

class ImagesAdapter(private var images: MutableList<ImageLocalItem> = mutableListOf()) :
  RecyclerView.Adapter<ImagesViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesViewHolder {
    return ImagesViewHolder.create(parent)
  }


  override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {
    val rowItem = images[position]
    holder.bind(rowItem)
   }

  override fun getItemCount(): Int {
    return images.size
  }

  fun update(data: List<ImageLocalItem>) {
      images.clear()
      images.addAll(data)
      notifyDataSetChanged()
  }

}
