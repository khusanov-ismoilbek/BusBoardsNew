package com.archay.busboards.ui

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.media3.common.util.UnstableApi
import androidx.navigation.fragment.navArgs
import com.archay.busboards.base.BaseFragment
import com.archay.busboards.data.dto.AdsDto
import com.archay.busboards.databinding.FragmentMainBinding
import com.archay.busboards.ui.widgets.AdWidgetFragment
import com.archay.busboards.ui.widgets.StationWidgetFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@UnstableApi
class MainFragment : BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {

    private val args by navArgs<MainFragmentArgs>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        addFragment(AdWidgetFragment(), AdWidgetFragment::class.java.name)
        addFragment(StationWidgetFragment(), StationWidgetFragment::class.java.name)
        AdWidgetFragment.getInstance(args.adsDto?.toList())
    }


    private fun addFragment(currentFragment: Fragment, widgetName: String) {
        val frameLayout = FrameLayout(requireContext())
        val lp = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        frameLayout.layoutParams = lp
        val frameId = currentFragment.hashCode()
        frameLayout.id = frameId
        frameLayout.tag = widgetName

        if (widgetName == AdWidgetFragment::class.java.name)
            binding.llContainerAd.addView(frameLayout)
        else
            binding.llContainerStation.addView(frameLayout)

        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(frameId, currentFragment, widgetName).commitNow()
    }


}