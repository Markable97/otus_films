package com.glushko.films.presentation_layer.ui.detail_film

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.glushko.films.App
import com.glushko.films.R
import com.glushko.films.business_logic_layer.domain.AboutFilm
import com.glushko.films.business_logic_layer.domain.Comment
import com.glushko.films.presentation_layer.vm.ViewModelSeeLater
import com.glushko.films.presentation_layer.vm.ViewModelSeeLaterFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*
import javax.inject.Inject

class FragmentDetailFilm: Fragment(R.layout.fragment_detail_film) {

    private val comments = listOf(
        Comment("Users 1", "qwhjqwrbqwhrjqwrb"),
        Comment("Users 2", "safasfasfasfasfsnmaf \n ashfjajshfjasfha"),
        Comment("Users 3", "qwhjqwrbqwhrjqwrb\n\n\nasdasdasdsad as"),
        Comment("Users 4", "qwhjqwrbqwhrjqwrb sadasdsadasdsa Ad asfa s SAf SA"),
        Comment("Users 5", "qwhjqwrbqwhrjqwrb ASF ASf ASfA Sf"),
        Comment("Users 6", "qwhjqwrbqwhrjqwrb As FAs FAs fAS fAs"),
        Comment("Users 7", "qwhjqwrbqwhrjqwrb Asf ASf ASf as wqf w E JH as"),
        Comment("Users 8", "qwhjqwrbqwhrjqwrb"),
        Comment("Users 9", "qwhjqwrbqwhrjqwrb"),

        )

    companion object {
        const val KEY_RETURN = "return info detail"
        const val EXTRA_POSITION = "position"
        const val EXTRA_FILM_INFO = "film_info"
        const val EXTRA_FILM_ID = "id"
        const val EXTRA_FILM_NAME = "name"

        fun newInstance(position: Int, film: AboutFilm): FragmentDetailFilm {
            return FragmentDetailFilm().apply {
                arguments = Bundle().apply {
                    putInt(EXTRA_POSITION, position)
                    putParcelable(EXTRA_FILM_INFO, film)
                }
            }
        }
    }

    private var chooseDate = false
    private var chooseTime = false
    private val calendar: Calendar = Calendar.getInstance()
    private var year = calendar.get(Calendar.YEAR)
    private var month = calendar.get(Calendar.MONTH)
    private var day = calendar.get(Calendar.DAY_OF_MONTH)
    private var hour = calendar.get(Calendar.HOUR_OF_DAY)
    private var minute = calendar.get(Calendar.MINUTE)
    private val filmDefault = AboutFilm(
        name = "",
        img = "",
        imgLike = R.drawable.ic_not_like,
        typeList = ""
    )
    var film: AboutFilm = filmDefault
    private lateinit var editTextComment: EditText
    private lateinit var model: ViewModelSeeLater
    @Inject
    lateinit var factory: ViewModelSeeLaterFactory
    //private lateinit var btnLike: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)
        model = ViewModelProvider(requireActivity(), factory)[ViewModelSeeLater::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        film = arguments?.getParcelable<AboutFilm>(EXTRA_FILM_INFO)?.apply {
            imgLike = if(imgLike==0) R.drawable.ic_not_like else imgLike
        } ?: filmDefault
        val position = arguments?.getInt(EXTRA_POSITION, -1)
        val btnLike = view.findViewById<FloatingActionButton>(R.id.btnLikeDetail)
        btnLike.setImageResource(film.imgLike)
        btnLike.setOnClickListener {
            film.like = if(film.like == 1) 0 else 1
            film.imgLike = if (film.like == 1) R.drawable.ic_like else R.drawable.ic_not_like
            btnLike.setImageResource(film.imgLike)
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

        Glide.with(requireContext()).load(film.img).error(R.drawable.ic_avatar_unknow)
            .into(view.findViewById(R.id.backdrop_detail_film))

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