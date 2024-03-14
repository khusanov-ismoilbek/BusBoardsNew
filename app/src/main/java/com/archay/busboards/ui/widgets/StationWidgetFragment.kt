package com.archay.busboards.ui.widgets

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.archay.busboards.base.BaseFragment
import com.archay.busboards.databinding.WidgetStationBinding
import com.archay.busboards.ui.BusViewModel
import com.archay.busboards.ui.widgets.adapter.StationAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class StationWidgetFragment : BaseFragment<WidgetStationBinding>(WidgetStationBinding::inflate) {

    private val viewModel by viewModels<BusViewModel>()

    private val nextStationAdapter by lazy { StationAdapter() }
    private val previousStationAdapter by lazy { StationAdapter() }

    private var list = ArrayList<BusViewModel.StationDto>()
    private var firstItem = BusViewModel.StationDto()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvNextStations.adapter = nextStationAdapter
        binding.rvPreviousStations.adapter = previousStationAdapter


        list.addAll(viewModel.nextStations())

        firstItem = list[0]
        list.removeAt(0)
        list.add(list.size, firstItem)

        binding.tvCurrentStation.text = firstItem.stationName
        binding.tvNextStation.text = firstItem.stationName

        nextStationAdapter.submitList(list)
        previousStationAdapter.submitList(list)

        updateStation()

        val currentTime = System.currentTimeMillis()
        binding.tvDayOfTheWeek.text = getDayOfWeek(currentTime)

        updateTime()

    }

    private fun updateTime() {
        val currentTime = Calendar.getInstance().time
        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val formattedTime = timeFormat.format(currentTime)
        binding.tvCurrentTime.text = formattedTime
        lifecycleScope.launch {
            delay(1000)
            updateTime()
        }
    }

    private fun getDayOfWeek(currentTimeMillis: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = currentTimeMillis

        return when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.SUNDAY -> "Sunday"
            Calendar.MONDAY -> "Monday"
            Calendar.TUESDAY -> "Tuesday"
            Calendar.WEDNESDAY -> "Wednesday"
            Calendar.THURSDAY -> "Thursday"
            Calendar.FRIDAY -> "Friday"
            Calendar.SATURDAY -> "Saturday"
            else -> "Invalid day"
        }
    }

    private fun updateStation(): List<BusViewModel.StationDto> {

        lifecycleScope.launch {
            delay(20_000)

            firstItem = list[0]
            list.removeAt(0)
            list.add(list.size, firstItem)

            nextStationAdapter.submitList(list)
            previousStationAdapter.submitList(list)

            nextStationAdapter.notifyDataSetChanged()
            previousStationAdapter.notifyDataSetChanged()

            binding.tvCurrentStation.text = firstItem.stationName
            binding.tvNextStation.text = firstItem.stationName

            updateStation()
        }

        return list
    }

}