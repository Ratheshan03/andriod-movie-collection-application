package cw2.w1809947

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.room.Room
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ActorSearchActivity : AppCompatActivity() {
    private var searchBox: EditText? = null
    private var searchBtn: Button? = null
    private var resultsBox: TextView? = null
    private var copyRightText: TextView? = null
    private var movieTitle: String = ""
    private var  writtenData: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actor_search)

        // Inflate the views from XML
        searchBox = findViewById(R.id.searchBox)
        searchBtn = findViewById(R.id.sBtn)
        resultsBox = findViewById(R.id.results)
        copyRightText = findViewById(R.id.copyright)

        // OnClick Listeners
        searchBtn?.setOnClickListener {
            getActorMovie()
        }

        // Loading saved instance values
        if (savedInstanceState != null) {
            val landBtn = savedInstanceState.getBoolean("SaveBtn")
            val landSearch = savedInstanceState.getCharSequence("MySavedData")
            val landView = savedInstanceState.getString("viewMovies")
            val copyRight = savedInstanceState.getString("copyright")

            searchBtn?.isEnabled = landBtn
            searchBox?.setText(landSearch)
            resultsBox?.text = landView
            copyRightText?.text = copyRight

        }
    }


    // Saving the state of the activity when the orientation changes.
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (searchBtn?.isEnabled == true) {
            outState.putBoolean("SaveBtn", true)
        } else {
            outState.putBoolean("SaveBtn", false)
        }
        writtenData = searchBox?.text.toString()
        outState.putCharSequence("MySavedData", writtenData)

        outState.putString("viewMovies", resultsBox?.text.toString())
        outState.putString("copyright", copyRightText?.text.toString())
    }

    private fun getActorMovie() {
        // Access the database
        val db = Room.databaseBuilder(this, AppDatabase::class.java, "MovieDB").build()
        val movieDao = db.movieDao()

        // Getting the actor name
        val actor = searchBox!!.text.toString().trim()
        if (actor  == "")
            return

        runBlocking {
            launch{
                // Emptying the results box for every search
                movieTitle = ""

                val movies =  movieDao.getSpecificMovieTitle(actor)
                movieTitle = "|-------- Actor Related Movies- -------|\n\n"

                for ( x in movies){
                    movieTitle += x.title.toString()
                    movieTitle += "\n"
                }

                resultsBox?.text = movieTitle

                // TESTING PURPOSE
                println("|______________________________________________|")
                println(movies)
                println("|______________________________________________|")
            }
        }
    }
}