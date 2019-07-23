package com.hobbajt.cityweather.citylist

import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.google.android.material.snackbar.Snackbar
import com.hobbajt.cityweather.R
import com.hobbajt.cityweather.databinding.FragmentCityListBinding
import com.hobbajt.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_city_list.*

class CityListFragment : BaseFragment<CityListVM, FragmentCityListBinding>() {

    companion object {
        private const val CITY_NAME_TRANSACTION_TAG = "cityName"
    }

    private lateinit var selectedCityVm: SelectedCityVM
    private var cityListAdapter: CityListAdapter? = null

    override fun bind() {
        binding?.vm = vm
    }

    override fun layoutId() = R.layout.fragment_city_list

    override fun vmClass(): Class<CityListVM> = CityListVM::class.java

    override fun setup() {
        super.setup()
        setupSearchText()
        setupSelectedCity()
        setupCityListAdapter()
        setupCityClick()
    }

    private fun setupSearchText() {
        vm.getIsSearchTextValid().observe(viewLifecycleOwner, Observer {
            citySearchField.error = if (it) {
                null
            } else {
                context?.getString(R.string.city_list_search_validation_error)
            }
        })
    }

    private fun setupSelectedCity() {
        selectedCityVm = activity?.run {
            ViewModelProviders.of(this).get(SelectedCityVM::class.java)
        } ?: throw Exception("Invalid Activity")
    }

    private fun setupCityListAdapter() {
        cityListAdapter = CityListAdapter(vm)
        binding?.cityList?.adapter = cityListAdapter
        vm.getCities().observe(viewLifecycleOwner, Observer {
            cityListAdapter?.submitList(it)
        })
    }

    private fun setupCityClick() {
        vm.getOnCityClick().observe(viewLifecycleOwner, Observer {
            selectedCityVm.select(it.first, it.second)
            vm.getCities().value?.indexOf(it.first)?.let { position ->
                openWeatherScreen(position)
            }
        })

        vm.getOnCityWeatherLoadError().observe(viewLifecycleOwner, Observer {
            displayLoadingErrorSnackbar()
        })
    }

    private fun displayLoadingErrorSnackbar() {
        view?.let {
            val text = context?.getString(R.string.snackbar_load_data_failed)
            if (text != null) {
                Snackbar.make(it, text, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun openWeatherScreen(position: Int) {
        val animationView =
            binding?.cityList?.findViewHolderForLayoutPosition(position)?.itemView?.findViewById<View>(R.id.cityName)
        animationView?.transitionName = CITY_NAME_TRANSACTION_TAG
        val navigatorExtras = animationView?.let {
            FragmentNavigatorExtras(animationView to animationView.transitionName)
        }
        view?.findNavController()
            ?.navigate(R.id.action_cityListFragment_to_weatherFragment, null, null, navigatorExtras)

    }
}