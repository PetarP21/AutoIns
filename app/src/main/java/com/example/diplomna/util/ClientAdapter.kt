package com.example.diplomna.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.diplomna.MainActivity
import com.example.diplomna.R
import com.example.diplomna.models.Client
import com.example.diplomna.ui.ShowClientsFragment

class ClientAdapter(val context: Context, val items: ArrayList<Client>) : RecyclerView.Adapter<ClientAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val pin: TextView
        val firstName: TextView
        val middleName: TextView
        val lastName: TextView
        val updateButton: ImageView
        val deleteButton: ImageView

        init {
            pin = view.findViewById(R.id.pin_textView)
            firstName = view.findViewById(R.id.firstName_textView)
            middleName = view.findViewById(R.id.middleName_textView)
            lastName = view.findViewById(R.id.lastName_textView)
            updateButton = view.findViewById(R.id.edit_client)
            deleteButton = view.findViewById(R.id.delete_client)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.client_card,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.pin.text = item.PIN
        holder.firstName.text = "Име: ${item.firstName}"
        holder.middleName.text = "Презиме: "+item.middleName
        holder.lastName.text = "Фамилия: "+item.lastName


        holder.deleteButton.setOnClickListener {
            if (context is MainActivity) {
                val navFragment = context.supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.childFragmentManager?.fragments?.first()
                if(navFragment is ShowClientsFragment){
                    navFragment.deleteClientRecordAlertDialog(item)
                }
            }
        }

        holder.updateButton.setOnClickListener {
            if (context is MainActivity) {
                val navFragment = context.supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.childFragmentManager?.fragments?.first()
                if(navFragment is ShowClientsFragment){
                    navFragment.updateClientRecordDialog(item)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}