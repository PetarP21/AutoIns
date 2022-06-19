package com.example.diplomna.ui

import DatabaseHandler
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.diplomna.R
import com.example.diplomna.databinding.FragmentLoginBinding
import com.example.diplomna.models.Employee
import com.example.diplomna.util.SHA256
import com.example.diplomna.util.SharedPref

class LoginFragment : Fragment() {
    private lateinit var binding : FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val databaseHandler = DatabaseHandler(requireContext())
        val isRemembered = SharedPref.readBoolean("CHECKBOX")
        val nickname = SharedPref.readString("NICKNAME")
        if(isRemembered){
            when(databaseHandler.getPositionByNickname(nickname)){
                "Админ" -> findNavController().navigate(R.id.action_loginFragment_to_adminOperationsFragment)
                "Застраховател" -> findNavController().navigate(R.id.action_loginFragment_to_employeeOperationsFragment)
                else -> {
                    Toast.makeText(context,"Error!", Toast.LENGTH_LONG).show()
                }
            }
        }
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_login,
            container,
            false
        )
        binding.loginButton.setOnClickListener {
           login()
        }
        binding.forgottenPassword.setOnClickListener{
            findNavController().navigate(R.id.action_loginFragment_to_forgottenPasswordFragment)
        }
        return binding.root
    }

    private fun login(){
        val databaseHandler = DatabaseHandler(requireContext())
        val nickname = binding.nickname.editText?.text.toString()
        val password = binding.password.editText?.text.toString()
        val rememberMe = binding.rememberMe.isChecked
        SharedPref.write(nickname,rememberMe)
        val salt = databaseHandler.getSaltByNickname(nickname)
        val isValid = databaseHandler.checkLogin(nickname, SHA256.encrypt(salt+password))
        if(isValid){

            when(databaseHandler.getPositionByNickname(nickname)){
                "Админ" -> {
                    val action = LoginFragmentDirections.actionLoginFragmentToAdminOperationsFragment(nickname)
                    findNavController().navigate(action)
                }
                "Застраховател" -> {
                    val action = LoginFragmentDirections.actionLoginFragmentToEmployeeOperationsFragment(nickname)
                    findNavController().navigate(action)
                }
                else -> {
                    Toast.makeText(context,"Error!",Toast.LENGTH_LONG).show()
                }
            }
        } else {
            Toast.makeText(context,"Грешно име и/или парола.",Toast.LENGTH_LONG).show()
        }
    }
}