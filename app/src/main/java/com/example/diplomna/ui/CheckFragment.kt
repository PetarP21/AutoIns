package com.example.diplomna.ui

import DatabaseHandler
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.diplomna.R
import com.example.diplomna.databinding.FragmentCheckBinding
import com.example.diplomna.models.ValidityOptions
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

class CheckFragment : Fragment() {
    private lateinit var binding: FragmentCheckBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_check,
            container,
            false
        )
        binding.search.setOnClickListener {
            isValid()
        }
        return binding.root
    }

    private fun isValid() {
        val licensePlate = binding.licensePlate.editText?.text.toString()
        if (licensePlate.isNotEmpty()) {
            val databaseHandler = DatabaseHandler(requireContext())
            val patternLicensePlate =
                Pattern.compile("[ABEKMHOPCTYX]{1,2}[0-9]{4}[ABEKMHOPCTYX]{2}")
            if (patternLicensePlate.matcher(licensePlate).matches()) {
                val vehicle =
                    databaseHandler.getVehicleByLicensePlate(licensePlate)
                val validity = databaseHandler.getValidityByVehicle(vehicle)
                var isValid = false
                if (validity.validity == getString(ValidityOptions.YES.id).toInt()) {
                    isValid = true
                }
                if (isValid) {
                    val startDateString = vehicle.date
                    val endDateString = calcEndDate(startDateString)
                    binding.answer.text =
                        "МПС с регистрационен номер ${vehicle.licencePlate} ИМА валидна застраховка 'Гражданска отговорност' до $endDateString включително."
                    binding.isValidImage.setImageResource(R.drawable.ic_check)
                    binding.isValidImage.visibility = View.VISIBLE
                } else {
                    binding.answer.text =
                        "МПС с регистрационен номер $licensePlate НЯМА валидна застраховка 'Гражданска отговорност'."
                    binding.isValidImage.setImageResource(R.drawable.ic__cancel)
                    binding.isValidImage.visibility = View.VISIBLE
                }
            } else {
                binding.licensePlate.editText?.error = "Въведете валиден регистрационен номер."
                binding.isValidImage.visibility = View.INVISIBLE
                binding.answer.text = ""
            }
        } else {
            binding.licensePlate.editText?.error = "Въведете регистрационен номер."
            binding.isValidImage.visibility = View.INVISIBLE
            binding.answer.text = ""
        }
    }

    private fun calcEndDate(startDateString: String) : String{
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
        val startDate = simpleDateFormat.parse(startDateString)
        val oneYearWithoutOneDayInMilliseconds = 31449600000
        var endDateMilliseconds: Long =
            startDate.time + oneYearWithoutOneDayInMilliseconds
        var endDateString = simpleDateFormat.format(Date(endDateMilliseconds))
        val start = Calendar.getInstance()
        if (startDate != null) {
            start.time = startDate
        }
        val startYear = start.get(Calendar.YEAR)
        if (isYearLeap(startYear) || isYearLeap(startYear+1)) {
            endDateMilliseconds += 86400000
            endDateString = simpleDateFormat.format(Date(endDateMilliseconds))
        }
        return endDateString
    }

    private fun isYearLeap(year: Int): Boolean {
        val leap: Boolean = if (year % 4 == 0) {
            if (year % 100 == 0) {
                year % 400 == 0
            } else
                true
        } else
            false
        return leap
    }
}