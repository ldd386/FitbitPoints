package ch.example.fitbitpoints.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import ch.example.fitbitpoints.FitBitPointsApplication
import ch.example.fitbitpoints.R
import ch.example.fitbitpoints.api.FitbitApi
import ch.example.fitbitpoints.api.model.CaloriesIntraday
import ch.example.fitbitpoints.api.model.HeartRateIntraday
import ch.example.fitbitpoints.api.model.StepsIntraday
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.series.DataPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate


class ActivityInDayDetailFragment : Fragment(){

    private lateinit var viewBindings: ActivityInDayDetailFragmentViewBindings

    lateinit var fitbitApi : FitbitApi

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewBindings = DataBindingUtil.inflate(inflater,
            ch.example.fitbitpoints.R.layout.activity_in_day_detail_fragment, container, false)
        return viewBindings.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //preparing graphs
        prepareGraph(viewBindings.graph)
        viewBindings.graph.viewport.setMaxY(220.0)
        prepareGraph(viewBindings.graph2)
        viewBindings.graph2.viewport.setMaxY(200.0)
        prepareGraph(viewBindings.graph3)
        viewBindings.graph3.viewport.setMaxY(15.0)
        // initialization of fitbitApi
        fitbitApi = (activity!!.application as FitBitPointsApplication).fitbitApi!!
        val date = arguments!!.getSerializable("date") as LocalDate
        viewBindings.dateInfo.text = date.toString()
        loadActivitiesForDate(date)
    }

    private fun prepareGraph(graph: GraphView) {
        graph.viewport.setScalableY(true)
        graph.viewport.isScalable  = true
        graph.viewport.setScrollableY(true)
        graph.viewport.isScrollable = true
        graph.viewport.setMinX(0.0)
        graph.viewport.setMaxX(24.0)
    }

    private fun loadActivitiesForDate(date: LocalDate) {
        GlobalScope.launch {
            // HEART RATE
            val heartRatesIntraday = fitbitApi.loadHeartRateIntraDay(date)
            val heartRateArrayDataPoints = ArrayList<DataPoint>()
            for (heartRateIntraday: HeartRateIntraday in heartRatesIntraday.dataset) {
                heartRateArrayDataPoints.add(
                    DataPoint(
                        heartRateIntraday.time.toSecondOfDay()/3600.0,
                        heartRateIntraday.value.toDouble()
                    )
                )
            }
            val heartRatesIntraDaySeries = LineGraphSeries<DataPoint>(heartRateArrayDataPoints.toTypedArray())
            heartRatesIntraDaySeries.setColor(resources.getColor(R.color.colorPrimary))
            // ui update
            val heartRateGraph = viewBindings.graph
            heartRateGraph.title = "Heart Rate (bpm)"
            heartRateGraph.addSeries(heartRatesIntraDaySeries)
            // STEPS
            val activitiesStepsIntraday =
                fitbitApi.loadTotalStepsIntraDay(date).activitiesStepsIntraday
            val stepsArrayDataPoints = ArrayList<DataPoint>()
            for (stepsIntraday: StepsIntraday in activitiesStepsIntraday.dataset) {
                stepsArrayDataPoints.add(
                    DataPoint(
                        stepsIntraday.time.toSecondOfDay()/3600.0,
                        stepsIntraday.value.toDouble()
                    )
                )
            }
            val stepsIntraDaySeries = LineGraphSeries<DataPoint>(stepsArrayDataPoints.toTypedArray())
            stepsIntraDaySeries.setColor(resources.getColor(R.color.colorPrimary))
            // ui update
            val stepsGraph = viewBindings.graph2
            stepsGraph.title = "Steps (steps/minutes)"
            stepsGraph.addSeries(stepsIntraDaySeries)
            // CALORIES
            val caloriesIntradayContainer = fitbitApi.loadCaloriesIntraDay(date)
            val caloriesArrayDataPoints = ArrayList<DataPoint>()
            for (caloriesIntraday: CaloriesIntraday in caloriesIntradayContainer.dataset) {
                caloriesArrayDataPoints.add(
                    DataPoint(
                        caloriesIntraday.time.toSecondOfDay()/3600.0,
                        caloriesIntraday.value
                    )
                )
            }
            val caloriesIntraDaySeries = LineGraphSeries<DataPoint>(caloriesArrayDataPoints.toTypedArray())
            caloriesIntraDaySeries.setColor(resources.getColor(R.color.colorPrimary))
            // ui update
            val caloriesGraph = viewBindings.graph3
            caloriesGraph.title = "Calories (calories/minutes)"
            caloriesGraph.addSeries(caloriesIntraDaySeries)
        }
    }


}