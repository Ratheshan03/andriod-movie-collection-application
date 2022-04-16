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
    lateinit var copyright: TextView
    private val dbData: ArrayList<String> = ArrayList()
    private var result = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_dbtable)

        dbTitle = findViewById(R.id.tableTitle)
        tableData = findViewById(R.id.tableData)
        copyright = findViewById(R.id.copyright)

        // Displaying the movie list in the Database
        displaymovieList()
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