package org.evolveapp.marathoner.ui.admin.marathon.posts.schedule

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.UiThread
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import kotlinx.coroutines.flow.collect
import org.evolveapp.marathoner.R
import org.evolveapp.marathoner.databinding.FragmentScheduleBinding
import org.evolveapp.marathoner.ui.admin.marathon.posts.add.AddPostActivity
import org.evolveapp.marathoner.utils.*
import timber.log.Timber
import java.util.*


private const val ARG_TIME_MILLIS = "ime_millis"

/**
 * A simple [Fragment] subclass.
 * Use the [ScheduleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ScheduleFragment : BottomSheetDialogFragment(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    private var timeMillis: Long = -1


    private val viewModel by viewModels<ScheduleViewModel>()

    private var binding: FragmentScheduleBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { timeMillis = it.getLong(ARG_TIME_MILLIS, -1) }

        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_schedule, container, false)
        return binding?.root
    }

    private fun sendSelectedTime() {

        when (viewModel.timing.value) {

            Timing.NOW -> {
                (requireActivity() as? AddPostActivity)?.onNowSelected()
                dismiss()
            }

            Timing.LATE -> {

                if (viewModel.year.value == null || viewModel.hourOfDay.value == null) {
                    showToast(R.string.date_and_time_req)
                } else {

                    val scheduledCalendar =
                        viewModel.getScheduledCalendarOrNull() ?: Calendar.getInstance()

                    (requireActivity() as? AddPostActivity)?.onLateSelected(scheduledCalendar.time)
                    dismiss()

                }

            }

        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.initTimeMillis = timeMillis

        binding?.close?.setOnClickListener { dismiss() }

        handleTimingUI()
        handleDateLayout()
        handleTimeLayout()
        handleDoneButton()


    }

    private fun handleDoneButton() {
        binding?.done?.setOnClickListener {

            if (requireActivity()::class.java.interfaces.contains(OnTimeSelectListener::class.java)) {
                sendSelectedTime()
            } else {
                throw IllegalStateException("parent activity should implement OnTimeSelectListener")
            }

        }
    }

    private fun handleDateLayout() {
        binding?.layoutPickDate?.setOnClickListener {

            val dialog = DatePickerDialog.newInstance(
                this,
                viewModel.getScheduledCalendarOrNull() ?: Calendar.getInstance()
            ).apply {
                minDate = Calendar.getInstance()
            }

            dialog.show(childFragmentManager, null)
        }
    }

    private fun handleTimeLayout() {
        binding?.layoutPickTime?.setOnClickListener {
            if (viewModel.year.value == null || viewModel.month.value == null) {
                showToast(R.string.select_date_first)
            } else {

                val c = (viewModel.getScheduledCalendarOrNull() ?: Calendar.getInstance())

                val dialog = TimePickerDialog.newInstance(
                    this,
                    c[Calendar.HOUR_OF_DAY],
                    c[Calendar.MINUTE],
                    0,
                    false
                ).apply {
                    // TODO: 10/10/2021 Handle min time
                }

                dialog.show(childFragmentManager, null)

            }
        }
    }

    private fun handleTimingUI() {

        when (timeMillis) {

            -1L -> {
                Timber.d("-1")
                viewModel.timing.value = Timing.NOW
            }

            else -> {
                Timber.d("not -1")

                val initDate = Date(timeMillis)
                viewModel.setInitialDate(initDate)

                viewModel.timing.value = Timing.LATE

                updateSelectedDateUI()
                updateSelectedTimeUI()

            }

        }

        launchViewLifecycleScope {
            viewModel.timing.collect {
                when (it) {

                    Timing.NOW -> {
                        binding?.layoutLate?.gone()

                        binding?.now?.isChecked = true
                        binding?.late?.isChecked = false

                    }

                    Timing.LATE -> {
                        binding?.layoutLate?.show()


                        binding?.now?.isChecked = false
                        binding?.late?.isChecked = true

                    }

                }
            }
        }

        binding?.timing?.setOnCheckedChangeListener { _, id ->

            when (id) {

                R.id.now -> {
                    viewModel.timing.value = Timing.NOW
                }

                R.id.late -> {
                    viewModel.timing.value = Timing.LATE
                }

            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        binding = null
    }

    companion object {


        interface OnTimeSelectListener {

            fun onNowSelected()

            fun onLateSelected(date: Date)

        }

        enum class Timing {
            NOW,
            LATE
        }


        /**
         * @param timeMillis - current initial time selected, or -1 if no time selected
         */
        @JvmStatic
        fun newInstance(timeMillis: Long) =
            ScheduleFragment().apply {
                arguments = Bundle().apply { putLong(ARG_TIME_MILLIS, timeMillis) }
            }
    }

    override fun onDateSet(view: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int) {

        viewModel.year.value = year
        viewModel.month.value = monthOfYear
        viewModel.day.value = dayOfMonth

        updateSelectedDateUI()

    }

    override fun onTimeSet(view: TimePickerDialog?, hourOfDay: Int, minute: Int, second: Int) {
        viewModel.hourOfDay.value = hourOfDay
        viewModel.minute.value = minute

        updateSelectedTimeUI()

    }

    @UiThread
    private fun updateSelectedDateUI() {
        val formatted = DateUtils.formatDateAsString(
            year = viewModel.year.value ?: 0,
            month = (viewModel.month.value?.plus(1)) ?: 1,
            day = viewModel.day.value ?: 1
        )
        binding?.selectedDate?.text = formatted
    }

    @UiThread
    private fun updateSelectedTimeUI() {

        val formatted = DateUtils.convertTimeToReadable(
            hourIn24 = viewModel.hourOfDay.value ?: 0,
            minute = viewModel.minute.value ?: 0,
            0
        )

        binding?.selectedTime?.text = formatted

    }

}