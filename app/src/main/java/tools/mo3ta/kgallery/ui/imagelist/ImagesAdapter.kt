package tools.mo3ta.kgallery.ui.imagelist

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tools.mo3ta.kgallery.model.ImageItem

class ImagesAdapter(private var images: MutableList<ImageItem> = mutableListOf()) :
  RecyclerView.Adapter<ImagesViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesViewHolder {
    Log.d("TestTest", "onCreateViewHolder:")
    return ImagesViewHolder.create(parent)
  }


  override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {
    val rowItem = images[position]
    holder.bind(rowItem)
    Log.d("aaa", "onBindViewHolder: ")
  }

  override fun getItemCount(): Int {
    Log.d("TestTest", "ImagesAdapter getItemCount: ${images.size}")
    return images.size
  }

  fun update(data: List<ImageItem>) {
      images.clear()
      images.addAll(data)
      notifyDataSetChanged()
  }

}
