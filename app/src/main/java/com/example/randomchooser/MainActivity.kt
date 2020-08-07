package com.example.randomchooser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_element_list.*
import java.io.Serializable

class MainActivity : AppCompatActivity(), View.OnClickListener, AddElementDialogFragment.AddElementDialogListener, ElementListRecyclerViewAdapter.DataSizeChanged {
    private var randomCount = 1
    private lateinit var recyclerViewAdapter: ElementListRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_element_list)

        val data = mutableListOf<Element>(Element("hello"), Element("bye"))
        recyclerViewAdapter = ElementListRecyclerViewAdapter(this, data, this)

        element_list_recylerview.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = recyclerViewAdapter
            addItemDecoration(DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL))
        }

        element_list_plus_button.setOnClickListener(this)
        element_list_minus_button.setOnClickListener(this)
        element_list_add_button.setOnClickListener(this)
        element_list_edit_button.setOnClickListener(this)
        element_list_choose_random.setOnClickListener(this)

        element_list_random_count.text = randomCount.toString()
    }

    override fun onClick(view: View) {
        when (view.id){
            R.id.element_list_plus_button -> {
                if (randomCount < recyclerViewAdapter.itemCount)
                randomCount++
                element_list_random_count.text = randomCount.toString()
            }
            R.id.element_list_minus_button -> {
                if (randomCount > 1)
                    randomCount--

                element_list_random_count.text = randomCount.toString()
            }
            R.id.element_list_add_button -> {
                val dialog = AddElementDialogFragment()
                dialog.show(supportFragmentManager, "AddElementDialogFragment")
            }
            R.id.element_list_edit_button -> recyclerViewAdapter.toggleEditMode()
            R.id.element_list_choose_random -> {
                val intent = Intent(this, ResultActivity::class.java)
                val args = Bundle()
                args.putSerializable(getString(R.string.send_elements_list), ArrayList(recyclerViewAdapter.data) as Serializable)
                intent.putExtra(getString(R.string.intent_bundle), args)
                intent.putExtra(getString(R.string.send_random_count), randomCount)
                startActivity(intent)
            }
        }
    }

    override fun onDialogPositive(element: Element) {
        recyclerViewAdapter.addElement(element)
    }

    override fun onDialogNegative() {
        Toast.makeText(this, "add element cancel", Toast.LENGTH_SHORT).show()
    }

    override fun onDialogModify(element: Element, position: Int) {
        recyclerViewAdapter.modifyElement(element, position)
    }

    override fun onDataSizeChanged(newSize: Int) {
        if (randomCount > newSize)
            randomCount = newSize
    }
}