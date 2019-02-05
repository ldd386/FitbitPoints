package ch.example.fitbitpoints.api

import android.util.Log
import ch.example.fitbitpoints.api.adapters.LocalDateAdapter
import ch.example.fitbitpoints.api.adapters.LocalTimeAdapter
import ch.example.fitbitpoints.api.model.*
import com.github.scribejava.apis.FitbitApi20
import com.github.scribejava.apis.fitbit.FitBitOAuth2AccessToken
import com.github.scribejava.core.builder.ServiceBuilder
import com.github.scribejava.core.model.OAuthRequest
import com.github.scribejava.core.model.Verb
import com.github.scribejava.core.oauth.OAuth20Service
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.DateTimeFormatterBuilder

import org.threeten.bp.temporal.ChronoField.DAY_OF_MONTH
import org.threeten.bp.temporal.ChronoField.MONTH_OF_YEAR
import org.threeten.bp.temporal.ChronoField.YEAR

class FitbitApi {

    private val LOG_TAG = "FitbitApi"

    private var service: OAuth20Service? = null
    private var accessToken: FitBitOAuth2AccessToken? = null

    // Obtain the Authorization URL
    val authUrl: String
        get() {
            val clientId = "22DB8T"
            val clientSecret = "143c4a5fdf75de9bb14011a99be6e14e"
            service = ServiceBuilder(clientId)
                .apiSecret(clientSecret)
                .scope("activity nutrition heartrate profile")
                .state("some_params")
                .build(FitbitApi20.instance())
            Log.i(LOG_TAG, "=== Fitbit's OAuth Workflow ===")
            Log.i(LOG_TAG, "Fetching the Authorization URL...")
            val authorizationUrl = service!!.authorizationUrl
            Log.i(LOG_TAG, "Got the Authorization URL!")
            Log.i(LOG_TAG, "Now go and authorize ScribeJava here:")
            Log.i(LOG_TAG, authorizationUrl)
            return authorizationUrl
        }

    fun doAuth(code: String) {
        // Trade the Request Token and Verfier for the Access Token
        Log.i(LOG_TAG, "Trading the Request Token for an Access Token...")
        val oauth2AccessToken = service!!.getAccessToken(code)
        Log.i(LOG_TAG, "Got the Access Token!")
        Log.i(
            LOG_TAG, "(if your curious it looks like this: " + oauth2AccessToken
                    + ", 'rawResponse'='" + oauth2AccessToken.rawResponse + "')"
        )

        if (oauth2AccessToken !is FitBitOAuth2AccessToken) {
            Log.i(LOG_TAG, "oauth2AccessToken is not instance of FitBitOAuth2AccessToken. Strange enough. exit.")
            return
        }
        accessToken = oauth2AccessToken
    }

    fun loadProfile(): Profile {
        val profileUrl = "https://api.fitbit.com/1/user/%s/profile.json"
        // Now let's go and ask for a protected resource!
        // This will get the profile for this user
        Log.i(LOG_TAG, "Now we're going to access the user profile...")
        val request = OAuthRequest(
            Verb.GET,
            String.format(profileUrl, accessToken!!.userId)
        )
        request.addHeader("x-li-format", "json")
        service!!.signRequest(accessToken, request)
        val response = service!!.execute(request)
        Log.i(LOG_TAG, "get profile HTTP code: " + response.code)
        val gson = getGson()
        return gson.fromJson(response.body, Profile::class.java)
    }

    fun loadDailyActivity(localDate: LocalDate): DailyActivitySummary {
        val dateTimeFormatter = fitbitDateTimeFormatter()
        val activityUrl = String.format(
            "https://api.fitbit.com/1/user/-/activities/date/%s.json",
            dateTimeFormatter.format(localDate)
        )
        // Now let's go and ask for a protected resource!
        // This will get the profile for this user
        Log.i(LOG_TAG, "Now we're going to access the daily activity...")
        val request = OAuthRequest(
            Verb.GET,
            String.format(activityUrl, accessToken!!.userId)
        )
        request.addHeader("x-li-format", "json")
        service!!.signRequest(accessToken, request)
        val response = service!!.execute(request)
        Log.i(LOG_TAG, "get daily activity code: " + response.code)
//        Log.i(LOG_TAG, "get daily activity body: " + response.body)
        val gson = getGson()
        return gson.fromJson(response.body, DailyActivitySummary::class.java)
    }

