package com.hobbajt.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseFragment<T : SchedulerViewModel, B : ViewDataBinding> : DaggerFragment() {

    @Inject
    @VisibleForTesting
    lateinit var viewModelFactory: ViewModelProvider.Factory

    protected lateinit var vm: T
    private var toolbarVm: ToolbarVM? = null

    protected var binding: B? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ViewModelProviders.of(this, viewModelFactory)[vmClass()]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId(), container, false)
        vm = ViewModelProviders.of(this, viewModelFactory)[vmClass()]
        bind()
        binding?.lifecycleOwner = this.viewLifecycleOwner
        setup()

        return binding?.root
    }

    protected abstract fun bind()

    protected abstract fun layoutId(): Int

    protected abstract fun vmClass(): Class<T>

    open fun setup() {
        setupToolbar()
    }

    private fun setupToolbar() {
        toolbarVm = activity?.run {
            ViewModelProviders.of(this).get(ToolbarVM::class.java)
        }
        (vm as? ToolbarOwner)?.toolbar?.observe(viewLifecycleOwner, Observer {
            toolbarVm?.updateToolbar(it)
        })
    }

    protected open fun onToolbarBackClicked() {
        view?.findNavController()?.popBackStack()
    }
}