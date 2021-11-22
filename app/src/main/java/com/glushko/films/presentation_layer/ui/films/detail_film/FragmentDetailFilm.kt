package com.glushko.films.presentation_layer.ui.films.detail_film

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.glushko.films.presentation_layer.ui.about_film.AboutFilm
import com.glushko.films.R
import com.glushko.films.business_logic_layer.domain.Users
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FragmentDetailFilm: Fragment(R.layout.fragment_detail_film) {

    private val comments = listOf(
        Users("Users 1", "qwhjqwrbqwhrjqwrb"),
        Users("Users 2", "safasfasfasfasfsnmaf \n ashfjajshfjasfha"),
        Users("Users 3", "qwhjqwrbqwhrjqwrb\n\n\nasdasdasdsad as"),
        Users("Users 4", "qwhjqwrbqwhrjqwrb sadasdsadasdsa Ad asfa s SAf SA"),
        Users("Users 5", "qwhjqwrbqwhrjqwrb ASF ASf ASfA Sf"),
        Users("Users 6", "qwhjqwrbqwhrjqwrb As FAs FAs fAS fAs"),
        Users("Users 7", "qwhjqwrbqwhrjqwrb Asf ASf ASf as wqf w E JH as"),
        Users("Users 8", "qwhjqwrbqwhrjqwrb"),
        Users("Users 9", "qwhjqwrbqwhrjqwrb"),

    )

    companion object {
        const val KEY_RETURN = "return info detail"
        const val EXTRA_POSITION = "position"
        const val EXTRA_FILM_INFO = "film_info"

        fun newInstance(position: Int, film: AboutFilm): FragmentDetailFilm {
            return FragmentDetailFilm().apply {
                arguments = Bundle().apply {
                    putInt(EXTRA_POSITION, position)
                    putParcelable(EXTRA_FILM_INFO, film)
                }
            }
        }
    }

    private lateinit var editTextComment: EditText
    //private lateinit var btnLike: ImageButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val film = arguments?.getParcelable(EXTRA_FILM_INFO) ?: AboutFilm(
            name = getString(R.string.default_value),
            img = R.drawable.ic_launcher_foreground,
            img_like = R.drawable.ic_not_like
        )
        val position = arguments?.getInt(EXTRA_POSITION, -1)
        val btnLike = view.findViewById<FloatingActionButton>(R.id.btnLikeDetail)
        btnLike.setImageResource(film.img_like)
        btnLike.setOnClickListener {
            film.like = !film.like
            film.img_like = if (film.like) R.drawable.ic_like else R.drawable.ic_not_like
            btnLike.setImageResource(film.img_like)
        }
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar_detail_film)
        toolbar.title = film.name
        editTextComment = view.findViewById(R.id.editTextComment)
        editTextComment.setText(film.comment)
        view.findViewById<ImageButton>(R.id.btnSendComment).setOnClickListener {
            film.comment = editTextComment.text.toString()
            setFragmentResult(KEY_RETURN, Bundle().apply {
                putParcelable(EXTRA_FILM_INFO, film)
                putInt(EXTRA_POSITION, position?:-1)
            })
            parentFragmentManager.popBackStack()
        }

        view.findViewById<ImageView>(R.id.backdrop_detail_film).setImageResource(film.img)

        val recycler = view.findViewById<RecyclerView>(R.id.recycler_comment_film)
        recycler.layoutManager = LinearLayoutManager(requireActivity())
        recycler.adapter = AdapterDetailFilm(comments)

        val btnInviteFriend = view.findViewById<FloatingActionButton>(R.id.btnInviteFriend)
        btnInviteFriend.setOnClickListener {
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