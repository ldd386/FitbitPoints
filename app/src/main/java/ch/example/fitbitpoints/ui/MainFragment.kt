package ch.example.fitbitpoints.ui

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.databinding.DataBindingUtil

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import ch.example.fitbitpoints.FitBitPointsApplication
import ch.example.fitbitpoints.HelsanaGoalsCalculator
import ch.example.fitbitpoints.R
import ch.example.fitbitpoints.api.FitbitApi
import ch.example.fitbitpoints.api.model.User
import ch.example.fitbitpoints.persistence.ActivityInDayEntity
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import org.threeten.bp.Month
import org.threeten.bp.temporal.ChronoUnit

class MainFragment : Fragment() {

    private lateinit var viewBindings: MainFragmentViewBindings

    lateinit var fitbitApi : FitbitApi
    lateinit var activityInDayListAdapter : ActivityInDayListAdapter

    val activityInADayList = ArrayList<ActivityInDayListItem>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewBindings = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false)
        return viewBindings.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listView = viewBindings.pointsActivities
        activityInDayListAdapter = ActivityInDayListAdapter(
                context!!,
                R.id.points_activities,
                activityInADayList
        )
        listView.adapter = activityInDayListAdapter
        listView.setOnItemClickListener { parent, view, position, id ->
            Log.i("MainFragment", "list item clicked, id: " + id)
            val bundle = Bundle()
            bundle.putSerializable("date", activityInADayList[id.toInt()].date)
            val activityInDayDetailFragment = ActivityInDayDetailFragment()
            activityInDayDetailFragment.arguments = bundle
            activity!!.supportFragmentManager.beginTransaction().addToBackStack("main")
                .add(R.id.content, activityInDayDetailFragment).commit()
        }
        Log.i("MainFragment", "onCreate called")
        // getting fitbitapi object from application
        if((activity!!.application as FitBitPointsApplication).fitbitApi  == null){
            (activity!!.application as FitBitPointsApplication).fitbitApi = FitbitApi()
        }
        fitbitApi = (activity!!.application as FitBitPointsApplication).fitbitApi!!
        handleIntent()
    }

    private fun handleIntent() {
        Log.i("MainFragment", "handling intent")
        if(activity!!.intent != null && activity!!.intent.data != null){
            val requestedUrl = activity!!.intent.data.toString()
            val startIndex = requestedUrl.indexOf("code=")
            val authCode = requestedUrl.substring(startIndex+5, startIndex+45)
            Log.i("MainFragment", String.format("auth code for Oauth2 is: %s", authCode))
            continueAuth(authCode)
        } else {
            Log.i("MainFragment", "starting auth")
            startAuth()
        }
    }

    private fun startAuth() {
        GlobalScope.launch {
            val authUrl = fitbitApi.authUrl
            openTabWithUrl(authUrl)
        }
    }

    private fun openTabWithUrl(authUrl: String) {
        val builder = CustomTabsIntent.Builder()
        val tabIntent = builder.build()
        tabIntent.launchUrl(context, Uri.parse(authUrl))
    }

    private fun continueAuth(authCode: String) {
        GlobalScope.launch {
            fitbitApi.doAuth(authCode)
            loadTodayUserData()
        }
    }

    private fun loadTodayUserData() {
        GlobalScope.launch {
            // getting Profile
            val profile = fitbitApi.loadProfile()
            if(profile.user == null){
                Log.e("MainFragment", "Profile for user not received")
                showError()
                return@launch
            }
            Log.i("MainFragment", String.format("Profile for user %s received", profile.user.fullName))
            //showing data to user
            showGeneralData(profile.user)
            // getting Intra-Days data
            val today  = LocalDate.now()
            val registrationDate = LocalDate.of(2019, Month.JANUARY, 17)
            val daysFromRegistration = ChronoUnit.DAYS.between(registrationDate,today)
            val activityInADayDao =  (activity!!.application as FitBitPointsApplication).db!!.activityInDayDao()
            val activityInDayEntitiesToSave = ArrayList<ActivityInDayEntity>()
            for(i in 0..daysFromRegistration){
                val date = today.minusDays(i)
                // trying to get data from database
                val activityInDayEntity = activityInADayDao.findByDate(date)
                if(activityInDayEntity == null){
                    // reloading data from the API & add entity to list
                    val caloriesIntraday = fitbitApi.loadCaloriesIntraDay(date)
                    val has150CaloriesInMax30Min =
                            HelsanaGoalsCalculator.has150CaloriesInMax30Min(caloriesIntraday.dataset)
                    val heartRateIntraday = fitbitApi.loadHeartRateIntraDay(date)
                    val heartRateIsGreaterThan110forAtLeast30Min
                            =
                            HelsanaGoalsCalculator.heartRateIsGreaterThan110forAtLeast30Min(
                                    heartRateIntraday.dataset
                            )
                    val stepsForDay = fitbitApi.loadTotalStepsIntraDay(date).activitiesSteps.first()
                    val stepsInAdayAreMoreThan10k =
                            HelsanaGoalsCalculator.stepsInAdayAreMoreThan10k(stepsForDay)
                    if(!date.equals(today)) {
                        activityInDayEntitiesToSave.add(
                                ActivityInDayEntity(
                                        date = date,
                                        creationDate = today,
                                        has150CaloriesInMax30Min = has150CaloriesInMax30Min,
                                        heartRateIsGreaterThan110forAtLeast30Min = heartRateIsGreaterThan110forAtLeast30Min,
                                        stepsInAdayAreMoreThan10k = stepsInAdayAreMoreThan10k
                                )
                        )
                    }
                    // adding data to UI
                    val canGetPoints =
                            has150CaloriesInMax30Min || heartRateIsGreaterThan110forAtLeast30Min || stepsInAdayAreMoreThan10k
                    val activityInDayListItem =
                            ActivityInDayListItem(
                                    date,
                                    has150CaloriesInMax30Min = has150CaloriesInMax30Min,
                                    heartRateIsGreaterThan110forAtLeast30Min = heartRateIsGreaterThan110forAtLeast30Min,
                                    stepsInAdayAreMoreThan10k = stepsInAdayAreMoreThan10k,
                                    canGetPoints = canGetPoints
                            )
                    updateList(activityInDayListItem)
                } else {
                    // getting data from entity
                    val has150CaloriesInMax30Min = activityInDayEntity.has150CaloriesInMax30Min
                    val heartRateIsGreaterThan110forAtLeast30Min = activityInDayEntity.heartRateIsGreaterThan110forAtLeast30Min
                    val stepsInAdayAreMoreThan10k = activityInDayEntity.stepsInAdayAreMoreThan10k
                    // adding data to UI
                    val canGetPoints =
                            has150CaloriesInMax30Min || heartRateIsGreaterThan110forAtLeast30Min || stepsInAdayAreMoreThan10k
                    val activityInDayListItem =
                            ActivityInDayListItem(
                                    date,
                                    has150CaloriesInMax30Min = has150CaloriesInMax30Min,
                                    heartRateIsGreaterThan110forAtLeast30Min = heartRateIsGreaterThan110forAtLeast30Min,
                                    stepsInAdayAreMoreThan10k = stepsInAdayAreMoreThan10k,
                                    canGetPoints = canGetPoints
                            )
                    updateList(activityInDayListItem)
                }
            }
            activityInADayDao.insertAll(activityInDayEntitiesToSave)
        }
    }

    private fun showError() {
        activity?.runOnUiThread {
            val alertDialog: AlertDialog? = activity?.let {
                val builder = AlertDialog.Builder(it)
                builder.apply {
                    setNeutralButton("ok",
                        DialogInterface.OnClickListener { dialog, id ->
                            activity!!.finish()
                        })
                }
                builder.setTitle("Error")
                builder.setMessage("Cannot load user profile.\nThe application will be terminated ")
                // Create the AlertDialog
                builder.create()
            }
            alertDialog?.show()
        }
    }

    private fun showGeneralData(user: User) {
        activity!!.runOnUiThread {
            val generalDataImageView = viewBindings.profile
            Picasso.get().load(user.avatar150).networkPolicy(NetworkPolicy.NO_CACHE).into(generalDataImageView)
            Log.i("MainFragment", "showing general data")
            val generalDataTextView = viewBindings.generalData
            val sb = StringBuilder()
            sb.appendln(user.fullName)
            sb.appendln("Member since: " + user.memberSince)
            sb.appendln("Average daily steps: " + user.averageDailySteps)
            generalDataTextView.text = sb.toString()
        }
    }

    private fun updateList(activityInDayListItem : ActivityInDayListItem) {
        activity!!.runOnUiThread {
            activityInADayList.add(activityInDayListItem)
            activityInDayListAdapter.notifyDataSetChanged()
        }
    }
}