    private fun fitbitDateTimeFormatter(): DateTimeFormatter {
        return DateTimeFormatterBuilder()
            .appendValue(YEAR, 4).appendLiteral("-")
            .appendValue(MONTH_OF_YEAR, 2).appendLiteral("-")
            .appendValue(DAY_OF_MONTH, 2).toFormatter()
    }

    fun loadHeartRateIntraDay(localDate: LocalDate): ActivitiesHeartRateIntraday {
        val dateTimeFormatter = fitbitDateTimeFormatter()
        val activityUrl = String.format(
            "https://api.fitbit.com/1/user/-/activities/heart/date/%s/1d/1min.json",
            dateTimeFormatter.format(localDate)
        )
        // Now let's go and ask for a protected resource!
        // This will get the profile for this user
        Log.i(LOG_TAG, "Now we're going to access the heart rate intra-day...")
        val request = OAuthRequest(
            Verb.GET,
            String.format(activityUrl, accessToken!!.userId)
        )
        request.addHeader("x-li-format", "json")
        service!!.signRequest(accessToken, request)
        val response = service!!.execute(request)
        Log.i(LOG_TAG, "get heart rate intra-day activity code: " + response.code)
//        Log.i(LOG_TAG, "get heart rate intra-day activity body: " + response.body)
        val gson = getGson()
        return gson.fromJson(response.body, HeartRatesContainer::class.java).activitiesHeartRateIntraday
    }

    fun loadCaloriesIntraDay(localDate: LocalDate): CaloriesIntradayContainer {
        val dateTimeFormatter = fitbitDateTimeFormatter()
        val activityUrl = String.format(
            "https://api.fitbit.com/1/user/-/activities/calories/date/%s/1d/1min.json",
            dateTimeFormatter.format(localDate)
        )
        // Now let's go and ask for a protected resource!
        // This will get the profile for this user
        Log.i(LOG_TAG, "Now we're going to access the calories intra-day...")
        val request = OAuthRequest(
            Verb.GET,
            String.format(activityUrl, accessToken!!.userId)
        )
        request.addHeader("x-li-format", "json")
        service!!.signRequest(accessToken, request)
        val response = service!!.execute(request)
        Log.i(LOG_TAG, "get calories intra-day activity code: " + response.code)
//        Log.i(LOG_TAG, "get calories intra-day activity body: " + response.body)
        val gson = getGson()
        return gson.fromJson(response.body, CaloriesContainer::class.java).activitiesCaloriesIntraday
    }

    fun loadTotalStepsIntraDay(localDate: LocalDate): StepsContainer {
        val dateTimeFormatter = fitbitDateTimeFormatter()
        val activityUrl = String.format(
            "https://api.fitbit.com/1/user/-/activities/steps/date/%s/1d.json",
            dateTimeFormatter.format(localDate)
        )
        // Now let's go and ask for a protected resource!
        // This will get the profile for this user
        Log.i(LOG_TAG, "Now we're going to access the steps intra-day...")
        val request = OAuthRequest(
            Verb.GET,
            String.format(activityUrl, accessToken!!.userId)
        )
        request.addHeader("x-li-format", "json")
        service!!.signRequest(accessToken, request)
        val response = service!!.execute(request)
        Log.i(LOG_TAG, "get steps activity code: " + response.code)
//        Log.i(LOG_TAG, "get steps activity body: " + response.body)
        val gson = getGson()
        return gson.fromJson(response.body, StepsContainer::class.java)
    }

    private fun getGson() : Gson {
        return GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
            .registerTypeAdapter(LocalTime::class.java, LocalTimeAdapter())
            .create()
    }

}
