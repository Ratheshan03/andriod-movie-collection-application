package cw2.w1809947

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.room.Room
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {
    private lateinit var saveButton: Button
    private lateinit var searchButton: Button
    private lateinit var searchActorButton: Button
    private lateinit var filterMovieButton: Button
    private lateinit var imageLogo: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inflate the views from XML
        saveButton = findViewById(R.id.button)
        searchButton = findViewById(R.id.button2)
        searchActorButton = findViewById(R.id.button3)
        filterMovieButton = findViewById(R.id.button4)
        imageLogo = findViewById(R.id.imageView)

        // OnClick Listeners
        saveButton.setOnClickListener{
            saveMovies()
        }

        searchButton.setOnClickListener{
            displaySearchPage()
        }

        searchActorButton.setOnClickListener {
            displayActorSearchPage()
        }

        filterMovieButton.setOnClickListener {
            displayFilterMoviePage()
        }

        if (savedInstanceState != null) {
            val logo2 = savedInstanceState.getBoolean("ImageLogo")
            val button1 = savedInstanceState.getBoolean("Btn1IsEnabled")
            val button2 = savedInstanceState.getBoolean("Btn2IsEnabled")
            val button3 = savedInstanceState.getBoolean("Btn3IsEnabled")

            //Assigning values
            imageLogo.isEnabled = logo2
            saveButton.isEnabled = button1
            searchButton.isEnabled = button2
            searchActorButton.isEnabled = button3
        }
    }

   // Saving the state of the activity when the orientation changes.
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (saveButton.isEnabled) {
            outState.putBoolean("Btn1IsEnabled", true)
        } else {
            outState.putBoolean("Btn1IsEnabled", false)
        }
        if (searchButton.isEnabled) {
            outState.putBoolean("Btn2IsEnabled", true)
        } else {
            outState.putBoolean("Btn2IsEnabled", false)
        }
        if (searchActorButton.isEnabled) {
            outState.putBoolean("Btn3IsEnabled", true)
        } else {
            outState.putBoolean("Btn3IsEnabled", false)
        }
        if (imageLogo.isEnabled) {
            outState.putBoolean("ImageLogo", true)
        } else {
            outState.putBoolean("NoImageLogo", false)
        }
    }


    // Read all the words from the file in raw resources
    fun saveMovies() {
        // Creating an instance of the database
        val db = Room.databaseBuilder(this, AppDatabase::class.java, "MovieDB").build()
        val movieDao = db.movieDao()

        val text = "Movies Added to Database"
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(applicationContext, text, duration)

        runBlocking {
            launch {
                val movie1 = Movie(
                    1,
                    "The Shawshank Redemption",
                    1994, "R",
                    "14 Oct 1994",
                    "142 min",
                    "Drama",
                    "Frank Darabont",
                    "Stephen King, Frank Darabont",
                    "Tim Robbins, Morgan Freeman, Bob Gunton",
                    "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency."
                )

                val movie2 = Movie(
                    2,
                    "Batman: The Dark Knight Returns, Part 1",
                    2012,
                    "PG-13",
                    "25 Sep 2012",
                    "76 min",
                    "Animation, Action, Crime, Drama, Thriller",
                    "Jay Oliva",
                    "Bob Kane (character created by: Batman), Frank Miller (comic book), Klaus Janson (comic book), Bob Goodman",
                    "Peter Weller, Ariel Winter, David Selby, Wade Williams",
                    "Batman has not been seen for ten years. A new breed of criminal ravages Gotham City, forcing 55-year-old Bruce Wayne back into the cape and cowl. But, does he still have what it takes to fight crime in a new era?"
                )

                val movie3 = Movie(
                    3,
                    "The Lord of the Rings: The Return of the King",
                    2003,
                    "PG-13",
                    "17 Dec 2003",
                    "201 min",
                    "Action, Adventure, Drama",
                    "Peter Jackson",
                    "J.R.R. Tolkien, Fran Walsh, Philippa Boyens",
                    "Elijah Wood, Viggo Mortensen, Ian McKellen",
                    "Gandalf and Aragorn lead the World of Men against Sauron's army to draw his gaze from Frodo and Sam as they approach Mount Doom with the One Ring."
                )

                val movie4 = Movie(
                    4,
                    "Inception",
                    2010,
                    "PG-13",
                    "16 Jul 2010",
                    "148 min",
                    "Action, Adventure, Sci-Fi",
                    "Christopher Nolan",
                    "Christopher Nolan",
                    "Leonardo DiCaprio, Joseph Gordon-Levitt, Elliot Page",
                    "A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O., but his tragic past may doom the project and his team to disaster."
                )

                val movie5 = Movie(
                    5,
                    "The Matrix",
                    1999,
                    "R",
                    "31 Mar 1999",
                    "136 min",
                    "Action, Sci-Fi",
                    "Lana Wachowski, Lilly Wachowski",
                    "Lana Wachowski, Lilly Wachowski",
                    "Keanu Reeves, Laurence Fishburne, Carrie-Anne Moss",
                    "When a beautiful stranger leads computer hacker Neo to a forbidding underworld, he discovers the shocking truth--the life he knows is the elaborate deception of an evil cyber-intelligence."
                )

                // Populating Database
                movieDao.insertMovies(movie1, movie2, movie3, movie4, movie5)
                toast.show()

                // Displaying DB table data
                displayDbTablePage()
            }
        }
    }

    // Displays the movie data that saved in the Database
    fun displayDbTablePage() {
        val i = Intent(this, DisplayDBTableActivity::class.java)
        startActivity(i)
    }

    // Displaying Movie Search Page
    fun displaySearchPage() {
        val i = Intent(this, SearchActivity::class.java)
        startActivity(i)
    }

    // Displaying Actor Search Page
    fun displayActorSearchPage(){
        val i = Intent(this, ActorSearchActivity::class.java)
        startActivity(i)
    }

    // Displaying Filter Movie Search Page
    fun displayFilterMoviePage(){
        val i = Intent(this, FilterMovieActivity::class.java)
        startActivity(i)
    }

}