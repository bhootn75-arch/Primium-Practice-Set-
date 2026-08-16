package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.dao.PracticeSetDao
import com.example.data.dao.QuestionDao
import com.example.data.dao.TestResultDao
import com.example.data.model.PracticeSetEntity
import com.example.data.model.QuestionEntity
import com.example.data.model.TestResultEntity
import com.example.data.seed.DefaultPracticeSets
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
  entities = [
    PracticeSetEntity::class,
    QuestionEntity::class,
    TestResultEntity::class
  ],
  version = 1,
  exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
  abstract fun practiceSetDao(): PracticeSetDao
  abstract fun questionDao(): QuestionDao
  abstract fun testResultDao(): TestResultDao

  companion object {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          AppDatabase::class.java,
          "santali_practice_sets.db"
        )
        .addCallback(object : Callback() {
          override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
              scope.launch(Dispatchers.IO) {
                DefaultPracticeSets.populateInitialData(database)
              }
            }
          }
        })
        .fallbackToDestructiveMigration()
        .build()
        INSTANCE = instance
        instance
      }
    }
  }
}
