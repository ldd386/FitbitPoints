package ch.example.fitbitpoints.persistence

import androidx.room.*
import org.threeten.bp.LocalDate

@Dao
interface ActivityInDayDao {

    @Query("SELECT * FROM activity_in_day")
    fun getAll(): List<ActivityInDayEntity>

    @Query("SELECT * FROM activity_in_day WHERE date LIKE :date")
    fun findByDate(date: LocalDate): ActivityInDayEntity?

    @Insert
    fun insertAll(activitiesInDayEntity: List<ActivityInDayEntity>)

    @Delete
    fun delete(activityInDayEntity: ActivityInDayEntity)
}