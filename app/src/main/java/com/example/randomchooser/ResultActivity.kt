package com.example.randomchooser

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_result.*
import java.lang.IllegalArgumentException

class ResultActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var elements: List<Element>
    private var chooseCount: Int = 0
    private lateinit var recyclerViewAdapter: ElementListRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val arrayData = intent.getSerializableExtra(getString(R.string.send_elements_list)) as ArrayList<*>
        elements = arrayData.filterIsInstance<Element>().toMutableList()
        chooseCount = intent.getIntExtra(getString(R.string.send_random_count), 0)

        val data: MutableList<Element> = chooseRandom(elements, chooseCount)
        recyclerViewAdapter = ElementListRecyclerViewAdapter(this, data)

        result_recyclerview.apply{
            adapter = recyclerViewAdapter
            layoutManager = LinearLayoutManager(this@ResultActivity)
            addItemDecoration(DividerItemDecoration(this@ResultActivity, DividerItemDecoration.VERTICAL))
        }

        result_retry.setOnClickListener(this)
    }

    private fun chooseRandom(data: List<Element>, chooseSize: Int) = data.shuffled().subList(0, chooseSize).toMutableList()

    override fun onClick(view: View) {
        when (view.id){
            R.id.result_retry -> {
                recyclerViewAdapter.data = chooseRandom(elements, chooseCount)
            }
        }
    }
}