package com.hobbajt.weather

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.AttributeSet
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.hobbajt.presentation.base.ToolbarVM
import com.hobbajt.weather.databinding.ActivityMainBinding
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : DaggerAppCompatActivity() {
    private var view: View? = null
    private var toolbarVm: ToolbarVM? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        toolbarVm = ViewModelProviders.of(this).get(ToolbarVM::class.java)
        setUp()
    }

    override fun onCreateView(parent: View?, name: String, context: Context, attrs: AttributeSet): View? {
        view = parent
        return super.onCreateView(parent, name, context, attrs)
    }

    private fun setUp() {
        setupToolbar()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        toolbarVm?.toolbar?.observe(this, Observer { toolbar ->
            val isBackExist = isBackButtonExist()
            val toolbarColor = view?.let {
                ContextCompat.getColor(it.context, toolbar.colorId)
            }
            supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(isBackExist)
                setDisplayShowHomeEnabled(isBackExist)
                toolbarColor?.let {
                    setBackgroundDrawable(ColorDrawable(it))
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                    window.statusBarColor = it
                }
                setTitle(toolbar.titleId)
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        toolbarVm?.toolbar?.value?.let {
            if (isBackButtonExist()) {
                navigationHostFragment.findNavController().popBackStack()
            }
        }
    }

    private fun isBackButtonExist(): Boolean {
        return navigationHostFragment.childFragmentManager.backStackEntryCount > 0
    }
}