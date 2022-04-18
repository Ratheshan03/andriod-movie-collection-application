package cw2.w1809947

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class FilterMovieActivity : AppCompatActivity() {
    private var searchBox: EditText? = null
    private var searchBtn: Button? = null
    private var resultsBox: TextView? = null
    private var copyRightText: TextView? = null
    private var MY_API_KEY: String? = null
    private var urlString: String? = null
    private var dbData: String? = null
    private var  movieTitles: ArrayList<String> = ArrayList()
    private var  writtenData: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter_movie)

        // Inflate the views from XML
        searchBox = findViewById(R.id.searchBox)
        searchBtn = findViewById(R.id.sBtn)
        resultsBox = findViewById(R.id.results)
        copyRightText = findViewById(R.id.copyright)

        // Assigning the API key
        MY_API_KEY = resources.getString(R.string.My_API_KEY)

        // OnClick Listeners
        searchBtn?.setOnClickListener {
            getMovie()
        }

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

    private fun getMovie() {
        // !! - converts any value to a non-null type and throws an exception if the value is null
        // trim() - removes whitespace from both ends of a string
        val movie = searchBox!!.text.toString().lowercase().trim()
        if (movie  == "")
            return

        // Setting up the fetching URL for the web service
        urlString = "https://www.omdbapi.com/?s=${movie}*&apikey=${MY_API_KEY}"

        // Start fetching data in the background
        runBlocking {
            withContext(Dispatchers.IO){
                // Emptying the results for every btn click
                dbData = ""

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

                dbData = "Movies related to the term: \n\n"

                // Picking up all the data
                for (x in parseJSON(stb)){
                    dbData += x + "\n"
                }

                // For Testing Purpose
                println("|***************************************************|")
                println(stb)
                println("|***************************************************|")
            }
            resultsBox?.setText(dbData)
        }
    }

    private fun parseJSON(stb: StringBuilder): ArrayList<String> {
        // Emptying results
        movieTitles.clear()
        // Extracting the actual data from the JSON data
        val json = JSONObject(stb.toString())
        val sArray = json.getJSONArray("Search")

        for( i in 0 until sArray.length()){
            val movies = sArray.getJSONObject(i)
            val movieTitle = movies["Title"] as String
            movieTitles.add(movieTitle)
        }
        val allResults = json["totalResults"] as String
        val NoOfResults = "\nTotal retrieved results: $allResults"
        movieTitles.add(NoOfResults)

        return movieTitles
    }
}

