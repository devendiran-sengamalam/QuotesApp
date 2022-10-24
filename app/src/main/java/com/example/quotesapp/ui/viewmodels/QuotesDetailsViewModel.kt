package com.example.quotesapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.quotesapp.data.model.Quotes
import com.example.quotesapp.ui.repositories.QuotesListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QuotesDetailsViewModel(application: Application): AndroidViewModel(application) {

    //var quotesDetails: News.NewsBO? = null
    private val _quotesDetails = MutableLiveData<Quotes.QuotesBO>()
    val quotesDetails: LiveData<Quotes.QuotesBO>
        get() = _quotesDetails

    private val repository = QuotesListRepository.getInstance(application)

    fun getDetails(id: String?) {


        viewModelScope.launch(Dispatchers.IO)
        {
            if (id != null) {
                _quotesDetails.postValue(repository.getQuoteByIdLiveData(quoteId = id))
            }
        }

    }
}