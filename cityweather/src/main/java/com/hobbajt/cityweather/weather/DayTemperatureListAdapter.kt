package com.hobbajt.cityweather.weather

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.hobbajt.cityweather.BR
import com.hobbajt.cityweather.R
import com.hobbajt.cityweather.weather.model.DayTemperature

class DayTemperatureListAdapter : RecyclerView.Adapter<DayTemperatureListAdapter.ViewHolder>() {

    var items: List<DayTemperature> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = items.count()

    override fun getItemViewType(position: Int) = R.layout.item_weather_day_temperature

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    class ViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DayTemperature) {
            binding.setVariable(BR.item, item)
            binding.executePendingBindings()
        }
    }
}