package com.example.quotesapp.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.quotesapp.R
import com.example.quotesapp.data.model.Quotes
import com.example.quotesapp.utils.show


class QuotesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    companion object
    {
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_LOADING = 1
    }

    private var newsList = ArrayList<Quotes.QuotesBO>()




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    {
        return if (viewType == VIEW_TYPE_ITEM)
        {
            val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_item_quotes, parent, false)
            MovieViewHolder(view)
        }
        else
        {
            val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_item_loading, parent, false)
            LoadingViewHolder(view)
        }
    }

    class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        private val imagePoster: ImageView = view.findViewById(R.id.image_poster)
        private val textTitle: TextView = view.findViewById(R.id.text_title)



        @SuppressLint("SetTextI18n")
        fun bindItems(news: Quotes.QuotesBO)
        {
         /*   textTitle.text = news.news.forEach{
                it.name
            }.toString()
*/

textTitle.text = news.content
            Glide.with(imagePoster.context).load(news.imageUrl)
                .centerCrop()
                .thumbnail(0.5f)
                .placeholder(R.drawable.ic_launcher_background)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imagePoster)
        }
    }

    override fun getItemCount(): Int
    {
        return newsList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int)
    {
        if (holder is MovieViewHolder)
            if(newsList[position] != null )
                holder.bindItems(newsList[position]!!)
            else if (holder is LoadingViewHolder)
                holder.showLoadingView()
    }

    override fun getItemViewType(position: Int): Int
    {
        return if (newsList[position] == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    fun setData(newNewsList: ArrayList<Quotes.QuotesBO>)
    {
        if (newNewsList != null)
        {
            if (newsList.isNotEmpty())
                newsList.removeAt(newsList.size - 1)
            newsList.clear()
            newsList.addAll(newNewsList)
        }
        else
            newsList.add(newNewsList)
        notifyDataSetChanged()
    }

    fun getData() = newsList

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        private val progressBar: ProgressBar = itemView.findViewById(R.id.progress_bar)

        fun showLoadingView()
        {
          progressBar.show()
        }
    }
}