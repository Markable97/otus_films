package com.glushko.films

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

class DetailFilmActivity : AppCompatActivity() {

    companion object{

        const val EXTRA_COMMENT = "comment"
        const val EXTRA_LIKE = "like"
    }

    private lateinit var editTextComment: EditText
    private lateinit var checkBoxLike: CheckBox
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_film)
        val filmName = intent.getStringExtra(MainActivity.EXTRA_FILM_NAME)
        val comment = intent.getStringExtra(EXTRA_COMMENT)
        val like = intent.getBooleanExtra(EXTRA_LIKE, false)
        val image = findViewById<ImageView>(R.id.imageDetail)
        image.setImageResource(getNameFilm(filmName?:""))
        editTextComment = findViewById(R.id.editTextComment)
        editTextComment.setText(comment?:"")
        checkBoxLike = findViewById(R.id.checkboxLikeSend)
        checkBoxLike.isChecked = like
        findViewById<TextView>(R.id.tvNameFilm).text = filmName
        findViewById<ImageButton>(R.id.btnSendComment).setOnClickListener {
            val newComment = editTextComment.text.toString()
            val newLike = checkBoxLike.isChecked
            setResult(RESULT_OK,Intent().apply {
                putExtra(EXTRA_COMMENT, newComment)
                putExtra(EXTRA_LIKE, newLike)
            })
            finish()
        }
        findViewById<Button>(R.id.btnInviteFriend).setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data = Uri.parse("mailto:")
                if(intent.resolveActivity(packageManager) != null){
                    startActivity(intent)
                }else {
                    Toast.makeText(this, "Не найдено приложение", Toast.LENGTH_LONG).show()
                }
            }catch (ex: ActivityNotFoundException){
                Toast.makeText(this, "Не найдено приложение", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun getNameFilm(filmName: String) =
        when(filmName){
            "Человек Паук" -> R.drawable.spider_man
            "Веном" -> R.drawable.venom
            else -> R.drawable.ic_launcher_background
        }
}