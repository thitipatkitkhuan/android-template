package com.thitipat.template.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [ItemData::class, ColorData::class, LogData::class], version = 1)
abstract class MainDatabase : RoomDatabase() {

    abstract fun mainDao(): MainDao

    companion object {
        @Volatile
        private var INSTANCE: MainDatabase? = null
        fun getInstance(context: Context): MainDatabase {
            synchronized(this) {// กัน thread อื่นมาใช้
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MainDatabase::class.java,
                        "todo_db"
                    )
                        .addCallback(object : Callback() {
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                super.onCreate(db)
                                /*CoroutineScope(Dispatchers.IO).launch {
                                    getInstance(context).mainDao().updateDownloadDate("${Shared.getDateNow()} ${Shared.getTimeNow()}")
                                }*/
                            }
                        }).build()
                }
                return instance
            }
        }
    }
}