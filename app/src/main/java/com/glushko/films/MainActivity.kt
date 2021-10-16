package com.glushko.films

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toDrawable

class MainActivity : AppCompatActivity() {

    private lateinit var cardViewFilm1: ConstraintLayout
    private lateinit var checkBoxLike1: CheckBox
    private lateinit var btnDetail1: ImageButton
    private lateinit var imgFilm1: ImageView
    private lateinit var tvFilm1: TextView
    private lateinit var tvComment1: TextView
    private lateinit var cardViewFilm2: ConstraintLayout
    private lateinit var checkBoxLike2: CheckBox
    private lateinit var btnDetail2: ImageButton
    private lateinit var imgFilm2: ImageView
    private lateinit var tvFilm2: TextView
    private lateinit var tvComment2: TextView

    companion object{
        const val EXTRA_FILM_NAME = "name"
        const val EXTRA_SAVE_STATE_FIRST = "save first"
        const val EXTRA_SAVE_STATE_SECOND = "save second"
        var whatIsClick: String? = null
    }

    private val startForResult1 = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == RESULT_OK){
            it.data?.let {values ->
                val comment = values.getStringExtra(DetailFilmActivity.EXTRA_COMMENT)
                val like = values.getBooleanExtra(DetailFilmActivity.EXTRA_LIKE, false)
                tvComment1.text = comment
                checkBoxLike1.isChecked = like
            }
        }
    }
    private val startForResult2 = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == RESULT_OK){
            it.data?.let {values ->
                val comment = values.getStringExtra(DetailFilmActivity.EXTRA_COMMENT)
                val like = values.getBooleanExtra(DetailFilmActivity.EXTRA_LIKE, false)
                tvComment2.text = comment
                checkBoxLike2.isChecked = like
            }
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        cardViewFilm1 = findViewById(R.id.cardViewFilm1)
        imgFilm1 = findViewById(R.id.imageFilm1)
        checkBoxLike1 = findViewById(R.id.checkBoxLike1)
        tvFilm1 = findViewById(R.id.tvFilm1)
        tvComment1 = findViewById(R.id.tvComment1)
        btnDetail1 = findViewById(R.id.btnDetail1)
        btnDetail1.setOnClickListener {
            whatIsClick = "first"
            val filmName = tvFilm1.text.toString()
            val comment = tvComment1.text.toString()
            val like = checkBoxLike1.isChecked
            cardViewFilm1.setBackgroundColor(resources.getColor(R.color.background_film_card))
            cardViewFilm2.setBackgroundColor(Color.WHITE)
            startForResult1.launch(Intent(this, DetailFilmActivity::class.java).apply {
                putExtra(EXTRA_FILM_NAME, filmName)
                putExtra(DetailFilmActivity.EXTRA_COMMENT, comment)
                putExtra(DetailFilmActivity.EXTRA_LIKE, like)
            })

        }
        cardViewFilm2 = findViewById(R.id.cardViewFilm2)
        imgFilm2 = findViewById(R.id.imageFilm2)
        checkBoxLike2 = findViewById(R.id.checkBoxLike2)
        tvFilm2 = findViewById(R.id.tvFilm2)
        tvComment2 = findViewById(R.id.tvComment2)
        btnDetail2 = findViewById(R.id.btnDetail2)
        btnDetail2.setOnClickListener {
            whatIsClick = "second"
            val filmName = tvFilm2.text.toString()
            val comment = tvComment2.text.toString()
            val like = checkBoxLike2.isChecked
            cardViewFilm1.setBackgroundColor(Color.WHITE)
            cardViewFilm2.setBackgroundColor(resources.getColor(R.color.background_film_card))
            startForResult2.launch(Intent(this, DetailFilmActivity::class.java).apply {
                putExtra(EXTRA_FILM_NAME, filmName)
                putExtra(DetailFilmActivity.EXTRA_COMMENT, comment)
                putExtra(DetailFilmActivity.EXTRA_LIKE, like)
            })

        }



    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val filmFirst = savedInstanceState.getParcelable<AboutFilm>(EXTRA_SAVE_STATE_FIRST)
        val filmSecond = savedInstanceState.getParcelable<AboutFilm>(EXTRA_SAVE_STATE_SECOND)
        Log.d("App", "whatIsClick = $whatIsClick \n $filmFirst \n $filmSecond" )
        filmFirst?.let {
            tvComment1.text = it.comment
            checkBoxLike1.isChecked = it.like
            cardViewFilm1.setBackgroundColor(
                when(it.color){
                    "first" -> resources.getColor(R.color.background_film_card)
                    "second" -> Color.WHITE
                    else -> Color.WHITE
                }
            )
        }
        filmSecond?.let {
            tvComment2.text = it.comment
            checkBoxLike2.isChecked = it.like
            cardViewFilm2.setBackgroundColor(
                when(it.color){
                    "first" -> Color.WHITE
                    "second" -> resources.getColor(R.color.background_film_card)
                    else -> Color.WHITE
                }
            )
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val filmFirst = AboutFilm(name = tvFilm1.text.toString(), comment = tvComment1.text.toString(), like = checkBoxLike1.isChecked, color = whatIsClick?:"")
        val filmSecond = AboutFilm(name = tvFilm2.text.toString(), comment = tvComment2.text.toString(), like = checkBoxLike2.isChecked,color = whatIsClick?:"")
        outState.putParcelable(EXTRA_SAVE_STATE_FIRST, filmFirst)
        outState.putParcelable(EXTRA_SAVE_STATE_SECOND, filmSecond)

    }
}