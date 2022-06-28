package com.example.diplomna.ui

import DatabaseHandler
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.*
import android.widget.EditText
import android.widget.SearchView
import android.widget.SearchView.OnQueryTextListener
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.diplomna.MainActivity
import com.example.diplomna.R
import com.example.diplomna.databinding.FragmentShowEmployersBinding
import com.example.diplomna.models.Employee
import com.example.diplomna.util.EmployeeAdapter


/* Може да направиш така когато влезеш в даден профил на дадения служител
служителя да остава логнат докато не излезе от профила си
Ще има бутонче на профил, през който можеш да излизаш от профила си
*/
class ShowEmployersFragment : Fragment() {
    private lateinit var binding: FragmentShowEmployersBinding
    private lateinit var itemAdapter: EmployeeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_show_employers,
            container,
            false
        )
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)
        setupListOfDataIntoRecyclerView()
        return binding.root
    }

    private fun setupListOfDataIntoRecyclerView() {

        if (getItemsList().size > 0) {

            binding.rvItemsList.visibility = View.VISIBLE
            binding.tvNoRecordsAvailable.visibility = View.GONE

            // Set the LayoutManager that this RecyclerView will use.
            binding.rvItemsList.layoutManager = LinearLayoutManager(requireContext())
            // Adapter class is initialized and list is passed in the param.
            itemAdapter = EmployeeAdapter(requireContext(), getItemsList())
            // adapter instance is set to the recyclerview to inflate the items.
            binding.rvItemsList.adapter = itemAdapter
        } else {
            binding.rvItemsList.visibility = View.GONE
            binding.tvNoRecordsAvailable.visibility = View.VISIBLE
        }
    }

    private fun getItemsList(): ArrayList<Employee> {
        //creating the instance of DatabaseHandler class
        val databaseHandler = DatabaseHandler(requireContext())
        //calling the viewEmployee method of DatabaseHandler class to read the records
        return databaseHandler.viewEmployee()
    }

    fun deleteEmployeeRecordAlertDialog(employee: Employee) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Delete Record")
        builder.setMessage("Are you sure you wants to delete ${employee.nickname}.")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton("Yes") { dialogInterface, _ ->

            val databaseHandler = DatabaseHandler(requireContext())
            val status = databaseHandler.deleteEmployee(
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
                    employee.password
                )
            )
            if (status > -1) {
                Toast.makeText(
                    context,
                    "Record deleted successfully.",
                    Toast.LENGTH_LONG
                ).show()

                setupListOfDataIntoRecyclerView()
            }

            dialogInterface.dismiss() // Dialog will be dismissed
        }
        builder.setNegativeButton("No") { dialogInterface, _ ->
            dialogInterface.dismiss() // Dialog will be dismissed
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false) // Will not allow user to cancel after clicking on remaining screen area.
        alertDialog.show()  // show the dialog to UI
    }

    fun updateEmployeeRecordDialog(employee: Employee) {
        val updateDialog = Dialog(requireContext(), R.style.Theme_Dialog)
        updateDialog.setCancelable(false)
        /*Set the screen content from a layout resource.
         The resource will be inflated, adding all top-level views to the screen.*/
        updateDialog.setContentView(R.layout.employee_update)

        val nicknameEditText =
            updateDialog.findViewById<EditText>(R.id.nickname_editText_update_emp)
        val firstNameEditText =
            updateDialog.findViewById<EditText>(R.id.firstName_editText_update_emp)
        val middleNameEditText =
            updateDialog.findViewById<EditText>(R.id.middleName_editText_update_emp)
        val lastNameEditText =
            updateDialog.findViewById<EditText>(R.id.lastName_editText_update_emp)
        val emailEditText = updateDialog.findViewById<EditText>(R.id.email_editText_update_emp)
        val updateButton = updateDialog.findViewById<TextView>(R.id.employee_update_textView)
        val cancelButton = updateDialog.findViewById<TextView>(R.id.employee_cancel_textView)


        nicknameEditText.setText(employee.nickname)
        firstNameEditText.setText(employee.firstName)
        middleNameEditText.setText(employee.middleName)
        lastNameEditText.setText(employee.lastName)
        emailEditText.setText(employee.email)

        updateButton.setOnClickListener {

            val nickname = nicknameEditText.text.toString()
            val firstName = firstNameEditText.text.toString()
            val middleName = middleNameEditText.text.toString()
            val lastName = lastNameEditText.text.toString()
            val email = emailEditText.text.toString()

            val databaseHandler = DatabaseHandler(requireContext())

            if (nickname.isNotEmpty() && firstName.isNotEmpty() && middleName.isNotEmpty() && lastName.isNotEmpty() && email.isNotEmpty()) {
                var isNickNameAvailable = false
                var isEmailAvailable = false
                var isEmailValid = false
                if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    isEmailValid = true
                } else {
                    emailEditText.error = "Невалиден имейл адрес."
                }
                if (databaseHandler.getEmployeeByNickname(nickname) != null && nickname != employee.nickname) {
                    nicknameEditText.error = "Вече съществува потребител с това потребителско име."
                } else {
                    isNickNameAvailable = true
                }

                if (databaseHandler.getEmployeeByEmail(email) != null && email != employee.email) {
                    emailEditText.error = "Вече съществува потребител с този имейл."
                } else {
                    isEmailAvailable = true
                }
                if (isEmailAvailable && isNickNameAvailable && isEmailValid) {
                    val status =
                        databaseHandler.updateEmployee(
                            Employee(
                                employee.id,
                                nickname,
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

                        setupListOfDataIntoRecyclerView()

                        updateDialog.dismiss() // Dialog will be dismissed
                    }
                } else {
                    Toast.makeText(context, "Не всички полета са попълнени.", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
        cancelButton.setOnClickListener {
            updateDialog.dismiss()
        }
        //Start the dialog and display it on screen.
        updateDialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.item_menu, menu)
        val searchView = SearchView((activity as MainActivity).supportActionBar?.themedContext ?: context)
        searchView.queryHint = "Въведете потр. име"
        menu.findItem(R.id.action_search).apply {
            setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW or MenuItem.SHOW_AS_ACTION_IF_ROOM)
            actionView = searchView
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                itemAdapter.filter.filter(newText)
                return false
            }
        })
    }
}