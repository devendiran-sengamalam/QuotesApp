package com.example.quotesapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey


data class Quotes (
    val count: Int,
    val lastItemIndex: Int,
    val page: Int,
    val results: List<QuotesBO>,
    val totalCount: Int,
    val totalPages: Int
   /* @SerializedName("status"  ) var status  : String?            = null,
    @SerializedName("sources" ) var news : ArrayList<NewsBO> = arrayListOf()*/

)
{
    @Entity(tableName = "quotes_table")
    data class QuotesBO(
        @PrimaryKey
        val _id: String,
        val author: String,
        val authorSlug: String,
        val content: String,
        val dateAdded: String,
        val dateModified: String,
        val length: Int,
        var imageUrl : String
       // val tags: List<String>
        /* @SerializedName("id"          ) var id          : String? = null,
         @SerializedName("name"        ) var name        : String? = null,
         @SerializedName("description" ) var description : String? = null,
         @SerializedName("url"         ) var url         : String? = null,
         @SerializedName("category"    ) var category    : String? = null,
         @SerializedName("language"    ) var language    : String? = null,
         @SerializedName("country"     ) var country     : String? = null*/

    )
}
