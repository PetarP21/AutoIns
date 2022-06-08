package com.example.diplomna.util

import DatabaseHandler
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.diplomna.MainActivity
import com.example.diplomna.R
import com.example.diplomna.models.Vehicle
import com.example.diplomna.ui.ShowInsurancesFragment

class VehicleAdapter(val context: Context, val items: ArrayList<Vehicle>) : RecyclerView.Adapter<VehicleAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val licensePlate: TextView
        val clientName: TextView
        val VIN: TextView
        val registrationCertificate: TextView
        val engine: TextView
        val type: TextView
        val brand: TextView
        val model: TextView
        val date: TextView
        val price: TextView
        val isValid: TextView
        val deleteButton: ImageView
        val editButton: ImageView

        init {
            licensePlate = view.findViewById(R.id.license_plate_textView)
            clientName = view.findViewById(R.id.client_name_textView)
            VIN = view.findViewById(R.id.vin_textView)
            registrationCertificate = view.findViewById(R.id.reg_certificate_textView)
            engine = view.findViewById(R.id.engine_textView)
            type = view.findViewById(R.id.type_textView)
            brand = view.findViewById(R.id.brand_textView)
            model = view.findViewById(R.id.model_textView)
            date = view.findViewById(R.id.date_textView)
            price = view.findViewById(R.id.price_textView)
            isValid = view.findViewById(R.id.isValid_textView)
            deleteButton = view.findViewById(R.id.delete_ins)
            editButton = view.findViewById(R.id.edit_ins)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_card,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val db = DatabaseHandler(context)
        val client = db.getClientById(item.clientId)

        holder.licensePlate.text = item.licencePlate
        holder.clientName.text = "Клиент: ${client.firstName} ${client.lastName}"
        holder.VIN.text = "Идентификационен номер: "+item.VIN
        holder.registrationCertificate.text = "№ на рег. сертификат: "+item.registrationCertificate
        holder.engine.text = "Обем на двигателя: "+item.engine.toString()
        holder.type.text = "Вид: "+context.getString(item.type.id)
        holder.brand.text = "Марка: "+item.brand
        holder.model.text = "Модел: "+item.model
        holder.date.text = "Дата на сключване: "+item.date
        holder.price.text = "Застрахователна премия: "+item.price.toString()
        if(item.isValid) {
            holder.isValid.text = "Валидност: Валидна"
        } else {
            holder.isValid.text = "Валидност: Невалидна"
        }

        holder.deleteButton.setOnClickListener {
            if (context is MainActivity) {
                val navFragment = context.supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.childFragmentManager?.fragments?.first()
                if(navFragment is ShowInsurancesFragment){
                    navFragment.deleteInsuranceRecordAlertDialog(item)
                }
            }
        }

        holder.editButton.setOnClickListener {
            if (context is MainActivity) {
                val navFragment = context.supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.childFragmentManager?.fragments?.first()
                if(navFragment is ShowInsurancesFragment){
                    navFragment.deleteInsuranceRecordAlertDialog(item)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}