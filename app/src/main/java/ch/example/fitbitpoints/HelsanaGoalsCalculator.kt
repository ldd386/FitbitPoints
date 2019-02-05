package ch.example.fitbitpoints

import ch.example.fitbitpoints.api.model.CaloriesIntraday
import ch.example.fitbitpoints.api.model.HeartRateIntraday
import ch.example.fitbitpoints.api.model.Steps
import org.threeten.bp.temporal.ChronoUnit

class HelsanaGoalsCalculator {
    companion object {
        @JvmStatic
        fun heartRateIsGreaterThan110forAtLeast30Min(heartRateIntradayIntraday: MutableList<HeartRateIntraday>): Boolean {
            val greaterThanValue = 110
            val atLeastMinutes = 30
            var consecutiveMinutesCounter = 0
            for (i in 0..heartRateIntradayIntraday.size - atLeastMinutes - 1) {
                // must be consecutive minutes
                for (j in i..i + atLeastMinutes) {
                    if (heartRateIntradayIntraday[j].value < greaterThanValue) {
                        consecutiveMinutesCounter = 0
                        break
                    }
                    if (consecutiveMinutesCounter > 0 && ChronoUnit.MINUTES.between(
                            heartRateIntradayIntraday[j - 1].time,
                            heartRateIntradayIntraday[j].time
                        ) != 1L
                    ) {
                        consecutiveMinutesCounter = 0
                        break
                    }
                    consecutiveMinutesCounter = consecutiveMinutesCounter + 1
                    if (consecutiveMinutesCounter == atLeastMinutes) {
                        return true
                    }
                }
            }
            return false
        }

        @JvmStatic
        fun has150CaloriesInMax30Min(caloriesIntraday: MutableList<CaloriesIntraday>): Boolean {
            val totCaloriesDesired = 150
            val inMaxMinutes = 30
            var totCaloriesCounter = 0.0
            // in this case the list size is 1440 = 24 hours x 60 minutes ( -> minutes are always consecutive)
            for (i in 0..caloriesIntraday.size - inMaxMinutes - 1) {
                // must be consecutive minutes
                for (j in i..i + inMaxMinutes) {
                    totCaloriesCounter = totCaloriesCounter + caloriesIntraday[j].value
                    if (totCaloriesCounter >= totCaloriesDesired) {
                        return true
                    }
                }
                totCaloriesCounter = 0.0
            }
            return false
        }

        @JvmStatic
        fun stepsInAdayAreMoreThan10k(stepsIntraday: Steps): Boolean {
            return stepsIntraday.value >= 10_000
        }
    }
}