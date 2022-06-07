package com.example.diplomna.ui

import DatabaseHandler
import android.app.ActionBar
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.slidingpanelayout.widget.SlidingPaneLayout
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
            setLicensePlates(licensePlatesAdapter)
        }
        binding.logout.setOnClickListener {
            SharedPref.clear()
            findNavController().navigate(R.id.action_employeeOperationsFragment_to_loginFragment)
        }
        binding.PINs.setOnItemClickListener { _, _, position, _ ->
            val PIN = PINsAdapter.getItem(position) ?: ""
            setDataByPIN(PIN)
        }
        binding.PINs.addTextChangedListener(textWatcherPIN)
        binding.licensePlates.setOnItemClickListener { _, _, position, _ ->
            val licensePlate = licensePlatesAdapter.getItem(position) ?: ""
            setDataByLicensePlate(licensePlate)
        }
        binding.licensePlates.addTextChangedListener(textWatcherLicensePlate)

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
            firstnameClient.editText?.inputType = InputType.TYPE_NULL
            firstnameClient.boxBackgroundColor = ContextCompat.getColor(requireContext(),R.color.light_gray)

            middlenameClient.editText?.setText(client.middleName)
            middlenameClient.editText?.inputType = InputType.TYPE_NULL
            middlenameClient.boxBackgroundColor = ContextCompat.getColor(requireContext(),R.color.light_gray)

            lastnameClient.editText?.setText(client.lastName)
            lastnameClient.editText?.inputType = InputType.TYPE_NULL
            lastnameClient.boxBackgroundColor = ContextCompat.getColor(requireContext(),R.color.light_gray)

        }
    }

    private fun setDataByLicensePlate(licensePlate : String){
        val vehicle = DatabaseHandler(requireContext()).getVehicleByLicensePlate(licensePlate,requireContext())
        with(binding){
            vinVehicle.editText?.setText(vehicle.VIN)
            vinVehicle.editText?.inputType = InputType.TYPE_NULL
            vinVehicle.boxBackgroundColor = ContextCompat.getColor(requireContext(),R.color.light_gray)

            registrationCertificate.editText?.setText(vehicle.registrationCertificate)
            registrationCertificate.editText?.inputType = InputType.TYPE_NULL
            registrationCertificate.boxBackgroundColor = ContextCompat.getColor(requireContext(),R.color.light_gray)

            engine.editText?.setText(vehicle.engine.toString())
            engine.editText?.inputType = InputType.TYPE_NULL
            engine.boxBackgroundColor = ContextCompat.getColor(requireContext(),R.color.light_gray)

            typeVehicle.editText?.setText(getString(vehicle.type.id))
            typeVehicle.boxBackgroundColor = ContextCompat.getColor(requireContext(),R.color.light_gray)
            typesVehicle.dropDownHeight = 0

            brand.editText?.setText(vehicle.brand)
            brand.editText?.inputType = InputType.TYPE_NULL
            brand.boxBackgroundColor = ContextCompat.getColor(requireContext(),R.color.light_gray)

            model.editText?.setText(vehicle.model)
            model.editText?.inputType = InputType.TYPE_NULL
            model.boxBackgroundColor = ContextCompat.getColor(requireContext(),R.color.light_gray)

            date.editText?.setText(vehicle.date)
            date.boxBackgroundColor = ContextCompat.getColor(requireContext(),R.color.light_gray)

            price.editText?.setText(vehicle.price.toString())
            price.boxBackgroundColor = ContextCompat.getColor(requireContext(),R.color.light_gray)

            setVehicleTypes()
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
                && engineStr.isNotEmpty() && brand.isNotEmpty() && model.isNotEmpty() && date.isNotEmpty()
                && price.isNotEmpty()) {
                val vehicle = databaseHandler.getVehicleByLicensePlate(licencePlate,requireContext())
                if(!vehicle.isValid){
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
                    Toast.makeText(context, "Автомобил с рег.номер $licencePlate вече има валидна застраховка.", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(context, "Something is blank.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private val textWatcherPIN = object : TextWatcher{
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int){
            with(binding) {
                firstnameClient.editText?.inputType = InputType.TYPE_CLASS_TEXT
                firstnameClient.boxBackgroundColor =
                    ContextCompat.getColor(requireContext(), R.color.white)

                middlenameClient.editText?.inputType = InputType.TYPE_CLASS_TEXT
                middlenameClient.boxBackgroundColor =
                    ContextCompat.getColor(requireContext(), R.color.white)

                lastnameClient.editText?.inputType = InputType.TYPE_CLASS_TEXT
                lastnameClient.boxBackgroundColor =
                    ContextCompat.getColor(requireContext(), R.color.white)
            }
        }

        override fun afterTextChanged(p0: Editable?) {
        }
    }

    private val textWatcherLicensePlate = object : TextWatcher{
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int){
            with(binding) {
                vinVehicle.editText?.inputType = InputType.TYPE_NULL
                vinVehicle.boxBackgroundColor = ContextCompat.getColor(requireContext(),R.color.white)

                registrationCertificate.editText?.inputType = InputType.TYPE_NULL
                registrationCertificate.boxBackgroundColor = ContextCompat.getColor(requireContext(),R.color.white)

                engine.editText?.inputType = InputType.TYPE_CLASS_NUMBER
                engine.boxBackgroundColor = ContextCompat.getColor(requireContext(),R.color.white)

                typeVehicle.editText?.inputType = InputType.TYPE_NULL
                typeVehicle.boxBackgroundColor = ContextCompat.getColor(requireContext(),R.color.white)
                typesVehicle.dropDownHeight = ViewGroup.LayoutParams.WRAP_CONTENT

                brand.editText?.inputType = InputType.TYPE_NULL
                brand.boxBackgroundColor = ContextCompat.getColor(requireContext(),R.color.white)

                model.editText?.inputType = InputType.TYPE_NULL
                model.boxBackgroundColor = ContextCompat.getColor(requireContext(),R.color.white)

                date.boxBackgroundColor = ContextCompat.getColor(requireContext(),R.color.white)

                date.boxBackgroundColor = ContextCompat.getColor(requireContext(),R.color.white)
            }
        }

        override fun afterTextChanged(p0: Editable?) {
        }
    }
}