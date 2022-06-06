package com.example.diplomna.ui

import DatabaseHandler
import android.os.Bundle
import android.text.InputFilter
import android.text.InputFilter.AllCaps
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.diplomna.R
import com.example.diplomna.databinding.FragmentEmployeeOperationsBinding
import com.example.diplomna.models.Client
import com.example.diplomna.models.Vehicle
import com.example.diplomna.models.VehicleTypes
import com.example.diplomna.util.SharedPref
import java.text.SimpleDateFormat
import java.util.*

/*
1 - бутон за профил както при другите админ операциите
2 - 2 бутона за "добавяне на застраховка" и "преглед на всички застраховки"

 */
class EmployeeOperationsFragment : Fragment() {
    private lateinit var binding : FragmentEmployeeOperationsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_employee_operations,
            container,
            false
        )
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
        val currentDate = simpleDateFormat.format(Date())
        val PINs = DatabaseHandler(requireContext()).getPINs().toTypedArray()
        val PINsAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, PINs)

        val licensePlates = DatabaseHandler(requireContext()).getLicensePlates().toTypedArray()
        val licensePlatesAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, licensePlates)

        binding.licensePlates.filters += InputFilter.AllCaps()

        binding.dateEditText.setText(currentDate)
        binding.price.editText?.setText("100")
        binding.addInsurance.setOnClickListener {
            addInsuranceRecord()
            setPINs(PINsAdapter)
        }
        binding.logout.setOnClickListener {
            SharedPref.clear()
            findNavController().navigate(R.id.action_employeeOperationsFragment_to_loginFragment)
        }
        binding.PINs.setOnItemClickListener { _, _, position, _ ->
            val PIN = PINsAdapter.getItem(position) ?: ""
            setDataByPIN(PIN)
        }
        binding.licensePlates.setOnItemClickListener { _, _, position, _ ->
            val licensePlate = licensePlatesAdapter.getItem(position) ?: ""
            setDataByLicensePlate(licensePlate)
        }
        setPINs(PINsAdapter)
        setLicensePlates(licensePlatesAdapter)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        setVehicleTypes()
    }

    private fun setPINs(adapter:  ArrayAdapter<String>){
        binding.PINs.setAdapter(adapter)
    }

    private fun setLicensePlates(adapter: ArrayAdapter<String>){
        binding.licensePlates.setAdapter(adapter)
    }

    private fun setDataByPIN(PIN : String){
        val client = DatabaseHandler(requireContext()).getClientByPIN(PIN)
        with(binding){
            firstnameClient.editText?.setText(client.firstName)
            middlenameClient.editText?.setText(client.middleName)
            lastnameClient.editText?.setText(client.lastName)
        }
    }

    private fun setDataByLicensePlate(licensePlate : String){
        val vehicle = DatabaseHandler(requireContext()).getVehicleByLicensePlate(licensePlate,requireContext())
        with(binding){
            vinVehicle.editText?.setText(vehicle.VIN)
            registrationCertificate.editText?.setText(vehicle.registrationCertificate)
            engine.editText?.setText(vehicle.engine.toString())
            typeVehicle.editText?.setText(getString(vehicle.type.id))
            brand.editText?.setText(vehicle.brand)
            model.editText?.setText(vehicle.model)
            date.editText?.setText(vehicle.date)
            price.editText?.setText(vehicle.price.toString())
        }
    }

    private fun setVehicleTypes(){
        val vehicleTypes = VehicleTypes.values().map { it.id }.map { getString(it) }
        val arrayAdapter = ArrayAdapter(requireContext(),R.layout.dropdown_item,vehicleTypes)
        with(binding.typesVehicle){
            setAdapter(arrayAdapter)
            setText((arrayAdapter.getItem(0).toString()), false)
        }
    }

    private fun addInsuranceRecord(){
        with(binding) {
            val pin = pinClient.editText?.text.toString()
            val firstName = firstnameClient.editText?.text.toString()
            val middleName = middlenameClient.editText?.text.toString()
            val lastName = lastnameClient.editText?.text.toString()

            val licencePlate = licensePlate.editText?.text.toString()
            val VIN = vinVehicle.editText?.text.toString()
            val regCertificate = registrationCertificate.editText?.text.toString()
            val engineStr = engine.editText?.text.toString()
            val vehicleType = VehicleTypes.values().first { getString(it.id) == typeVehicle.editText?.text.toString() }
            val brand = brand.editText?.text.toString()
            val model = model.editText?.text.toString()
            val date = date.editText?.text.toString()
            val price = price.editText?.text.toString()

            val databaseHandler = DatabaseHandler(requireContext())
            if(pin.isNotEmpty() && firstName.isNotEmpty() && middleName.isNotEmpty() && lastName.isNotEmpty()
                && licencePlate.isNotEmpty() && VIN.isNotEmpty() && regCertificate.isNotEmpty()
                && engineStr.isNotEmpty() && brand.isNotEmpty() && model.isNotEmpty() && date.isNotEmpty() && price.isNotEmpty()) {
                val statusClient = databaseHandler.addClient(
                    Client(0,pin,firstName,middleName,lastName)
                )
                val statusVehicle = databaseHandler.addVehicle(requireContext(),
                    Vehicle(0,databaseHandler.getIdByPIN(pin),licencePlate,VIN,regCertificate,engineStr.toInt(),vehicleType,brand,model,date,price.toDouble(),true)
                )
                if(statusClient > -1 && statusVehicle > -1) {
                    Toast.makeText(context,"Record saved", Toast.LENGTH_LONG).show()
                    with(binding) {
                        pinClient.editText?.text?.clear()
                        firstnameClient.editText?.text?.clear()
                        middlenameClient.editText?.text?.clear()
                        lastnameClient.editText?.text?.clear()
                        licensePlate.editText?.text?.clear()
                        vinVehicle.editText?.text?.clear()
                        registrationCertificate.editText?.text?.clear()
                        engine.editText?.text?.clear()
                        binding.brand.editText?.text?.clear()
                        binding.model.editText?.text?.clear()
                        binding.date.editText?.text?.clear()
                    }
                }
            } else {
                Toast.makeText(context, "Something is blank.", Toast.LENGTH_LONG).show()
            }
        }
    }
}