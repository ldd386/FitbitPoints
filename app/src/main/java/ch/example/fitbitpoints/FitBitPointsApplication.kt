package ch.example.fitbitpoints

import android.app.Application
import android.util.Log
import androidx.room.Room
import ch.example.fitbitpoints.api.FitbitApi
import ch.example.fitbitpoints.persistence.FitBitPointsDatabase

class FitBitPointsApplication : Application() {

    var fitbitApi : FitbitApi? = null
    var db : FitBitPointsDatabase? = null

    override fun onCreate() {
        super.onCreate()
        Log.i("FitBitPointsApplication", "=== Application created ===")
        db = Room.databaseBuilder(
            applicationContext,
            FitBitPointsDatabase::class.java, "database-fitbit_points"
        ).build()
        Log.i("FitBitPointsApplication", "FitBitPointsDatabase initializated")

    }

}