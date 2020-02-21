package com.hyesun.memoapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hyesun.memoapp.R
import com.hyesun.memoapp.db.model.Memo
import com.hyesun.memoapp.util.InjectorUtils
import com.hyesun.memoapp.viewmodel.ImageViewModel
import kotlinx.android.synthetic.main.row_memo.view.*

class MemoAdapter(private val context: Context) :
    RecyclerView.Adapter<MemoAdapter.MemoViewHolder>() {
    private var memoList = ArrayList<Memo>()
    private var imageViewModel: ImageViewModel? = null

    init {
        imageViewModel = ViewModelProvider(
            context as AppCompatActivity,
            InjectorUtils.provideImageViewModelFactory(context)
        )
            .get(ImageViewModel::class.java)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_memo, parent, false)

        return MemoViewHolder(view)
    }

    override fun getItemCount() = memoList.size

    override fun onBindViewHolder(holder: MemoViewHolder, position: Int) {
        val memo = memoList[position]
        val thumbnail = imageViewModel?.thumbnailImage(memo.id)

        with(holder) {
            Glide.with(context)
                .load(thumbnail)
                .into(ivThumbnail)

            tvTitle.text = memo.title
            tvContent.text = memo.content
        }
    }

    fun setMemoList(memoList: List<Memo>) {
        this.memoList.clear()
        this.memoList.addAll(memoList)
        notifyDataSetChanged()
    }

    class MemoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivThumbnail: ImageView = view.ivThumbnail
        val tvTitle: TextView = view.tvTitle
        val tvContent: TextView = view.tvContent
    }
}