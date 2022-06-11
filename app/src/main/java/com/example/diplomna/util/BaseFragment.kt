package com.example.diplomna.util

import androidx.fragment.app.Fragment
import java.util.*

abstract class BaseFragment : Fragment() {
     fun calcAge(PIN: String): Int {
        var ageInteger = 0
        if (PIN.count() == 10) {
            var year = PIN.substring(0, 2).toInt()
            var month = PIN.substring(2, 4).toInt()
            val day = PIN.substring(4, 6).toInt()
            if (month > 40) {
                year += 2000
                month -= 40
            } else {
                year += 1900
            }
            val dobCalendar = Calendar.getInstance()
            dobCalendar.set(year, month, day)
            val today = Calendar.getInstance()
            ageInteger = today.get(Calendar.YEAR) - dobCalendar.get(Calendar.YEAR);
            if (today.get(Calendar.MONTH) == dobCalendar.get(Calendar.MONTH)) {
                if (today.get(Calendar.DAY_OF_MONTH) < dobCalendar.get(Calendar.DAY_OF_MONTH)) {
                    ageInteger -= 1;
                }
            } else if (today.get(Calendar.MONTH) < dobCalendar.get(Calendar.MONTH)) {
                ageInteger -= 1;
            }
        }
        return ageInteger
    }

     fun calcPrice(engine: Int, age: Int): Double {
        var price: Double = if (engine < 1200) {
            250.0
        } else if (engine < 1600) {
            252.0
        } else if (engine < 1800) {
            260.0
        } else if (engine < 2000) {
            272.0
        } else if (engine < 2500) {
            334.0
        } else 361.0

        if (age < 25) {
            price += 0.8 * price
        }
        return price
    }
}