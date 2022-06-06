package com.example.diplomna.ui

import DatabaseHandler
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.diplomna.R
import com.example.diplomna.databinding.FragmentAdminOperationsBinding
import com.example.diplomna.models.Employee
import com.example.diplomna.util.SHA256
import com.example.diplomna.util.SharedPref

class AdminOperationsFragment : Fragment() {
    private lateinit var binding: FragmentAdminOperationsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_admin_operations,
            container,
            false
        )
        binding.addEmployee.setOnClickListener { addRecord() }
        binding.viewEmployers.setOnClickListener {
            findNavController().navigate(R.id.action_adminOperationsFragment_to_showEmployersFragment)
        }
        binding.logout.setOnClickListener {
            SharedPref.clear()
            findNavController().navigate(R.id.action_adminOperationsFragment_to_loginFragment)
        }
        return binding.root
    }

    private fun addRecord() {
        val nickname = binding.usernameEmployee.editText?.text.toString()
        val firstName = binding.firstnameEmployee.editText?.text.toString()
        val lastName = binding.lastnameEmployee.editText?.text.toString()
        val position = getCheckedRadioButton().text.toString()
        val databaseHandler = DatabaseHandler(requireContext())
        if(nickname.isNotEmpty() && firstName.isNotEmpty() && lastName.isNotEmpty() && position.isNotEmpty()) {
            val salt = SHA256.salt()
            val status = databaseHandler.addEmployee(Employee(0,nickname, firstName, lastName,position,salt,SHA256.encrypt(salt+"admin")))
            if(status > -1) {
                Toast.makeText(context,"Record saved",Toast.LENGTH_LONG).show()
                binding.usernameEmployee.editText?.text?.clear()
                binding.firstnameEmployee.editText?.text?.clear()
                binding.lastnameEmployee.editText?.text?.clear()

                // setup
            }
        } else {
            Toast.makeText(context, "Something is blank.",Toast.LENGTH_LONG).show()
        }
    }

    private fun getCheckedRadioButton() =
        binding.root.findViewById<RadioButton>(binding.positionRadioGroup.checkedRadioButtonId)
}