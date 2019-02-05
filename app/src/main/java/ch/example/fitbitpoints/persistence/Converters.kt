package ch.example.fitbitpoints.persistence

import androidx.room.TypeConverter
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

class Converters {
    object LocalDateConverter {
        private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

        @TypeConverter
        @JvmStatic
        fun toLocalDate(value: String?): LocalDate? {
            return value?.let {
                return LocalDate.parse(value, formatter)
            }
        }

        @TypeConverter
        @JvmStatic
        fun toString(date: LocalDate?): String? {
            return date?.format(formatter)
        }
    }
}