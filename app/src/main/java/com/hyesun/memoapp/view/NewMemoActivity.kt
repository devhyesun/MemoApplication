package com.hyesun.memoapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.hyesun.memoapp.R
import com.hyesun.memoapp.db.model.Memo
import com.hyesun.memoapp.util.InjectorUtils
import com.hyesun.memoapp.viewmodel.MemoViewModel
import kotlinx.android.synthetic.main.activity_new_memo.*

class NewMemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_memo)
    }
}
