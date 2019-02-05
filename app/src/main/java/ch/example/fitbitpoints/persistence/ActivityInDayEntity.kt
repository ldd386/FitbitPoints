package ch.example.fitbitpoints.persistence

import androidx.room.*
import org.threeten.bp.LocalDate

@Entity(tableName = "activity_in_day")
data class ActivityInDayEntity(
    @PrimaryKey var date: LocalDate,
    @ColumnInfo(name = "creation_date") var creationDate : LocalDate,
    @ColumnInfo(name = "has_150_calories_in_max_30min") var has150CaloriesInMax30Min: Boolean,
    @ColumnInfo(name = "heart_rate_is_greater_than_110_for_at_least_30min") var heartRateIsGreaterThan110forAtLeast30Min: Boolean,
    @ColumnInfo(name = "steps_in_a_day_are_more_than_10k") var stepsInAdayAreMoreThan10k: Boolean)