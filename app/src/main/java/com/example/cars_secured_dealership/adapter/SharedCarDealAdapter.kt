package com.example.cars_secured_dealership.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cars_secured_dealership.R
import com.example.cars_secured_dealership.model.CarDeal
import kotlinx.android.synthetic.main.item_cardeal.view.*

class SharedCarDealAdapter(private val carDeals : List<CarDeal>, private val onClick : (CarDeal) -> Unit) : RecyclerView.Adapter<SharedCarDealAdapter.ViewHolder>() {

    lateinit var context: Context

    override fun getItemCount(): Int {
        return carDeals.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SharedCarDealAdapter.ViewHolder {
        context = parent.context
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_cardeal, parent, false))
    }

    override fun onBindViewHolder(holder: SharedCarDealAdapter.ViewHolder, position: Int) {
        holder.bind(carDeals[position])
    }



    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        init {
            itemView.setOnClickListener{
                onClick(carDeals[adapterPosition])
            }
        }
        fun bind(carDeals: CarDeal){
            val amountToShow = "%.2f".format(carDeals.total)
            itemView.tv_carDeal.text = carDeals.carDeal
            itemView.tv_country.text = carDeals.country
            itemView.tv_date.text = carDeals.date
            itemView.tv_amount.text = amountToShow
        }
    }


}
