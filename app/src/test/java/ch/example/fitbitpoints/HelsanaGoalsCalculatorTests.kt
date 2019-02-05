package ch.example.fitbitpoints

import ch.example.fitbitpoints.api.model.CaloriesIntraday
import ch.example.fitbitpoints.api.model.HeartRateIntraday
import ch.example.fitbitpoints.api.model.Steps
import org.junit.Test

import org.junit.Assert.*
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import kotlin.random.Random

class HelsanaGoalsCalculatorTests {

    @Test
    fun stepsAreMoreThan10kTrueTest(){
        val steps = Steps()
        steps.dateTime = LocalDate.now().toString()
        steps.value = 10234
        assertTrue(HelsanaGoalsCalculator.stepsInAdayAreMoreThan10k(steps))
    }

    @Test
    fun stepsAreMoreThan10kFalseTest(){
        val steps = Steps()
        steps.dateTime = LocalDate.now().toString()
        steps.value = 9234
        assertFalse(HelsanaGoalsCalculator.stepsInAdayAreMoreThan10k(steps))
    }

    @Test
    fun heartRateIsGreaterThan110forAtLeast30MinTrueTest(){
        val heartRateIntradayList = ArrayList<HeartRateIntraday>()
        var localTimeNow = LocalTime.of(10, 0)
        for(i in 0L..12L){
            val heartRateIntraday = HeartRateIntraday()
            heartRateIntraday.time = localTimeNow
            heartRateIntraday.value = 85 + Random.nextInt(20)
            heartRateIntradayList.add(heartRateIntraday)
            localTimeNow = localTimeNow.plusMinutes(1)

        }
        for(i in 0L..36L){
            val heartRateIntraday = HeartRateIntraday()
            heartRateIntraday.time = localTimeNow
            heartRateIntraday.value = 110 + Random.nextInt(20)
            heartRateIntradayList.add(heartRateIntraday)
            localTimeNow = localTimeNow.plusMinutes(1)
        }
        System.out.println("Localtime at end of test = " +  localTimeNow)
        assertTrue(HelsanaGoalsCalculator.heartRateIsGreaterThan110forAtLeast30Min(heartRateIntradayList))
    }

    @Test
    fun heartRateIsGreaterThan110forAtLeast30MinTrue2Test(){
        val heartRateIntradayList = ArrayList<HeartRateIntraday>()
        var localTimeNow = LocalTime.of(10, 45)
        for(i in 0L..5L){
            val heartRateIntraday = HeartRateIntraday()
            heartRateIntraday.time = localTimeNow
            heartRateIntraday.value = 85 + Random.nextInt(20)
            heartRateIntradayList.add(heartRateIntraday)
            localTimeNow = localTimeNow.plusMinutes(1)
        }
        for(i in 0L..31L){
            val heartRateIntraday = HeartRateIntraday()
            heartRateIntraday.time = localTimeNow
            heartRateIntraday.value = 110 + Random.nextInt(20)
            heartRateIntradayList.add(heartRateIntraday)
            localTimeNow = localTimeNow.plusMinutes(1)
        }
        System.out.println("Localtime at end of test = " +  localTimeNow)
        assertTrue(HelsanaGoalsCalculator
            .heartRateIsGreaterThan110forAtLeast30Min(heartRateIntradayList))
    }

