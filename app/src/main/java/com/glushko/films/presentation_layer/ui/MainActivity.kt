package com.glushko.films.presentation_layer.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.VisibleForTesting
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.glushko.films.App
import com.glushko.films.business_logic_layer.domain.AboutFilm
import com.glushko.films.presentation_layer.ui.exit_dialog.ExitDialog
import com.glushko.films.R
import com.glushko.films.business_logic_layer.domain.FavoriteFilm
import com.glushko.films.data_layer.datasource.response.ResponseFilm
import com.glushko.films.data_layer.utils.TAG
import com.glushko.films.data_layer.utils.TYPE_FILM_LIST
import com.glushko.films.presentation_layer.ui.detail_film.FragmentDetailFilm
import com.glushko.films.presentation_layer.ui.detail_film.FragmentDetailFilm.Companion.EXTRA_FILM_ID
import com.glushko.films.presentation_layer.ui.detail_film.FragmentDetailFilm.Companion.EXTRA_FILM_NAME
import com.glushko.films.presentation_layer.ui.exit_dialog.OnDialogListener
import com.glushko.films.presentation_layer.ui.favorite.CallbackFragmentFavorites
import com.glushko.films.presentation_layer.ui.favorite.FragmentFavorites
import com.glushko.films.presentation_layer.ui.films.CallbackFragmentFilms
import com.glushko.films.presentation_layer.ui.films.FragmentFilms
import com.glushko.films.presentation_layer.ui.see_later.FragmentSeeLater
import com.glushko.films.presentation_layer.vm.ViewModelFilms
import com.glushko.films.presentation_layer.vm.ViewModelFilmsFactory
//import com.glushko.films.presentation_layer.vm.ViewModelFilmsFactory
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.get
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import javax.inject.Inject

class MainActivity : AppCompatActivity(), OnDialogListener, CallbackFragmentFilms,
    CallbackFragmentFavorites {

    @VisibleForTesting
    val idlingResource = SimpleIdlingResource()

    private lateinit var remoteConfig: FirebaseRemoteConfig
    private lateinit var container: FragmentContainerView
    private lateinit var bottomNavigate: BottomNavigationView
    private lateinit var toolbar: Toolbar
    private lateinit var progressBar: ProgressBar


    @Inject
    lateinit var factory: ViewModelFilmsFactory
    private lateinit var model: ViewModelFilms
    private var selectMenu:Int? = null

    companion object {
        const val EXTRA_SAVE_STATE_TAB = "id_menu_bottom"
        const val TYPE_LIST_FILM_KEY = "TYPE_LIST_FILM"
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        idlingResource.setIdleState(false)
        App.appComponent.inject(this)
        model = ViewModelProvider(this, factory).get(ViewModelFilms::class.java)
        initFirebaseToken()
        initFirebaseConfig()
        setContentView(R.layout.activity_single_main)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        progressBar = findViewById(R.id.toolbar_progress_bar)
        selectMenu = savedInstanceState?.getInt(EXTRA_SAVE_STATE_TAB)?: R.id.menu_films
        container = findViewById(R.id.main_container)
        bottomNavigate = findViewById(R.id.nav_bottom_main)
        bottomNavigate.setOnItemSelectedListener {
            when(it.itemId){
                R.id.menu_films -> {
                    progressBar.visibility = View.VISIBLE
                    //model.clearFilms()
                    //model.getFilms(1)
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_container, FragmentFilms()).commit()
                }
                R.id.menu_favorite_films -> {
                    idlingResource.setIdleState(false)
                    supportFragmentManager.beginTransaction().replace(
                        R.id.main_container,
                        FragmentFavorites()).commit()

                }
                R.id.menu_see_later ->{
                    supportFragmentManager.beginTransaction().replace(
                        R.id.main_container,
                        FragmentSeeLater()).commit()
                }
            }
            true
        }
        /*bottomNavigate.setOnItemReselectedListener {
            //Позоволяет не реагировать на повтороное нажатие вкладки
        }*/
        selectMenu?.let {
            bottomNavigate.selectedItemId = it
        }
        model.liveDataFilm.observe(this, Observer {
            idlingResource.setIdleState(true)
            if(!it.isUpdateDB){
                progressBar.visibility = View.INVISIBLE
                if(!it.isSuccess){
                    Snackbar.make(bottomNavigate, getErrorMessage(it.err), Snackbar.LENGTH_LONG)
                        .setAction(getString(R.string.repeat)) {
                            model.getFilms(isNoAddPage = true)
                            progressBar.visibility = View.VISIBLE
                        }
                        .show()
                }
            }
        })

        Log.d("TAG", "Пришло от уведомления название фильма ${intent.getStringExtra(EXTRA_FILM_NAME)} \n " +
                "id = ${intent.getIntExtra(EXTRA_FILM_ID, -1)}")
        val filmId = intent.getIntExtra(EXTRA_FILM_ID, -1)
        if(filmId > 0){
            model.getFilm(filmId)
            progressBar.visibility = View.VISIBLE
        }
        model.liveDataOnceFilm.observe(this) {
            progressBar.visibility = View.INVISIBLE
            if (it.err == ResponseFilm.ERROR_NO) {
                val film = it.film!!
                supportFragmentManager.beginTransaction()
                    .add(R.id.main_container, FragmentDetailFilm.newInstance(film.position, film))
                    .addToBackStack("films")
                    .commit()
            }
        }
    }

    private fun initFirebaseConfig() {
        remoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val updated = task.result
                    Log.d(TAG, "Config params updated: $updated")
                    Toast.makeText(this, "Fetch and activate succeeded",
                        Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Fetch failed",
                        Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "Config params failed: ${task.isSuccessful}")
                }
                updateTypeList()
            }
    }

    private fun updateTypeList(){
        val type = remoteConfig.getString(TYPE_LIST_FILM_KEY)
        Log.d(TAG, "Type list film: $type")
        TYPE_FILM_LIST = type
        model.getFilms(1)
    }
    private fun initFirebaseToken(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.d("TAG", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            Log.d("TAG", "token = $token")
        })
    }

    private fun getErrorMessage(error: Int): String{
        return when(error){
            ResponseFilm.ERROR_NETWORK -> getString(R.string.error_network)
            ResponseFilm.ERROR_SERVER_TOKEN -> getString(R.string.error_token)
            ResponseFilm.ERROR_SERVER_TIME_LIMIT -> getString(R.string.error_limit)
            else -> getString(R.string.error_unknown)
        }
    }



    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //outState.putParcelableArrayList(EXTRA_SAVE_STATE, ArrayList(films))
        outState.putInt(EXTRA_SAVE_STATE_TAB, bottomNavigate.selectedItemId)

    }

    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount > 0){
            supportFragmentManager.popBackStack()
        }else{
            ExitDialog().show(supportFragmentManager, "ExitDialog")
        }
    }

    override fun onClickDialog(exit: Boolean) {
        if (exit) finish()
    }

    override fun actionWithMovie(position: Int, film: AboutFilm) {
        val favoriteFilm = FavoriteFilm(film.id, film.name, film.img)
        if (film.like == 1) {
            model.addFavoriteFilm(favoriteFilm)
        } else {
            model.deleteFavoriteFilm(favoriteFilm)
        }
    }

    override fun showProgressbar() {
        progressBar.visibility = View.VISIBLE
    }

    override fun actionInFavoriteMovies(film: FavoriteFilm, isDelete: Boolean) {
        if(isDelete){
            model.deleteFavoriteFilm(film)
        }else{
            model.addFavoriteFilm(film)
        }

    }
}