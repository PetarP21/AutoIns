package com.example.diplomna.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.example.diplomna.MainActivity
import com.example.diplomna.R
import com.example.diplomna.models.Employee
import com.example.diplomna.ui.ShowEmployersFragment

class EmployeeAdapter(val context: Context, val items: ArrayList<Employee>) : RecyclerView.Adapter<EmployeeAdapter.ViewHolder>(){

   inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nickname: TextView
        val firstName: TextView
        val middleName: TextView
        val lastName: TextView
        val email: TextView
        val position: TextView
        val deleteButton: ImageView
        val updateButton: ImageView

        init {
            nickname = view.findViewById(R.id.nickname_textView_employee)
            firstName = view.findViewById(R.id.firstname_textView_employee)
            middleName = view.findViewById(R.id.middleName_textView_employee)
            lastName = view.findViewById(R.id.lastName_textView_employee)
            email = view.findViewById(R.id.email_textView_employee)
            position = view.findViewById(R.id.position_textView_employee)
            deleteButton = view.findViewById(R.id.delete_employee)
            updateButton = view.findViewById(R.id.update_employee)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       return ViewHolder(
           LayoutInflater.from(context).inflate(
               R.layout.employee_card,
               parent,
               false
           )
       )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.nickname.text = item.nickname
        holder.firstName.text = "Име: ${item.firstName}"
        holder.middleName.text = "Презиме:"+item.middleName
        holder.lastName.text = "Фамилия: "+item.lastName
        holder.email.text = "Имейл: "+item.email
        holder.position.text = "Позиция: "+item.position

        if(item.nickname=="admin") {
            holder.deleteButton.isGone = true
            holder.updateButton.isGone = true
        }

        holder.deleteButton.setOnClickListener {
            if (context is MainActivity) {
                val navFragment = context.supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.childFragmentManager?.fragments?.first()
                if(navFragment is ShowEmployersFragment){
                    navFragment.deleteEmployeeRecordAlertDialog(item)
                }
            }
        }

        holder.updateButton.setOnClickListener {
            if (context is MainActivity) {
                val navFragment = context.supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.childFragmentManager?.fragments?.first()
                if(navFragment is ShowEmployersFragment){
                    navFragment.updateEmployeeRecordDialog(item)
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }
}