package com.kssidll.zapierdalo.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kssidll.zapierdalo.APPLICATION_NAME
import com.kssidll.zapierdalo.data.dao.GpsDao
import com.kssidll.zapierdalo.data.dao.RunActionDao
import com.kssidll.zapierdalo.data.dao.StepsDao
import com.kssidll.zapierdalo.data.data.GpsEntity
import com.kssidll.zapierdalo.data.data.RunActionEntity
import com.kssidll.zapierdalo.data.data.StepsEntity
import com.kssidll.zapierdalo.data.data.shadow.GpsLengthShadow
import com.kssidll.zapierdalo.data.data.shadow.StepsAmountShadow
import com.kssidll.zapierdalo.data.data.view.RunAction
import com.kssidll.zapierdalo.data.data.view.RunActionSummary

/**
 * default database name
 */
const val DATABASE_NAME: String = APPLICATION_NAME + "_database.db"

@Database(
    version = 1,
    entities = [
        StepsEntity::class,
        StepsAmountShadow::class,
        GpsEntity::class,
        GpsLengthShadow::class,
        RunActionEntity::class,
    ],
    views = [
        RunAction::class,
        RunActionSummary::class,
    ]
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun runActionDao(): RunActionDao
    abstract fun gpsDao(): GpsDao
    abstract fun stepsDao(): StepsDao

    companion object {
        /**
         * @param context app context
         * @param name name of the database or absolute path if not internal location
         * @return [RoomDatabase.Builder] of [AppDatabase] for [name]
         */
        @Suppress("SameParameterValue")
        private fun builder(
            context: Context,
            name: String
        ): RoomDatabase.Builder<AppDatabase> {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                name
            )
        }

        /**
         * @param context app context
         * @return [AppDatabase] created in internal location with [DATABASE_NAME] name
         */
        fun build(context: Context): AppDatabase {
            return builder(
                context,
                DATABASE_NAME
            ).build()
        }
    }
}