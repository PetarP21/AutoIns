package com.example.diplomna.ui

import DatabaseHandler
import android.os.Bundle
import android.text.InputType
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.diplomna.R
import com.example.diplomna.databinding.FragmentUpdateProfileBinding
import com.example.diplomna.models.Employee
import com.example.diplomna.models.SecurityQuestions
import com.example.diplomna.util.SHA256
import com.example.diplomna.util.SharedPref
import java.util.regex.Pattern

class UpdateProfileFragment : Fragment() {
    private lateinit var binding: FragmentUpdateProfileBinding
    private lateinit var username: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_update_profile,
            container,
            false
        )
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val databaseHandler = DatabaseHandler(requireContext())
        username = SharedPref.readString("NICKNAME")
        val employee = databaseHandler.getEmployeeByNickname(username)
        if (employee != null) {
            with(binding) {
                usernameEmployee.editText?.setText(employee.nickname)
                usernameEmployee.editText?.inputType = InputType.TYPE_NULL
                usernameEmployee.boxBackgroundColor =
                    ContextCompat.getColor(requireContext(), R.color.light_gray)

                firstnameEmployee.editText?.setText(employee.firstName)
                middlenameEmployee.editText?.setText(employee.middleName)
                lastnameEmployee.editText?.setText(employee.lastName)
                emailEmployee.editText?.setText(employee.email)
            }
        }
        binding.updateProfileData.setOnClickListener {
            if (employee != null) {
                updateProfileData(employee)
            }
        }
        binding.updateSecurityAnswer.setOnClickListener {
            if (employee != null) {
                updateSecurityAnswer(employee)
            }
        }
        binding.updateProfilePassword.setOnClickListener {
            if (employee != null) {
                updatePassword(employee)
            }
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
        with(binding.secQuestion) {
            setAdapter(arrayAdapter)
            setText((arrayAdapter.getItem(0).toString()), false)
        }
    }

    private fun updatePassword(employee: Employee) {
        val oldPassword = binding.oldPassword.editText?.text.toString()
        val newPassword = binding.newPassword.editText?.text.toString()
        val repeatedPassword = binding.newPasswordRepeat.editText?.text.toString()
        val databaseHandler = DatabaseHandler(requireContext())

        if (oldPassword.isNotEmpty() && newPassword.isNotEmpty() && repeatedPassword.isNotEmpty()) {
            val patternPassword = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,16}\$")
            if (patternPassword.matcher(newPassword).matches()) {
                var arePasswordsEqual = false
                var isOldPasswordValid = false
                if (newPassword == repeatedPassword) {
                    arePasswordsEqual = true
                } else {
                    Toast.makeText(requireContext(), "Паролите не съвпадат.", Toast.LENGTH_LONG)
                        .show()
                }

                if (SHA256.encrypt(employee.salt + oldPassword) == employee.password) {
                    isOldPasswordValid = true
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Въведете правилната парола.",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }

                if (arePasswordsEqual && isOldPasswordValid) {
                    val status =
                        databaseHandler.updateEmployee(
                            Employee(
                                employee.id,
                                employee.nickname,
                                employee.firstName,
                                employee.middleName,
                                employee.lastName,
                                employee.email,
                                employee.positionId,
                                employee.securityQuestionId,
                                employee.securityAnswer,
                                employee.salt,
                                SHA256.encrypt(employee.salt + newPassword)
                            )
                        )
                    if (status > -1) {
                        Toast.makeText(
                            requireContext(),
                            "Успешно сменена парола.",
                            Toast.LENGTH_LONG
                        )
                            .show()
                        binding.oldPassword.editText?.text?.clear()
                        binding.newPassword.editText?.text?.clear()
                        binding.newPasswordRepeat.editText?.text?.clear()
                        SharedPref.writeUsername(username)
                    }
                }
            } else {
                Toast.makeText(
                    context,
                    "Паролата трябва е с дължина от 8 до 16 символа. Минимум 1 главна буква, 1 малка буква и 1 цифра.",
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        } else {
            Toast.makeText(context, "Не всички полета са попълнени.", Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun updateSecurityAnswer(employee: Employee) {
        val securityQuestion = binding.secQuestions.editText?.text.toString()
        val securityAnswer = binding.secAnswer.editText?.text.toString()

        val databaseHandler = DatabaseHandler(requireContext())
        if (securityAnswer.isNotEmpty()) {
            val status =
                databaseHandler.updateEmployee(
                    Employee(
                        employee.id,
                        employee.nickname,
                        employee.firstName,
                        employee.middleName,
                        employee.lastName,
                        employee.email,
                        employee.positionId,
                        databaseHandler.getIdBySecurityQuestion(securityQuestion),
                        SHA256.encrypt(employee.salt + securityAnswer),
                        employee.salt,
                        employee.password
                    )
                )
            if (status > -1) {
                Toast.makeText(requireContext(), "Записът е редактиран.", Toast.LENGTH_LONG)
                    .show()
                SharedPref.writeUsername(username)
            }
        } else {
            Toast.makeText(context, "Не всички полета са попълнени.", Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun updateProfileData(employee: Employee) {
        val username = binding.usernameEmployee.editText?.text.toString()
        val firstName = binding.firstnameEmployee.editText?.text.toString()
        val middleName = binding.middlenameEmployee.editText?.text.toString()
        val lastName = binding.lastnameEmployee.editText?.text.toString()
        val email = binding.emailEmployee.editText?.text.toString()

        val databaseHandler = DatabaseHandler(requireContext())

        if (username.isNotEmpty() && firstName.isNotEmpty() && middleName.isNotEmpty() && lastName.isNotEmpty() && email.isNotEmpty()) {
            var isNickNameAvailable = false
            var isEmailAvailable = false
            var isEmailValid = false
            if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                isEmailValid = true
            } else {
                binding.emailEmployee.editText?.error = "Невалиден имейл адрес."
            }
            if (databaseHandler.getEmployeeByNickname(username) != null && username != employee.nickname) {
                binding.usernameEmployee.editText?.error =
                    "Вече съществува потребител с това потребителско име."
            } else {
                isNickNameAvailable = true
            }

            if (databaseHandler.getEmployeeByEmail(email) != null && email != employee.email) {
                binding.emailEmployee.editText?.error = "Вече съществува потребител с този имейл."
            } else {
                isEmailAvailable = true
            }
            if (isEmailAvailable && isNickNameAvailable && isEmailValid) {
                val status =
                    databaseHandler.updateEmployee(
                        Employee(
                            employee.id,
                            username,
                            firstName,
                            middleName,
                            lastName,
                            email,
                            employee.positionId,
                            employee.securityQuestionId,
                            employee.securityAnswer,
                            employee.salt,
                            employee.password
                        )
                    )
                if (status > -1) {
                    Toast.makeText(requireContext(), "Записът е редактиран.", Toast.LENGTH_LONG)
                        .show()
                    SharedPref.writeUsername(username)
                }
            }
        } else {
            Toast.makeText(context, "Не всички полета са попълнени.", Toast.LENGTH_LONG)
                .show()
        }
    }
}