package com.kssidll.zapierdalo.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import co.anbora.labs.spatia.builder.SpatiaRoom
import co.anbora.labs.spatia.geometry.GeometryConverters
import com.kssidll.zapierdalo.APPLICATION_NAME
import com.kssidll.zapierdalo.data.dao.GpsDao
import com.kssidll.zapierdalo.data.dao.RunActionDao
import com.kssidll.zapierdalo.data.dao.StepsDao
import com.kssidll.zapierdalo.data.data.GpsEntity
import com.kssidll.zapierdalo.data.data.RunActionEntity
import com.kssidll.zapierdalo.data.data.StepsEntity
import com.kssidll.zapierdalo.data.data.view.GpsPath
import com.kssidll.zapierdalo.data.data.view.RunAction
import com.kssidll.zapierdalo.data.data.view.RunActionSummary
import com.kssidll.zapierdalo.data.data.view.StepsAmount

/**
 * default database name
 */
const val DATABASE_NAME: String = APPLICATION_NAME + "_database.db"

@Database(
    version = 1,
    entities = [
        StepsEntity::class,
        GpsEntity::class,
        RunActionEntity::class,
    ],
    views = [
        StepsAmount::class,
        GpsPath::class,
        RunAction::class,
        RunActionSummary::class,
    ]
)
@TypeConverters(
    GeometryConverters::class
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
        private fun builder(
            context: Context,
            name: String
        ): SpatiaRoom.Builder<AppDatabase> {
            return SpatiaRoom.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                name
            ).addCallback(object: Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    // Initialize Spatialite
                    db.query("SELECT InitSpatialMetaData();").moveToNext()
                    // Room already creates a BLOB column for the geometry, so we need to use
                    // RecoverGeometryColumn to correctly initialize Spatialite's metadata
                    db.query("SELECT RecoverGeometryColumn('GpsEntity', 'location', 4326, 'POINT', 'XY');")
                        .moveToNext()
                }
            })
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