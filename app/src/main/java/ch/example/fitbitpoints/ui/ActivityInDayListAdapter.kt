package ch.example.fitbitpoints.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import ch.example.fitbitpoints.R

import java.util.ArrayList

class ActivityInDayListAdapter(private val mContext: Context, resourceId: Int, listItems: ArrayList<ActivityInDayListItem>) :
    ArrayAdapter<ActivityInDayListItem>(mContext, resourceId, listItems) {

    private val mListItems: List<ActivityInDayListItem>
    private val mInflater: LayoutInflater

    init {
        mListItems = listItems
        mInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        var convertView = view
        val holder: ViewHolder
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.activity_in_day_list_item_layout, null)
            holder = ViewHolder()
            holder.title = convertView!!.findViewById(R.id.title)
            holder.icon = convertView.findViewById(R.id.icon)
            holder.description1 = convertView.findViewById(R.id.description1)
            holder.description2 = convertView.findViewById(R.id.description2)
            holder.description3 = convertView.findViewById(R.id.description3)
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }

        val item = getItem(position)
        if (item != null) {
            if (item.canGetPoints) {
                holder.title!!.text = String.format("Points for date %s: 200", item.date.toString())
                holder.icon!!.setImageResource(R.drawable.outline_thumb_up_24)
            } else {
                holder.title!!.text = String.format("Points for date %s: 0", item.date.toString())
                holder.icon!!.setImageResource(R.drawable.outline_thumb_down_24)
            }
            holder.description1!!.text = String.format("150 Calories In Max 30Min = %s" , item.has150CaloriesInMax30Min)
            holder.description2!!.text = String.format("Heart Rate >110bpm for +30Min = %s",
                    item.heartRateIsGreaterThan110forAtLeast30Min)
            holder.description3!!.text = String.format("Steps in day > 10 000 = %s", item.stepsInAdayAreMoreThan10k)
        }

        return convertView
    }

    override fun getCount(): Int {
        return mListItems.size
    }

    override fun getItem(position: Int): ActivityInDayListItem? {
        return mListItems[position]
    }

    inner class ViewHolder {
        internal var title: TextView? = null
        internal var description1: TextView? = null
        internal var description2: TextView? = null
        internal var description3: TextView? = null
        internal var icon: ImageView? = null
    }
}
