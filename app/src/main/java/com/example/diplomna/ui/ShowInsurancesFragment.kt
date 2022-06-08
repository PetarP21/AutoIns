package com.example.diplomna.ui

import DatabaseHandler
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.diplomna.R
import com.example.diplomna.databinding.FragmentShowInsurancesBinding
import com.example.diplomna.models.Employee
import com.example.diplomna.models.Vehicle
import com.example.diplomna.util.EmployeeAdapter
import com.example.diplomna.util.VehicleAdapter


class ShowInsurancesFragment : Fragment() {
    private lateinit var binding: FragmentShowInsurancesBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_show_insurances,
            container,
            false
        )
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbarIns)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setupListOfDataIntoRecyclerView()
        return binding.root
    }

    private fun setupListOfDataIntoRecyclerView() {

        if (getItemsList().size > 0) {

            binding.rvItemsListIns.visibility = View.VISIBLE
            binding.tvNoRecordsAvailableIns.visibility = View.GONE

            // Set the LayoutManager that this RecyclerView will use.
            binding.rvItemsListIns.layoutManager = LinearLayoutManager(requireContext())
            // Adapter class is initialized and list is passed in the param.
            val vehicleAdapter = VehicleAdapter(requireContext(), getItemsList())
            // adapter instance is set to the recyclerview to inflate the items.
            binding.rvItemsListIns.adapter = vehicleAdapter
        } else {
            binding.rvItemsListIns.visibility = View.GONE
            binding.tvNoRecordsAvailableIns.visibility = View.VISIBLE
        }
    }


    private fun getItemsList(): ArrayList<Vehicle> {
        //creating the instance of DatabaseHandler class
        val databaseHandler = DatabaseHandler(requireContext())
        //calling the viewEmployee method of DatabaseHandler class to read the records
        return databaseHandler.getAllVehicles(requireContext())
    }

    fun deleteInsuranceRecordAlertDialog(vehicle: Vehicle) {
        val builder = AlertDialog.Builder(requireContext())
        //set title for alert dialog
        builder.setTitle("Изтриване")
        //set message for alert dialog
        builder.setMessage("Сигурни ли сте че искате да изтриете ${vehicle.licencePlate}?")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("Да") { dialogInterface, _ ->

            //creating the instance of DatabaseHandler class
            val databaseHandler = DatabaseHandler(requireContext())
            //calling the deleteEmployee method of DatabaseHandler class to delete record
            val status = databaseHandler.deleteVehicle(
                Vehicle(vehicle.id, vehicle.clientId, vehicle.licencePlate,vehicle.VIN,
                vehicle.registrationCertificate,vehicle.engine,vehicle.type,vehicle.brand,vehicle.model,vehicle.date,vehicle.price,vehicle.isValid))
            if (status > -1) {
                Toast.makeText(
                    context,
                    "Успешно изтрита застраховка.",
                    Toast.LENGTH_LONG
                ).show()

                setupListOfDataIntoRecyclerView()
            }

            dialogInterface.dismiss() // Dialog will be dismissed
        }
        //performing negative action
        builder.setNegativeButton("Не") { dialogInterface, _ ->
            dialogInterface.dismiss() // Dialog will be dismissed
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false) // Will not allow user to cancel after clicking on remaining screen area.
        alertDialog.show()  // show the dialog to UI
    }

}