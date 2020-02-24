package com.hyesun.memoapp.callback

import androidx.recyclerview.widget.DiffUtil

class ImageDiffCallback(
    private val oldMemoList: List<String>,
    private val newMemoList: List<String>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldMemoList.size

    override fun getNewListSize() = newMemoList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        (oldMemoList[oldItemPosition] == newMemoList[newItemPosition])

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldMemoList[oldItemPosition] == newMemoList[newItemPosition]
}