package com.glushko.films.films.detail_film

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.glushko.films.AboutFilm
import com.glushko.films.R
import java.text.FieldPosition

class FragmentDetailFilm: Fragment(R.layout.activity_detail_film) {

    companion object {
        private const val EXTRA_POSITION = "position"
        const val EXTRA_FILM_INFO = "film_info"

        fun newInstance(position: Int, film: AboutFilm): FragmentDetailFilm{
            return FragmentDetailFilm().apply {
                arguments = Bundle().apply {
                    putInt(EXTRA_POSITION, position)
                    putParcelable(EXTRA_FILM_INFO, film)
                }
            }
        }
    }

    private lateinit var editTextComment: EditText
    private lateinit var btnLike: ImageButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val film = arguments?.getParcelable(EXTRA_FILM_INFO) ?: AboutFilm(
            name = getString(R.string.default_value),
            img = R.drawable.ic_launcher_foreground,
            img_like = R.drawable.ic_not_like
        )
        val position = arguments?.getInt(EXTRA_POSITION, -1)
        view.findViewById<ImageView>(R.id.imageDetail).setImageResource(film.img )
        view.findViewById<TextView>(R.id.tvNameFilm).text = film.name
        editTextComment = view.findViewById(R.id.editTextComment)
        editTextComment.setText(film.comment)
        btnLike = view.findViewById(R.id.btnLikeDetail)
        btnLike.setImageResource(film.img_like )
        btnLike.setOnClickListener {
            film.like = !film.like
            film.img_like = if (film.like) R.drawable.ic_like else R.drawable.ic_not_like
            btnLike.setImageResource(film.img_like)
        }

        view.findViewById<ImageButton>(R.id.btnSendComment).setOnClickListener {
            film.comment = editTextComment.text.toString()
            /*setResult(AppCompatActivity.RESULT_OK, Intent().apply {
                putExtra(DetailFilmActivity.EXTRA_FILM_INFO, film)
                putExtra(DetailFilmActivity.EXTRA_POSITION, position)
            })*/
            parentFragmentManager.popBackStack()
        }
        view.findViewById<Button>(R.id.btnInviteFriend).setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data = Uri.parse("mailto:")
                val mailBody = getString(R.string.invite_message, film.name)
                intent.putExtra(Intent.EXTRA_TEXT, mailBody)
                startActivity(intent)
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(requireContext(), getString(R.string.not_find_app), Toast.LENGTH_LONG).show()
            }
        }
    }

}