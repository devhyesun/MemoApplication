package com.hyesun.memoapp.callback

import androidx.recyclerview.widget.DiffUtil
import com.hyesun.memoapp.db.model.Memo

class MemoDiffCallback(
    private val oldMemoList: List<Memo>,
    private val newMemoList: List<Memo>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldMemoList.size

    override fun getNewListSize() = newMemoList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        (oldMemoList[oldItemPosition].id == newMemoList[newItemPosition].id)

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldMemoList[oldItemPosition] == newMemoList[newItemPosition]
}