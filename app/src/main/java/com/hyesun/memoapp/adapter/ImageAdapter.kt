package com.hyesun.memoapp.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.hyesun.memoapp.R
import com.hyesun.memoapp.listener.OnDeleteListener
import kotlinx.android.synthetic.main.row_picture.view.*

class ImageAdapter(private val context: Context,
                   private val isEditable: Boolean): RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {
    private var imagePathList = ArrayList<String>()
    private var onDeleteListener: OnDeleteListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_picture, parent, false)
        return ImageViewHolder(view)
    }

    override fun getItemCount() = imagePathList.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imagePath = imagePathList[position]

        with(holder) {
            Glide.with(context)
                .load(Uri.parse(imagePath))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(ivPicture)

            btnDelete.visibility = if (isEditable) {
                btnDelete.setOnClickListener {
                    onDeleteListener?.onDelete(position)
                }
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    fun setImagePathList(imagePathList: List<String>) {
        this.imagePathList.clear()
        this.imagePathList.addAll(imagePathList)

        notifyDataSetChanged()
    }

    fun setOnDeleteListener(listener: OnDeleteListener) {
        onDeleteListener = listener
    }

    class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivPicture = view.ivPicture
        val btnDelete = view.btnDelete
    }
}