package cw2.w1809947

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.room.Room
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class DisplayDBTableActivity : AppCompatActivity() {
    lateinit var dbTitle: TextView
    lateinit var tableData: TextView
    lateinit var copyRightText: TextView
    private val dbData: ArrayList<String> = ArrayList()
    private var result = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_dbtable)

        dbTitle = findViewById(R.id.tableTitle)
        tableData = findViewById(R.id.tableData)
        copyRightText = findViewById(R.id.copyright)

        // Loading saved preferences
        if (savedInstanceState != null) {
            val tableTitle = savedInstanceState.getString("dbTitle")
            val dbValues = savedInstanceState.getString("tableValues")
            val copyRight = savedInstanceState.getString("copyRight")

            dbTitle.text = tableTitle
            tableData.text = dbValues
            copyRightText.text = copyRight

        }

        // Displaying the movie list in the Database
        displaymovieList()
    }

    // Saving the state of the activity when the orientation changes.
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("dbTitle", dbTitle.text.toString())
        outState.putString("tableValues", tableData.text.toString())
        outState.putString("copyRight", copyRightText.text.toString())
    }

    fun displaymovieList(){
        val db = Room.databaseBuilder(this, AppDatabase::class.java, "MovieDB").build()
        val movieDao = db.movieDao()

        runBlocking {
            launch {
                val movies: List<Movie> = movieDao.getAll()
                for (m in movies) {
                    dbData.add("\n${m.id}\n ${m.title}\n ${m.year}\n ${m.rated}\n ${m.released}\n ${m.runtime}\n ${m.genre}\n ${m.director}\n ${m.writer}\n ${m.actors}\n ${m.plot}\n")
                }

                for (m in dbData) {
                    result += m + "\n"
                }
                tableData.setText(result)
            }
        }
    }
}