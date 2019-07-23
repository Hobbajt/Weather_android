package com.hobbajt.cityweather.weather

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.transition.TransitionInflater
import com.hobbajt.cityweather.R
import com.hobbajt.cityweather.citylist.SelectedCityVM
import com.hobbajt.cityweather.databinding.FragmentWeatherBinding
import com.hobbajt.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_weather.*

class WeatherFragment : BaseFragment<WeatherVM, FragmentWeatherBinding>() {

    private lateinit var selectedCityVm: SelectedCityVM
    private var dayTemperatureListAdapter: DayTemperatureListAdapter? = null

    override fun bind() {
        binding?.vm = vm
    }

    override fun layoutId() = R.layout.fragment_weather

    override fun vmClass() = WeatherVM::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val transition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = transition
        sharedElementReturnTransition = transition
    }

    override fun setup() {
        super.setup()
        setupSelectedCity()
        setupDayTemperatureListAdapter()
        setupHourlyTemperatures()
    }

    private fun setupSelectedCity() {
        selectedCityVm = activity?.run {
            ViewModelProviders.of(this).get(SelectedCityVM::class.java)
        } ?: throw Exception("Invalid Activity")

        selectedCityVm.selectedCityWeather.observe(viewLifecycleOwner, Observer {
            vm.onCitySelected(it)
        })
    }

    private fun setupDayTemperatureListAdapter() {
        dayTemperatureListAdapter = DayTemperatureListAdapter()
        binding?.dayTemperatures?.adapter = dayTemperatureListAdapter
        vm.getDayTemperatures().observe(viewLifecycleOwner, Observer {
            dayTemperatureListAdapter?.items = it
        })
    }

    private fun setupHourlyTemperatures() {
        vm.getHourlyTemperatures().observe(viewLifecycleOwner, Observer {
            hourlyTemperatureChart.values = it
        })
    }
}