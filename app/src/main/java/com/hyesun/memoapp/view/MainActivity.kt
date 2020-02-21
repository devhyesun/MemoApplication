package com.hyesun.memoapp.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
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
    val memoViewModel by lazy {
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}

