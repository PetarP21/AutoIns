package com.example.diplomna.ui

import DatabaseHandler
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.diplomna.R
import com.example.diplomna.databinding.FragmentUpdateInsuranceBinding
import com.example.diplomna.models.Client
import com.example.diplomna.models.ValidityOptions
import com.example.diplomna.models.Vehicle
import java.util.regex.Pattern

class UpdateInsuranceFragment : Fragment() {
    private lateinit var binding: FragmentUpdateInsuranceBinding
    private val args: UpdateInsuranceFragmentArgs by navArgs() // прочети как работи
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_update_insurance,
            container,
            false
        )
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val PINs = DatabaseHandler(requireContext()).getPINs().toTypedArray()
        val PINsAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, PINs)
        binding.PINs.setOnItemClickListener { _, _, position, _ ->
            val PIN = PINsAdapter.getItem(position) ?: ""
            setDataByPIN(PIN)
        }
        binding.PINs.addTextChangedListener(textWatcherPIN)

        val databaseHandler = DatabaseHandler(requireContext())
        val vehicle = args.vehicle
        val client = databaseHandler.getClientByVehicle(vehicle)
        with(binding) {
            pinClient.editText?.setText(client.PIN)
            setDataByPIN(client.PIN)
        }
        binding.updateClientData.setOnClickListener {
            updateClient(vehicle)
            setPINs(PINsAdapter)
        }
        binding.updateLicensePlate.setOnClickListener {
            updateLicensePlate(vehicle)
        }
        setPINs(PINsAdapter)
        return binding.root
    }

    private fun updateClient(vehicle: Vehicle) {
        val pin = binding.pinClient.editText?.text.toString()
        val firstName = binding.firstnameClient.editText?.text.toString()
        val middleName = binding.middlenameClient.editText?.text.toString()
        val lastName = binding.lastnameClient.editText?.text.toString()
        val databaseHandler = DatabaseHandler(requireContext())
        if (pin.isNotEmpty() && firstName.isNotEmpty() && middleName.isNotEmpty() && lastName.isNotEmpty()) {
            var isPINValid = false
            val patternPIN = Pattern.compile("[0-9]{2}[0145][0-9][0-3][0-9]{5}")
            if (patternPIN.matcher(pin).matches()) {
                val client = databaseHandler.getClientByPIN(pin)
                if ("" == client.PIN || (pin == client.PIN && firstName == client.firstName && middleName == client.middleName && lastName == client.lastName)) {
                    isPINValid = true
                } else {
                    Toast.makeText(context, "Вече съществува клиент с ЕГН: $pin", Toast.LENGTH_LONG)
                        .show()
                }
            } else {
                Toast.makeText(context, "Невалидно ЕГН.", Toast.LENGTH_LONG).show()
            }
            if (isPINValid) {
                var isNewClient = false
                var counter = 0
                val clients = databaseHandler.getAllClients()
                for (client in clients) {
                    if (client.PIN == pin) {
                        counter++
                    }
                }
                if (clients.isEmpty() || counter == 0) {
                    isNewClient = true
                }
                if (isNewClient) {
                    databaseHandler.addClient(
                        Client(0, pin, firstName, middleName, lastName)
                    )
                }
                val statusVehicle = databaseHandler.updateVehicle(
                    Vehicle(
                        vehicle.id,
                        databaseHandler.getIdByPIN(pin),
                        vehicle.licencePlate,
                        vehicle.VIN,
                        vehicle.registrationCertificate,
                        vehicle.engine,
                        vehicle.vehicleTypeId,
                        vehicle.brand,
                        vehicle.model,
                        vehicle.date,
                        vehicle.price,
                        vehicle.validityId
                    )
                )
                if (statusVehicle > -1) {
                    Toast.makeText(context, "Успешно променен клиент в застраховка.", Toast.LENGTH_LONG)
                        .show()
                    with(binding) {
                        pinClient.editText?.text?.clear()
                        firstnameClient.editText?.text?.clear()
                        middlenameClient.editText?.text?.clear()
                        lastnameClient.editText?.text?.clear()
                        licensePlate.editText?.text?.clear()
                    }
                }
            } else {
                Toast.makeText(context, "Не всички полета са попълнени.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun updateLicensePlate(vehicle: Vehicle) {
        val licencePlate = binding.licensePlate.editText?.text.toString()
        val databaseHandler = DatabaseHandler(requireContext())
        if (licencePlate.isNotEmpty()) {
            var isLicensePlateValid = false
            val patternLicensePlate =
                Pattern.compile("[ABEKMHOPCTYX]{1,2}[0-9]{4}[ABEKMHOPCTYX]{2}")
            if (patternLicensePlate.matcher(licencePlate).matches()) {
                val currentVehicle = databaseHandler.getVehicleByLicensePlate(licencePlate)
                val validity = databaseHandler.getValidityByVehicle(currentVehicle)
                if (validity.validity.toString() != getString(ValidityOptions.YES.id)) {
                    isLicensePlateValid = true
                } else {
                    Toast.makeText(
                        context,
                        "Автомобил с рег.номер $licencePlate вече има валидна застраховка.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } else {
                Toast.makeText(context, "Невалиден регистрационен номер.", Toast.LENGTH_LONG).show()
            }
            if (isLicensePlateValid) {
                val statusVehicle = databaseHandler.updateVehicle(
                    Vehicle(
                        vehicle.id,
                        vehicle.clientId,
                        licencePlate,
                        vehicle.VIN,
                        vehicle.registrationCertificate,
                        vehicle.engine,
                        vehicle.vehicleTypeId,
                        vehicle.brand,
                        vehicle.model,
                        vehicle.date,
                        vehicle.price,
                        vehicle.validityId
                    )
                )
                if (statusVehicle > -1) {
                    Toast.makeText(
                        context,
                        "Успешно променена рег. номер на застраховка.",
                        Toast.LENGTH_LONG
                    ).show()
                    with(binding) {
                        licensePlate.editText?.text?.clear()
                    }
                }
            }
        } else {
            Toast.makeText(context, "Не всички полета са попълнени.", Toast.LENGTH_LONG).show()
        }
    }

    private fun setPINs(adapter: ArrayAdapter<String>) {
        binding.PINs.setAdapter(adapter)
    }

    private fun setDataByPIN(PIN: String) {
        val client = DatabaseHandler(requireContext()).getClientByPIN(PIN)
        with(binding) {
            firstnameClient.editText?.setText(client.firstName)
            firstnameClient.editText?.inputType = InputType.TYPE_NULL
            firstnameClient.boxBackgroundColor =
                ContextCompat.getColor(requireContext(), R.color.light_gray)

            middlenameClient.editText?.setText(client.middleName)
            middlenameClient.editText?.inputType = InputType.TYPE_NULL
            middlenameClient.boxBackgroundColor =
                ContextCompat.getColor(requireContext(), R.color.light_gray)

            lastnameClient.editText?.setText(client.lastName)
            lastnameClient.editText?.inputType = InputType.TYPE_NULL
            lastnameClient.boxBackgroundColor =
                ContextCompat.getColor(requireContext(), R.color.light_gray)

        }
    }

    private val textWatcherPIN = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            with(binding) {
                if (firstnameClient.editText?.inputType == InputType.TYPE_NULL) {
                    firstnameClient.editText?.text?.clear()
                }
                firstnameClient.editText?.inputType = InputType.TYPE_CLASS_TEXT
                firstnameClient.boxBackgroundColor =
                    ContextCompat.getColor(requireContext(), R.color.white)
                if (middlenameClient.editText?.inputType == InputType.TYPE_NULL) {
                    middlenameClient.editText?.text?.clear()
                }
                middlenameClient.editText?.inputType = InputType.TYPE_CLASS_TEXT
                middlenameClient.boxBackgroundColor =
                    ContextCompat.getColor(requireContext(), R.color.white)

                if (lastnameClient.editText?.inputType == InputType.TYPE_NULL) {
                    lastnameClient.editText?.text?.clear()
                }
                lastnameClient.editText?.inputType = InputType.TYPE_CLASS_TEXT
                lastnameClient.boxBackgroundColor =
                    ContextCompat.getColor(requireContext(), R.color.white)
            }
        }

        override fun afterTextChanged(p0: Editable?) {
        }
    }

}