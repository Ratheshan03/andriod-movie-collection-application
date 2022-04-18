package cw2.w1809947

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class SearchActivity : AppCompatActivity() {
    private var searchBox: EditText? = null
    private var searchBtn: Button? = null
    private var resultsBox: TextView? = null
    private var saveResultBtn: Button? = null
    private var copyRightText: TextView? = null
    private var MY_API_KEY: String? = null
    private var urlString: String? = null
    private var dbData: String? = null

    // Search values for each movies
    private var movieTitle: String = ""
    private var movieYear: Int = 0
    private var movieRated: String = ""
    private var movieReleased: String = ""
    private var movieRuntime: String = ""
    private var movieGenre: String = ""
    private var movieDirector: String = ""
    private var movieWriter: String = ""
    private var movieActors: String = ""
    private var moviePlot: String = ""
    private var writtenData: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // Inflate the views from XML
        searchBox = findViewById(R.id.searchBox)
        searchBtn = findViewById(R.id.searchBtn)
        resultsBox = findViewById(R.id.results)
        saveResultBtn = findViewById(R.id.saveBtn)
        copyRightText = findViewById(R.id.copyright)

        // Assigning the API key
        MY_API_KEY = resources.getString(R.string.My_API_KEY)

        // OnClick Listeners
        searchBtn?.setOnClickListener {
            getMovie()
        }

        saveResultBtn?.setOnClickListener {
            saveMovie()
        }

        // Loading saved preferences
        if (savedInstanceState != null) {
            val retrieveDetails = savedInstanceState.getBoolean("RetrieveData")
            val saveDetails = savedInstanceState.getBoolean("SaveData")
            val textDetails = savedInstanceState.getString("MovieDetails")
            val landSearch = savedInstanceState.getCharSequence("MySavedData")
            val copyRight = savedInstanceState.getString("copyright")

            searchBtn?.isEnabled = retrieveDetails
            saveResultBtn?.isEnabled = saveDetails
            resultsBox?.text = textDetails
            searchBox?.setText(landSearch)
            copyRightText?.text = copyRight

        }
    }

    // Saving the state of the activity when the orientation changes.
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (searchBtn?.isEnabled == true) {
            outState.putBoolean("RetrieveData", true)
        } else {
            outState.putBoolean("RetrieveData", false)
        }
        if (saveResultBtn?.isEnabled == true) {
            outState.putBoolean("SaveData", true)
        } else {
            outState.putBoolean("SaveData", false)
        }
        outState.putString("MovieDetails", resultsBox?.text.toString())

        writtenData = searchBox!!.text.toString()
        outState.putCharSequence("MySavedData", writtenData)

        outState.putString("copyright", copyRightText?.text.toString())
    }

    private fun getMovie() {
        // !! - converts any value to a non-null type and throws an exception if the value is null
        // trim() - removes whitespace from both ends of a string
        val movie = searchBox!!.text.toString().lowercase().trim()
        if (movie  == "")
            return

        // Setting up the fetching URL for the web service
        urlString = "https://www.omdbapi.com/?t=$movie&apikey=$MY_API_KEY"


        // Start fetching data in the background - Not in main thread
        runBlocking {
            withContext(Dispatchers.IO){
                // whole JSON data
                val stb = StringBuilder("")
                val url = URL(urlString)
                val con = url.openConnection() as HttpURLConnection
                val bf: BufferedReader

                try {
                    bf = BufferedReader(InputStreamReader(con.inputStream))
                }catch (e: IOException){
                    e.printStackTrace()
                    return@withContext
                }

                var line = bf.readLine()
                while (line != null){
                    stb.append(line)
                    line = bf.readLine()
                }

                // Picking up all the data
                dbData = parseJSON(stb)
            }
            resultsBox?.setText(dbData)
        }
    }

    private fun parseJSON(stb: StringBuilder): String {
        // Extracting the actual data from the JSON data
        val json = JSONObject(stb.toString())
        movieTitle = json["Title"] as String
        val Year = json["Year"] as String
        movieYear = Year.toInt()
        movieRated = json["Rated"] as String
        movieReleased = json["Released"] as String
        movieRuntime = json["Runtime"] as String
        movieGenre = json["Genre"] as String
        movieDirector = json["Director"] as String
        movieWriter = json["Writer"] as String
        movieActors = json["Actors"] as String
        moviePlot = json["Plot"] as String

        var finalResult = "\nTitle: " + '"' + movieTitle + '"'+
                      "\nYear: " +  movieYear +
                      "\nRated: " + movieRated +
                      "\nReleased: " + movieReleased +
                      "\nRuntime: " + movieRuntime +
                      "\nGenre: " + movieGenre +
                      "\nDirector: " + movieDirector +
                      "\nWriter: " + movieWriter +
                      "\nActors: " + movieActors +
                      "\n\nPlot: " + moviePlot

        return finalResult;
    }

    private fun saveMovie() {
        // Access the database
        val db = Room.databaseBuilder(this, AppDatabase::class.java, "MovieDB").build()
        val movieDao = db.movieDao()

        // Toast message for confirmation
        val text = "Movie Added to Database"
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(applicationContext, text, duration)

        runBlocking {
            launch{
                // Saving the searched movie results in the DataBase
                val movies: List<Movie> = movieDao.getAll()
                val index = movies.size + 1
                val mov = Movie(index, movieTitle, movieYear, movieRated, movieReleased, movieRuntime, movieGenre, movieDirector, movieWriter, movieActors, moviePlot)
                movieDao.insertMovies(mov)
                toast.show()

                // For Testing Purpose
                println("\n|------------------------------------------------------------|\n")
                for(m in movies){
                    println(m)
                    println()
                }
                println("\n|------------------------------------------------------------|\n")
            }
        }
    }
}