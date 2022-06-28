package com.example.diplomna.ui

import DatabaseHandler
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.diplomna.R
import com.example.diplomna.databinding.FragmentForgottenPasswordBinding
import com.example.diplomna.models.Employee
import com.example.diplomna.models.SecurityQuestions
import com.example.diplomna.util.SHA256

class ForgottenPasswordFragment : Fragment() {
    private lateinit var binding : FragmentForgottenPasswordBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_forgotten_password,
            container,
            false
        )
        binding.resetPassword.setOnClickListener {
            reset()
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        setSecurityQuestions()
    }

    private fun setSecurityQuestions() {
        val securityQuestions = SecurityQuestions.values().map { it.id }.map { getString(it) }
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, securityQuestions)
        with(binding.questions) {
            setAdapter(arrayAdapter)
            setText((arrayAdapter.getItem(0).toString()), false)
        }
    }

    private fun reset(){
        val databaseHandler = DatabaseHandler(requireContext())
        val username = binding.usernameForgottenPasswordTextInput.editText?.text.toString()
        val securityQuestion = binding.questionsTextInput.editText?.text.toString()
        val securityAnswer = binding.answerTextInput.editText?.text.toString()
        val salt = databaseHandler.getSaltByNickname(username)
        val isValid = databaseHandler.checkPasswordReset(username,databaseHandler.getIdBySecurityQuestion(securityQuestion),SHA256.encrypt(salt+securityAnswer))
        if(isValid){
            val employee = databaseHandler.getEmployeeByNickname(username)
            if(employee != null) {
                databaseHandler.updateEmployee(Employee(employee.id,employee.nickname,employee.firstName,employee.middleName,employee.lastName,employee.email,
                    employee.positionId,employee.securityQuestionId,employee.securityAnswer,employee.salt,SHA256.encrypt(employee.salt+"admin")))
            }
            Toast.makeText(context,"Успешно нулирана парола. Новата ви парола е 'admin'. Моля, променете при следващо влизане в профила.", Toast.LENGTH_LONG).show()
            with(binding){
                usernameForgottenPasswordTextInput.editText?.text?.clear()
                answerTextInput.editText?.text?.clear()
            }
        } else {
            Toast.makeText(context,"Данните не съвпадат.", Toast.LENGTH_LONG).show()
        }
    }
}