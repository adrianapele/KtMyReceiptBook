package com.example.ktmyreceiptbook.adapter

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MyRecyclerView(context: Context, attributeSet: AttributeSet, defStyle: Int):
    RecyclerView(context, attributeSet, defStyle)
{
    lateinit var view: View

    fun setEmptyView(view: View)
    {
        this.view = view;
        setViewsVisibility()
    }

    fun setViewsVisibility()
    {
        var hasAdapterData = adapter?.itemCount == 0
        if (hasAdapterData)
        {
            view.visibility = View.VISIBLE
            this.visibility = View.GONE
        }
        else
        {
            view.visibility = View.GONE
            this.visibility = View.VISIBLE
        }
    }

    override fun setAdapter(adapter: Adapter<*>?)
    {
        val oldAdapter = getAdapter()
        super.setAdapter(adapter)

        oldAdapter?.unregisterAdapterDataObserver(observer)
        adapter?.registerAdapterDataObserver(observer)
    }

    val observer = object: AdapterDataObserver()
    {
        override fun onChanged()
        {
            super.onChanged()
            setViewsVisibility()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int)
        {
            super.onItemRangeInserted(positionStart, itemCount)
            setViewsVisibility()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int)
        {
            super.onItemRangeRemoved(positionStart, itemCount)
            setViewsVisibility()
        }
    }
}