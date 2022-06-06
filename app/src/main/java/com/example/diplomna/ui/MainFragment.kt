package com.example.diplomna.ui

import DatabaseHandler
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.diplomna.R
import com.example.diplomna.databinding.FragmentMainBinding
import com.example.diplomna.util.SharedPref

class MainFragment : Fragment() {
    private lateinit var binding : FragmentMainBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_main,
            container,
            false
        )
        binding.employersButton.setOnClickListener{
            findNavController().navigate(R.id.action_mainFragment_to_loginFragment)
        }
        binding.checkButton.setOnClickListener{
            findNavController().navigate(R.id.action_mainFragment_to_checkFragment)
        }
        return binding.root
    }
}