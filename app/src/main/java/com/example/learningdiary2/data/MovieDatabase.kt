    package com.example.learningdiary2.data
    import android.content.Context
    import androidx.room.Database
    import androidx.room.Room
    import androidx.room.RoomDatabase
    import androidx.room.TypeConverters
    import com.example.learningdiary2.utils.CustomConverters
    import com.example.learningdiary2.models.Movie

    @Database(
        entities = [Movie::class],
        version = 5,
        exportSchema = false
    )
    @TypeConverters(CustomConverters::class)
    abstract class MovieDatabase : RoomDatabase() {
        abstract fun movieDao(): MovieDao

        companion object{
            @Volatile
            private var Instance: MovieDatabase? = null

            fun getDatabase(context: Context): MovieDatabase {
                return Instance?: synchronized(this){
                    Room.databaseBuilder(context, MovieDatabase::class.java, "movie_db")
                        .fallbackToDestructiveMigration()
                        .build()
                        .also {
                            Instance = it
                        }
                }
            }
        }
    }