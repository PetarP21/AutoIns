package com.example.diplomna.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import com.example.diplomna.R

class SplashFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Looper.myLooper()?.let {
            Handler(it).postDelayed({
                    findNavController().navigate(R.id.action_splashFragment_to_mainFragment)
            }, 2500)
        }
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }
}