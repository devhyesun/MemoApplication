package com.hyesun.memoapp.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hyesun.memoapp.R
import com.hyesun.memoapp.adapter.MemoAdapter
import com.hyesun.memoapp.db.model.Memo
import com.hyesun.memoapp.util.InjectorUtils
import com.hyesun.memoapp.viewmodel.MemoViewModel

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {
    private val memoViewModel by lazy {
        ViewModelProvider(this, InjectorUtils.provideMemoViewModelFactory(this))
            .get(MemoViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val adapter = MemoAdapter(this)

        rvMemoList.layoutManager = LinearLayoutManager(this)
        rvMemoList.adapter = adapter

        memoViewModel.allMemoList.observe(this,
            Observer<List<Memo>> { memoList ->
                adapter.setMemoList(memoList)
            })

        fab.setOnClickListener {
            startActivity(Intent(this, NewMemoActivity::class.java))
        }
    }

}

