package com.glushko.films

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

class DetailFilmActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_FILM_NAME = "name"
        const val EXTRA_COMMENT = "comment"
        const val EXTRA_LIKE = "like"
    }

    private lateinit var editTextComment: EditText
    private lateinit var checkBoxLike: CheckBox
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_film)
        val filmName = intent.getStringExtra(EXTRA_FILM_NAME)
        val comment = intent.getStringExtra(EXTRA_COMMENT)
        val like = intent.getBooleanExtra(EXTRA_LIKE, false)
        val image = findViewById<ImageView>(R.id.imageDetail)
        image.setImageResource(FilmImageProvider.getNameFilm(filmName ?: ""))
        editTextComment = findViewById(R.id.editTextComment)
        editTextComment.setText(comment ?: "")
        checkBoxLike = findViewById(R.id.checkboxLikeSend)
        checkBoxLike.isChecked = like
        findViewById<TextView>(R.id.tvNameFilm).text = filmName
        findViewById<ImageButton>(R.id.btnSendComment).setOnClickListener {
            val newComment = editTextComment.text.toString()
            val newLike = checkBoxLike.isChecked
            setResult(RESULT_OK, Intent().apply {
                putExtra(EXTRA_COMMENT, newComment)
                putExtra(EXTRA_LIKE, newLike)
            })
            finish()
        }
        findViewById<Button>(R.id.btnInviteFriend).setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data = Uri.parse("mailto:")
                val mailBody = getString(R.string.invite_message, filmName)
                intent.putExtra(Intent.EXTRA_TEXT, mailBody)
                startActivity(intent)
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(this, getString(R.string.not_find_app), Toast.LENGTH_LONG).show()
            }
        }
    }

}