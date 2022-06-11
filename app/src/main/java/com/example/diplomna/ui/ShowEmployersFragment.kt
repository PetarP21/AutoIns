package com.example.diplomna.ui

import DatabaseHandler
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.diplomna.R
import com.example.diplomna.databinding.FragmentShowEmployersBinding
import com.example.diplomna.models.Client
import com.example.diplomna.models.Employee
import com.example.diplomna.models.VehicleTypes
import com.example.diplomna.util.EmployeeAdapter


/* Може да направиш така когато влезеш в даден профил на дадения служител
служителя да остава логнат докато не излезе от профила си
Ще има бутонче на профил, през който можеш да излизаш от профила си
*/
class ShowEmployersFragment : Fragment() {
    private lateinit var binding: FragmentShowEmployersBinding
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
        setupListOfDataIntoRecyclerView()
        return binding.root
    }

    /**
     * Function is used to show the list of inserted data.
     */
    private fun setupListOfDataIntoRecyclerView() {

        if (getItemsList().size > 0) {

            binding.rvItemsList.visibility = View.VISIBLE
            binding.tvNoRecordsAvailable.visibility = View.GONE

            // Set the LayoutManager that this RecyclerView will use.
            binding.rvItemsList.layoutManager = LinearLayoutManager(requireContext())
            // Adapter class is initialized and list is passed in the param.
            val itemAdapter = EmployeeAdapter(requireContext(), getItemsList())
            // adapter instance is set to the recyclerview to inflate the items.
            binding.rvItemsList.adapter = itemAdapter
        } else {
            binding.rvItemsList.visibility = View.GONE
            binding.tvNoRecordsAvailable.visibility = View.VISIBLE
        }
    }

    /**
     * Function is used to get the Items List from the database table.
     */
    private fun getItemsList(): ArrayList<Employee> {
        //creating the instance of DatabaseHandler class
        val databaseHandler = DatabaseHandler(requireContext())
        //calling the viewEmployee method of DatabaseHandler class to read the records
        return databaseHandler.viewEmployee()
    }

    /**
     * Method is used to show the Alert Dialog.
     */
    fun deleteEmployeeRecordAlertDialog(employee: Employee) {
        val builder = AlertDialog.Builder(requireContext())
        //set title for alert dialog
        builder.setTitle("Delete Record")
        //set message for alert dialog
        builder.setMessage("Are you sure you wants to delete ${employee.nickname}.")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("Yes") { dialogInterface, _ ->

            //creating the instance of DatabaseHandler class
            val databaseHandler = DatabaseHandler(requireContext())
            //calling the deleteEmployee method of DatabaseHandler class to delete record
            val status = databaseHandler.deleteEmployee(
                Employee(
                    employee.id,
                    employee.nickname,
                    employee.firstName,
                    employee.middleName,
                    employee.lastName,
                    employee.email,
                    employee.position,
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
        //performing negative action
        builder.setNegativeButton("No") { dialogInterface, _ ->
            dialogInterface.dismiss() // Dialog will be dismissed
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false) // Will not allow user to cancel after clicking on remaining screen area.
        alertDialog.show()  // show the dialog to UI
    }

    fun updateEmployeeRecordDialog(employee: Employee) {
        val updateDialog = Dialog(requireContext(), R.style.Theme_Dialog)
        updateDialog.setCancelable(false)
        /*Set the screen content from a layout resource.
         The resource will be inflated, adding all top-level views to the screen.*/
        updateDialog.setContentView(R.layout.employee_update)

        val nickname = updateDialog.findViewById<EditText>(R.id.nickname_editText_update_emp)
        val firstName = updateDialog.findViewById<EditText>(R.id.firstName_editText_update_emp)
        val middleName = updateDialog.findViewById<EditText>(R.id.middleName_editText_update_emp)
        val lastName = updateDialog.findViewById<EditText>(R.id.lastName_editText_update_emp)
        val email = updateDialog.findViewById<EditText>(R.id.email_editText_update_emp)
        val updateButton = updateDialog.findViewById<TextView>(R.id.employee_update_textView)
        val cancelButton = updateDialog.findViewById<TextView>(R.id.employee_cancel_textView)


        nickname.setText(employee.nickname)
        firstName.setText(employee.firstName)
        middleName.setText(employee.middleName)
        lastName.setText(employee.lastName)
        email.setText(employee.email)

        updateButton.setOnClickListener {

            val nickname = nickname.text.toString()
            val firstName = firstName.text.toString()
            val middleName = middleName.text.toString()
            val lastName = lastName.text.toString()
            val email = email.text.toString()

            val databaseHandler = DatabaseHandler(requireContext())

            if (nickname.isNotEmpty() && firstName.isNotEmpty() && middleName.isNotEmpty() && lastName.isNotEmpty() && email.isNotEmpty()) {
                val status =
                    databaseHandler.updateEmployee(
                        Employee(
                            employee.id,
                            nickname,
                            firstName,
                            middleName,
                            lastName,
                            email,
                            employee.position,
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
                Toast.makeText(
                    requireContext(),
                    "Не всички полета са попълнени.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        cancelButton.setOnClickListener {
            updateDialog.dismiss()
        }
        //Start the dialog and display it on screen.
        updateDialog.show()
    }
}