    @Test
    fun heartRateIsGreaterThan110forAtLeast30MinFalseTest(){
        val heartRateIntradayList = ArrayList<HeartRateIntraday>()
        var localTimeNow = LocalTime.of(14, 58)
        for(i in 0L..3L){ // 4 minutes of less-intensive activity
            val heartRateIntraday = HeartRateIntraday()
            heartRateIntraday.time = localTimeNow
            heartRateIntraday.value = 85 + Random.nextInt(20)
            heartRateIntradayList.add(heartRateIntraday)
            localTimeNow = localTimeNow.plusMinutes(1)
        }
        for(i in 0L..25L){ // 26 minutes of intensive activity
            val heartRateIntraday = HeartRateIntraday()
            heartRateIntraday.time = localTimeNow
            heartRateIntraday.value = 110 + Random.nextInt(20)
            heartRateIntradayList.add(heartRateIntraday)
            localTimeNow = localTimeNow.plusMinutes(1)
        }
        for(i in 0L..3L){ // 4 minutes of less-intensive activity
            val heartRateIntraday = HeartRateIntraday()
            heartRateIntraday.time = localTimeNow
            heartRateIntraday.value = 85 + Random.nextInt(20)
            heartRateIntradayList.add(heartRateIntraday)
            localTimeNow = localTimeNow.plusMinutes(1)
        }
        for(i in 0L..15L){ // 16 minutes of intensive activity
            val heartRateIntraday = HeartRateIntraday()
            heartRateIntraday.time = localTimeNow
            heartRateIntraday.value = 110 + Random.nextInt(20)
            heartRateIntradayList.add(heartRateIntraday)
            localTimeNow = localTimeNow.plusMinutes(1)
        }
        System.out.println("Localtime at end of test = " +  localTimeNow)

        assertFalse(HelsanaGoalsCalculator
            .heartRateIsGreaterThan110forAtLeast30Min(heartRateIntradayList))

    }

    @Test
    fun has150CaloriesInMax30MinTrueTest(){
        val caloriesIntradayList = ArrayList<CaloriesIntraday>()
        var localTimeNow = LocalTime.of(0, 0)
        for(i in 0L..400L-1L){ // 400 minutes rest
            val caloriesIntraday = CaloriesIntraday()
            caloriesIntraday.value = 1.1
            caloriesIntraday.time = localTimeNow
            caloriesIntradayList.add(caloriesIntraday)
            localTimeNow = localTimeNow.plusMinutes(1)
        }
        for(i in 0L..40L-1L){ // 40 minutes of an intensive activity
            val caloriesIntraday = CaloriesIntraday()
            caloriesIntraday.value = 5 + Random.nextDouble(2.2)
            caloriesIntraday.time = localTimeNow
            caloriesIntradayList.add(caloriesIntraday)
            localTimeNow = localTimeNow.plusMinutes(1)
        }
        for(i in 0L..1000L-1L){ // 1000 minutes rest
            val caloriesIntraday = CaloriesIntraday()
            caloriesIntraday.value = 0.9 + Random.nextDouble(2.2)
            caloriesIntraday.time = localTimeNow
            caloriesIntradayList.add(caloriesIntraday)
            localTimeNow = localTimeNow.plusMinutes(1)
        }
        System.out.println("Localtime at end of test = " +  localTimeNow)
        assertTrue(HelsanaGoalsCalculator.has150CaloriesInMax30Min(caloriesIntradayList))
    }

    @Test
    fun has150CaloriesInMax30MinFalseLessCaloriesTest(){
        val caloriesIntradayList = ArrayList<CaloriesIntraday>()
        var localTimeNow = LocalTime.of(0, 0)
        for(i in 0L..400L-1L){ // 400 minutes rest
            val caloriesIntraday = CaloriesIntraday()
            caloriesIntraday.value = 1.1
            caloriesIntraday.time = localTimeNow
            caloriesIntradayList.add(caloriesIntraday)
            localTimeNow = localTimeNow.plusMinutes(1)
        }
        for(i in 0L..40L-1L){ // 40 minutes of an intensive activity
            val caloriesIntraday = CaloriesIntraday()
            caloriesIntraday.value = 2 + Random.nextDouble(2.2)
            caloriesIntraday.time = localTimeNow
            caloriesIntradayList.add(caloriesIntraday)
            localTimeNow = localTimeNow.plusMinutes(1)
        }
        for(i in 0L..1000L-1L){ // 1000 minutes rest
            val caloriesIntraday = CaloriesIntraday()
            caloriesIntraday.value = 0.9 + Random.nextDouble(2.2)
            caloriesIntraday.time = localTimeNow
            caloriesIntradayList.add(caloriesIntraday)
            localTimeNow = localTimeNow.plusMinutes(1)
        }
        System.out.println("Localtime at end of test = " +  localTimeNow)
        assertFalse(HelsanaGoalsCalculator.has150CaloriesInMax30Min(caloriesIntradayList))

    }
}
