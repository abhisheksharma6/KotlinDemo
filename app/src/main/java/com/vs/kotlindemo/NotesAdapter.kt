package com.vs.kotlindemo

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class NotesAdapter : BaseAdapter {

    private var notesList = ArrayList<Note>()
    private var context: Context? = null

    constructor(context: Context, notesList: ArrayList<Note>) : super() {
        this.context = context
        this.notesList = notesList
    }


    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View? {
        val view: View?
        val vh: ViewHolder

        if(p1 == null){
            view = LayoutInflater.from(context).inflate(R.layout.note, p2, false)
            vh = ViewHolder(view)
            view.tag = vh
            Log.i("JSA", "set Tag for ViewHolder, position: " + p0)
        }else {
            view = p1
            vh = view.tag as ViewHolder
        }
        vh.tvTitle.text = notesList[p0].title
        vh.tvContent.text = notesList[p0].content

        return view
    }

    override fun getItem(p0: Int): Any {
       return notesList[p0]
    }

    override fun getItemId(p0: Int): Long {
       return p0.toLong()
    }

    override fun getCount(): Int {
        return notesList.size
    }

    private class ViewHolder(view: View?) {

         val tvTitle: TextView
         val tvContent: TextView

        init {
            this.tvTitle = view?.findViewById(R.id.tvTitle) as TextView
            this.tvContent = view?.findViewById(R.id.tvContent) as TextView
        }

    }

}