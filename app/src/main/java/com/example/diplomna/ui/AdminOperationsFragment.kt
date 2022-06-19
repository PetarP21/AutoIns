package com.example.diplomna.ui

import DatabaseHandler
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.diplomna.R
import com.example.diplomna.databinding.FragmentAdminOperationsBinding
import com.example.diplomna.models.Employee
import com.example.diplomna.models.SecurityQuestions
import com.example.diplomna.models.VehicleTypes
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
        val username = AdminOperationsFragmentArgs.fromBundle(requireArguments()).username

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
        val middleName = binding.middlenameEmployee.editText?.text.toString()
        val lastName = binding.lastnameEmployee.editText?.text.toString()
        val email = binding.emailEmployee.editText?.text.toString()
        val position = getCheckedRadioButton().text.toString()
        val databaseHandler = DatabaseHandler(requireContext())
        if (nickname.isNotEmpty() && firstName.isNotEmpty() && middleName.isNotEmpty()
            && lastName.isNotEmpty() && email.isNotEmpty() && position.isNotEmpty()
        ) {
            var isNickNameAvailable = false
            var isEmailAvailable = false
            var isEmailValid = false
            if(Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                isEmailValid = true
            } else {
                Toast.makeText(
                    context,
                    "Невалиден имейл адрес.",
                    Toast.LENGTH_LONG
                ).show()
            }
            if (databaseHandler.getEmployeeByNickname(nickname) != null) {
                Toast.makeText(
                    context,
                    "Вече съществува потребител с това потребителско име.",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                isNickNameAvailable = true
            }
            if (databaseHandler.getEmployeeByEmail(email) != null) {
                Toast.makeText(
                    context,
                    "Вече съществува потребител с този имейл.",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                isEmailAvailable = true
            }
            if (isEmailAvailable && isNickNameAvailable && isEmailValid) {
                val salt = SHA256.salt()
                val status = databaseHandler.addEmployee(
                    Employee(
                        0,
                        nickname,
                        firstName,
                        middleName,
                        lastName,
                        email,
                        position,
                        getString(SecurityQuestions.PET.id),
                        SHA256.encrypt(salt + "admin"),
                        salt,
                        SHA256.encrypt(salt + "admin")
                    )
                )
                if (status > -1) {
                    Toast.makeText(context, "Служителят е записан успешно.", Toast.LENGTH_LONG).show()
                    binding.usernameEmployee.editText?.text?.clear()
                    binding.firstnameEmployee.editText?.text?.clear()
                    binding.middlenameEmployee.editText?.text?.clear()
                    binding.lastnameEmployee.editText?.text?.clear()
                    binding.emailEmployee.editText?.text?.clear()
                }
            }
        } else {
            Toast.makeText(context, "Something is blank.", Toast.LENGTH_LONG).show()
        }
    }

    private fun getCheckedRadioButton() =
        binding.root.findViewById<RadioButton>(binding.positionRadioGroup.checkedRadioButtonId)
}