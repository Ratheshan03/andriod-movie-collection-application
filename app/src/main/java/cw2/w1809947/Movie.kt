package cw2.w1809947

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Movie(
    @PrimaryKey
    @ColumnInfo(name = "movieId")
    val id: Int,

    @ColumnInfo(name = "Title")
    val title: String?,

    @ColumnInfo(name = "Year")
    val year: Int?,

    @ColumnInfo(name = "Rated")
    val rated: String?,

    @ColumnInfo(name = "Released")
    val released: String?,

    @ColumnInfo(name = "Runtime")
    val runtime: String?,

    @ColumnInfo(name = "Genre")
    val genre: String?,

    @ColumnInfo(name = "Director")
    val director: String?,

    @ColumnInfo(name = "Writer")
    val writer: String?,

    @ColumnInfo(name = "Actors")
    val actors: String?,

    @ColumnInfo(name = "Plot")
    val plot: String?,
)
