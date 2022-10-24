package com.example.quotesapp.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.quotesapp.data.model.Quotes
import com.example.quotesapp.ui.repositories.QuotesListRepository
import com.example.quotesapp.utils.ApiException
import com.example.quotesapp.utils.NoInternetException
import com.example.quotesapp.utils.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class QuotesListViewModel(application: Application) : AndroidViewModel(application)
{

    private var newsList = ArrayList<Quotes.QuotesBO>()
    private val repository = QuotesListRepository.getInstance(application)
    var number:Int =0
    private val _newsLiveData = MutableLiveData<State<ArrayList<Quotes.QuotesBO>>>()
    val newsLiveData: LiveData<State<ArrayList<Quotes.QuotesBO>>>
        get() = _newsLiveData



    private lateinit var newsResponse: Quotes




    fun getNews()
    {

            newsList.clear()
            _newsLiveData.postValue(State.loading())

        viewModelScope.launch(Dispatchers.IO)
        {
            Log.d("MovieNameee","${_newsLiveData.value}")


                try {

                    var quotesList = repository.getNews()
                    if(quotesList!=null && quotesList.size>0)
                    {
                        _newsLiveData.postValue(State.success(quotesList as ArrayList<Quotes.QuotesBO>))
                    }
                    else {
                        newsResponse = repository.getNewsRemote(

                        )
                        withContext(Dispatchers.Main)
                        {
                            if (newsResponse != null) {
                                newsList.addAll(newsResponse.results)


                                _newsLiveData.postValue(State.success(newsList))
                            } else
                                _newsLiveData.postValue(State.error("Error"))
                        }
                        newsResponse.results.forEach {

                            it.imageUrl = getRandomImage()

                            repository.insertQuote(it)

                        }
                        newsList.forEach {
                            it.imageUrl = getRandomImage()
                        }
                        _newsLiveData.postValue(State.success(newsList))
                    }
                }
                catch (e: ApiException)
                {
                    withContext(Dispatchers.Main)
                    {
                        _newsLiveData.postValue(State.error(e.message!!))

                    }
                }
                catch (e: NoInternetException)
                {
                    withContext(Dispatchers.Main)
                    {
                        _newsLiveData.postValue(State.error(e.message!!))

                    }
                }

        }
    }


    private fun getRandomImage(): String {

        var Random_Image= arrayOf<String>(
            "https://as1.ftcdn.net/v2/jpg/02/43/34/20/1000_F_243342003_4WawRfK5LVVMj5u0kxKImNrFtylVNa6I.jpg",
            "https://as2.ftcdn.net/v2/jpg/01/38/85/41/1000_F_138854138_SJE0ORwUcDGgyhHboytXaR8ydWHbgswz.jpg",
            "https://as1.ftcdn.net/v2/jpg/01/81/95/32/1000_F_181953234_ubmWHXayfcuLnQkEw3XIcfwwdgAxn8DY.jpg",
            "https://as1.ftcdn.net/v2/jpg/02/84/59/72/1000_F_284597294_e3dUZ2pQpo2XaXuUHwnlDqVaq2IN5jBZ.jpg",
            "https://as1.ftcdn.net/v2/jpg/00/84/05/02/1000_F_84050276_AuNIYlsLxe79n0m64XNacjP2hMx9kiHl.jpg",
            "https://as1.ftcdn.net/v2/jpg/01/93/15/02/1000_F_193150282_DOjh3VoWeyhsXYn1rg1F0bcBkDHmErtI.jpg",
            "https://t3.ftcdn.net/jpg/02/27/96/36/240_F_227963625_Mp5TfBYjpTScthgWZKWj7qTbI0WPhQyu.jpg",
            "https://t3.ftcdn.net/jpg/02/35/29/24/240_F_235292457_As8nGLCTVbxVxU1zLeGnrIxb2fWGDuME.jpg",
            "https://t3.ftcdn.net/jpg/04/20/55/42/240_F_420554282_a5NFifSllcQig2k8rvnhrERWI1Ok7meY.jpg",
            "https://as2.ftcdn.net/v2/jpg/02/75/71/89/1000_F_275718943_ORKB0mXTEoa3YuKB0Ng8hBJPmB5JXxFl.jpg"


            )
        // var num = (0..Random_Color.size-1).random()
        if(number<9) {
            number += 1
        }
        else
        {

            number=0
        }
        return Random_Image[number]

    }

}
