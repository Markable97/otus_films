package com.glushko.films.data_layer.datasource.response

import com.glushko.films.business_logic_layer.domain.AboutFilm





data class ResponseFilm(
    val pagesCount: Int, //Api не достоверную инфу предоставляет, по факту не 13 элементов, а 20
    var isSuccess: Boolean,
    var isUpdateDB: Boolean,
    var page: Int,
    var films: List<AboutFilm> = listOf(),
    var err: Int
){
    companion object{
        const val PAGE_COUNT = 20
        const val ERROR_NETWORK = -100
        const val ERROR_SERVER_TOKEN = -50
        const val ERROR_SERVER_TIME_LIMIT = -51
        const val ERROR_UNKNOWN = -999
        const val ERROR_NO = 0

    }
}
