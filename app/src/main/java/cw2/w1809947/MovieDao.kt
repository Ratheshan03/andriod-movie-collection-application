package cw2.w1809947

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MovieDao {
    @Insert
    suspend fun insertAll(vararg movies: Movie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(vararg movie: Movie)

    @Query("SELECT * FROM movie")
    suspend fun getAll(): List<Movie>

    @Query("SELECT * FROM movie WHERE Actors LIKE '%' || :name || '%' ")
    suspend fun getSpecificMovieTitle(name: String): List<Movie>

    @Query("SELECT count(*) FROM movie")
    suspend fun countAll(): Int
}