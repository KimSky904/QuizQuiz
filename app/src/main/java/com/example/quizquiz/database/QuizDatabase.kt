package com.example.quizquiz.database

import android.content.Context
import androidx.room.*

@Dao
interface QuizDAO {
    @Insert
    fun insert(quiz: Quiz) : Long // "insert into quiz values( ... )

    @Update
    fun update(quiz: Quiz) // "update from quiz values( ... ) where id=1"

    @Delete
    fun delete(quiz: Quiz) // "delete from quiz where id = 1"

    @Query("SELECT * FROM quiz")
    fun getAll(): List<Quiz>
}

@Database(entities=[Quiz::class], version=1)
@TypeConverters(StringListConverter::class)
abstract class QuizDatabase : RoomDatabase() {
    abstract fun quizDAO(): QuizDAO

    // 싱글턴 패턴 적용
    companion object {
        private var INSTANCE: QuizDatabase? = null

        fun getInstance(context: Context) : QuizDatabase {
            if(INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    QuizDatabase::class.java,
                "database.db").build()
            }
            return INSTANCE!!
        }
    }
}















