package com.archay.busboards.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.archay.busboards.R
import com.archay.busboards.base.BaseFragment
import com.archay.busboards.databinding.FragmentTokenBinding
import com.archay.busboards.local.BusPreferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TokenFragment : BaseFragment<FragmentTokenBinding>(FragmentTokenBinding::inflate) {

    @Inject
    lateinit var preferences: BusPreferences

    private val viewModel by viewModels<BusViewModel>()
    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getToken()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.token.observe(viewLifecycleOwner) {
            preferences.deviceToken = it.token

            val arrayCode = it.token.toCharArray()

            binding.llDeviceToken.removeAllViews()

            for (item in arrayCode) {
                val inflater =
                    layoutInflater.inflate(
                        R.layout.item_number,
                        binding.llDeviceToken,
                        false
                    )

                inflater.findViewById<TextView>(R.id.title).text = item.toString()
                binding.llDeviceToken.addView(inflater)

            }


            viewModel.getDeviceByToken(it.token)
        }

        viewModel.deviceByToken.observe(viewLifecycleOwner) {

            preferences.deviceId = it.id

            viewModel.ads(it.id)
        }

        viewModel.deviceByTokenFailure.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                delay(2000)
                viewModel.getDeviceByToken(preferences.deviceToken)
            }
        }

        viewModel.ads.observe(viewLifecycleOwner) {
            val action = TokenFragmentDirections.actionTokenFragmentToMainFragment()
            action.adsDto = it.toTypedArray()
            findNavController().navigate(action)
        }
    }
}