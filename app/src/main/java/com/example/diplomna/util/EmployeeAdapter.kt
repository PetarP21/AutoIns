package com.example.diplomna.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.example.diplomna.MainActivity
import com.example.diplomna.R
import com.example.diplomna.models.Employee
import com.example.diplomna.ui.ShowEmployersFragment

class EmployeeAdapter(val context: Context, val items: ArrayList<Employee>) : RecyclerView.Adapter<EmployeeAdapter.ViewHolder>(){

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nickname: TextView
        val firstName: TextView
        val lastName: TextView
        val deleteButton: ImageView
        val llMain : LinearLayout

        init {
            // Define click listener for the ViewHolder's View.
            nickname = view.findViewById(R.id.nickname)
            firstName = view.findViewById(R.id.firstname)
            lastName = view.findViewById(R.id.lastname)
            deleteButton = view.findViewById(R.id.delete_ins)
            llMain = view.findViewById(R.id.llMain)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       return ViewHolder(
           LayoutInflater.from(context).inflate(
               R.layout.items_row,
               parent,
               false
           )
       )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.nickname.text = item.nickname
        holder.firstName.text = item.firstName
        holder.lastName.text = item.lastName

        if(item.nickname=="admin") {
            holder.deleteButton.isGone = true
        }

        holder.deleteButton.setOnClickListener {
            if (context is MainActivity) {
                val navFragment = context.supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.childFragmentManager?.fragments?.first()
                if(navFragment is ShowEmployersFragment){
                    navFragment.deleteRecordAlertDialog(item)
                }
            }
        }

        // Updating the background color according to the odd/even positions in list.
        if (position % 2 == 0) {
            holder.llMain.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.orange
                )
            )
        } else {
            holder.llMain.setBackgroundColor(ContextCompat.getColor(context, R.color.background_splash))
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }
}