package com.archay.busboards.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.archay.busboards.R
import com.archay.busboards.base.BaseFragment
import com.archay.busboards.databinding.FragmentSplashBinding
import com.archay.busboards.local.BusPreferences
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {


    @Inject
    lateinit var preferences: BusPreferences

    private val viewModel by viewModels<BusViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (preferences.deviceId.isNotEmpty())
            viewModel.ads(preferences.deviceId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.adsLoading.observe(viewLifecycleOwner) {
            if (it)
                binding.pbLoading.visibility = View.VISIBLE
            else
                binding.pbLoading.visibility = View.GONE
        }

        viewModel.ads.observe(viewLifecycleOwner) { adsDto ->
            val action = SplashFragmentDirections.actionSplashFragmentToMainFragment()
            action.adsDto = adsDto.toTypedArray()
            findNavController().navigate(action)

        }

        if (preferences.deviceId.isEmpty() /*|| preferences.deviceId.isEmpty()*/) {
            findNavController().navigate(R.id.tokenFragment)
        }
    }

}