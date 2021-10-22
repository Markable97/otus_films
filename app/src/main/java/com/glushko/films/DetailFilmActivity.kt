package com.glushko.films

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

class DetailFilmActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_POSITION = "position"
        const val EXTRA_FILM_INFO = "film_info"
    }


    private lateinit var editTextComment: EditText
    private lateinit var btnLike: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_film)
        val film = intent.getParcelableExtra(EXTRA_FILM_INFO) ?: AboutFilm(
            name = getString(R.string.default_value),
            img = R.drawable.ic_launcher_foreground,
            img_like = R.drawable.ic_not_like
        )
        val position = intent.getIntExtra(EXTRA_POSITION, -1)
        findViewById<ImageView>(R.id.imageDetail).setImageResource(film.img )
        findViewById<TextView>(R.id.tvNameFilm).text = film.name
        editTextComment = findViewById(R.id.editTextComment)
        editTextComment.setText(film.comment)
        btnLike = findViewById(R.id.btnLikeDetail)
        btnLike.setImageResource(film.img_like )
        btnLike.setOnClickListener {
            film.like = !film.like
            film.img_like = if (film.like) R.drawable.ic_like else R.drawable.ic_not_like
            btnLike.setImageResource(film.img_like)
        }

        findViewById<ImageButton>(R.id.btnSendComment).setOnClickListener {
            film.comment = editTextComment.text.toString()
            setResult(RESULT_OK, Intent().apply {
                putExtra(EXTRA_FILM_INFO, film)
                putExtra(EXTRA_POSITION, position)
            })
            finish()
        }
        findViewById<Button>(R.id.btnInviteFriend).setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data = Uri.parse("mailto:")
                val mailBody = getString(R.string.invite_message, film.name)
                intent.putExtra(Intent.EXTRA_TEXT, mailBody)
                startActivity(intent)
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(this, getString(R.string.not_find_app), Toast.LENGTH_LONG).show()
            }
        }
    }

}