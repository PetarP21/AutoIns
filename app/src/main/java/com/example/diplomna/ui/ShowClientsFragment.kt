package com.example.diplomna.ui

import DatabaseHandler
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.diplomna.MainActivity
import com.example.diplomna.R
import com.example.diplomna.databinding.FragmentShowClientsBinding
import com.example.diplomna.models.Client
import com.example.diplomna.util.BaseFragment
import com.example.diplomna.util.ClientAdapter

class ShowClientsFragment : BaseFragment() {
    private lateinit var binding: FragmentShowClientsBinding
    private lateinit var clientAdapter: ClientAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_show_clients,
            container,
            false
        )
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbarClient)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        deleteUnusedClients()
        setHasOptionsMenu(true)
        setupListOfDataIntoRecyclerView()
        return binding.root
    }

    private fun deleteUnusedClients() {
        val databaseHandler = DatabaseHandler(requireContext())
        val clients = databaseHandler.getAllClients()
        val vehicles = databaseHandler.getAllVehicles()
        var counter = 0
        for (client in clients) {
            for (vehicle in vehicles) {
                if (client.id == vehicle.clientId) {
                    counter++
                }
            }
            if (counter < 1) {
                databaseHandler.deleteClient(client)
            }
            counter = 0
        }
    }

    private fun setupListOfDataIntoRecyclerView() {
        if (getItemsList().size > 0) {

            binding.rvItemsListClient.visibility = View.VISIBLE
            binding.tvNoRecordsAvailableClient.visibility = View.GONE

            // Set the LayoutManager that this RecyclerView will use.
            binding.rvItemsListClient.layoutManager = LinearLayoutManager(requireContext())
            // Adapter class is initialized and list is passed in the param.
            clientAdapter = ClientAdapter(requireContext(), getItemsList())
            // adapter instance is set to the recyclerview to inflate the items.
            binding.rvItemsListClient.adapter = clientAdapter
        } else {
            binding.rvItemsListClient.visibility = View.GONE
            binding.tvNoRecordsAvailableClient.visibility = View.VISIBLE
        }
    }

    private fun getItemsList(): ArrayList<Client> {
        //creating the instance of DatabaseHandler class
        val databaseHandler = DatabaseHandler(requireContext())
        //calling the viewEmployee method of DatabaseHandler class to read the records
        return databaseHandler.getAllClients()
    }

    fun updateClientRecordDialog(client: Client) {
        val updateDialog = Dialog(requireContext(), R.style.Theme_Dialog)
        updateDialog.setCancelable(false)
        /*Set the screen content from a layout resource.
         The resource will be inflated, adding all top-level views to the screen.*/
        updateDialog.setContentView(R.layout.client_update)

        // val pin = updateDialog.findViewById<EditText>(R.id.pin_editText_update)
        val firstName = updateDialog.findViewById<EditText>(R.id.firstName_editText_update)
        val middleName = updateDialog.findViewById<EditText>(R.id.middleName_editText_update)
        val lastName = updateDialog.findViewById<EditText>(R.id.lastName_editText_update)
        val updateButton = updateDialog.findViewById<TextView>(R.id.client_update_textView)
        val cancelButton = updateDialog.findViewById<TextView>(R.id.client_cancel_textView)

        /*  pin.setText(client.PIN)
          val oldPin = client.PIN */
        firstName.setText(client.firstName)
        middleName.setText(client.middleName)
        lastName.setText(client.lastName)

        updateButton.setOnClickListener {

            // val pin_str = pin.text.toString()
            val firstName = firstName.text.toString()
            val middleName = middleName.text.toString()
            val lastName = lastName.text.toString()


            val databaseHandler = DatabaseHandler(requireContext())

            if (firstName.isNotEmpty() && middleName.isNotEmpty() && lastName.isNotEmpty()) {
                val status =
                    databaseHandler.updateClient(
                        Client(
                            client.id,
                            client.PIN,
                            firstName,
                            middleName,
                            lastName
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

    fun deleteClientRecordAlertDialog(client: Client) {
        val builder = AlertDialog.Builder(requireContext())
        //set title for alert dialog
        builder.setTitle("Изтриване")
        //set message for alert dialog
        builder.setMessage("Сигурни ли сте че искате да изтриете ${client.firstName} ${client.middleName} ${client.lastName} с ЕГН: ${client.PIN}?")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("Да") { dialogInterface, _ ->

            //creating the instance of DatabaseHandler class
            val databaseHandler = DatabaseHandler(requireContext())
            //calling the deleteEmployee method of DatabaseHandler class to delete record
            val status = databaseHandler.deleteClient(
                Client(client.id, client.PIN, client.firstName, client.middleName, client.lastName)
            )
            if (status > -1) {
                Toast.makeText(
                    context,
                    "Успешно изтрит клиент.",
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.item_menu, menu)
        val searchView =
            SearchView((activity as MainActivity).supportActionBar?.themedContext ?: context)
        searchView.queryHint = "Въведете ЕГН"
        menu.findItem(R.id.action_search).apply {
            setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW or MenuItem.SHOW_AS_ACTION_IF_ROOM)
            actionView = searchView
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                clientAdapter.filter.filter(newText)
                return false
            }
        })
    }
}