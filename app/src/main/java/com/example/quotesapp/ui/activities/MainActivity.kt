package com.example.quotesapp.ui.activities

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quotesapp.R
import com.example.quotesapp.databinding.ActivityMainBinding
import com.example.quotesapp.ui.adapters.QuotesAdapter
import com.example.quotesapp.ui.viewmodels.QuotesListViewModel
import com.example.quotesapp.utils.*

class MainActivity : AppCompatActivity() {

    companion object
    {
        const val ANIMATION_DURATION = 1000.toLong()
    }
    private lateinit var newsAdapter: QuotesAdapter
    private lateinit var viewModel :QuotesListViewModel
    //private val viewModel by viewModels<MovieListViewModel>()
    private var dataBind : ActivityMainBinding?  = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBind = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this)[QuotesListViewModel::class.java]

        setupUI()
        handleNetworkChanges()
        setupAPICall()
    }


    private fun setupUI()
    {
        newsAdapter = QuotesAdapter()
        dataBind?.recyclerViewMovies?.apply{
            layoutManager = LinearLayoutManager(context)
            itemAnimator = DefaultItemAnimator()
            adapter = newsAdapter
            // This a simple divider between each list item in the RecyclerView
            this.addItemDecoration(
                DividerItemDecoration(
                    this.context,
                    (this.layoutManager as LinearLayoutManager).orientation
                )
            )
            addOnItemTouchListener(
                RecyclerItemClickListener(
                    context,
                    object : RecyclerItemClickListener.OnItemClickListener {
                        override fun onItemClick(view: View, position: Int) {
                            if (newsAdapter.getData().isNotEmpty()) {
                                val selectQuote = newsAdapter.getData()[position]
                                selectQuote?.let {
                                   val intent =
                                        Intent(
                                            applicationContext,
                                            QuoteDetailsActivity::class.java
                                        )
                                    intent.putExtra("quoteID",it._id)
                                    startActivity(intent)
                                }
                            }
                        }
                    })
            )

        }


    }



    private fun setupAPICall()
    {
        viewModel.newsLiveData.observe(this, Observer { state ->
            when (state)
            {
                is State.Loading ->
                {
                    Toast.makeText(this,"loading", Toast.LENGTH_LONG)
                    dataBind?.recyclerViewMovies?.hide()
                    // dataBind.linearLayoutSearch.hide()
                    dataBind?.progressBar?.show()
                }
                is State.Success ->
                {
                    Toast.makeText(this,"sucess", Toast.LENGTH_LONG)
                    dataBind?.recyclerViewMovies?.show()
                    //  dataBind.linearLayoutSearch.hide()
                    dataBind?.progressBar?.hide()
                    newsAdapter.setData(state.data)
                }
                is State.Error ->
                {
                    Toast.makeText(this,"error", Toast.LENGTH_LONG)
                    dataBind?.progressBar?.hide()
                    Toast.makeText(this, state.message,  Toast.LENGTH_SHORT).show()

                    //  showToast(state.message)
                }
                else -> {}
            }
        })



    }

    private fun handleNetworkChanges()
    {
        NetworkUtils.getNetworkLiveData(applicationContext).observe(this, Observer { isConnected ->
            if (!isConnected)
            {
                dataBind?.textViewNetworkStatus?.text = getString(R.string.text_no_connectivity)
                dataBind?.networkStatusLayout?.apply {
                    show()
                    setBackgroundColor(getColorRes(R.color.colorStatusNotConnected))
                }
            }
            else
            {
                if (viewModel.newsLiveData.value is State.Error || newsAdapter.itemCount == 0)
                    viewModel.getNews()

                dataBind?.textViewNetworkStatus?.text = getString(R.string.text_connectivity)
                dataBind?.networkStatusLayout?.apply {
                    setBackgroundColor(getColorRes(R.color.colorStatusConnected))

                    animate()
                        .alpha(1f)
                        .setStartDelay(ANIMATION_DURATION)
                        .setDuration(ANIMATION_DURATION)
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                hide()
                            }
                        })
                }
            }
        })
    }
}