package com.example.cars_secured_dealership.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cars_secured_dealership.R
import com.example.cars_secured_dealership.activity.EXTRA_CARDEAL
import com.example.cars_secured_dealership.activity.EditCarDealActivity
import com.example.cars_secured_dealership.adapter.SharedCarDealAdapter
import com.example.cars_secured_dealership.model.CarDeal
import com.example.cars_secured_dealership.viewmodel.SharedViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    private val mainCarDeals = arrayListOf<CarDeal>()
    private val carDealAdapter = SharedCarDealAdapter(mainCarDeals){ carDeal ->
        onCarDealClicked(carDeal)
    }
    private lateinit var viewModel: SharedViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        initViewModel()
    }

    private fun initViewModel(){
        viewModel = ViewModelProviders.of(this).get(SharedViewModel::class.java)

        viewModel.mainCarDeals.observe(this, Observer {
            mainCarDeals ->
            this.mainCarDeals.clear()
            this.mainCarDeals.addAll(mainCarDeals)

            carDealAdapter.notifyDataSetChanged()
        })
    }

    private fun initViews(){
        createItemTouchHelper().attachToRecyclerView(rv_carDeals_home)
        rv_carDeals_home.apply {
            layoutManager = LinearLayoutManager(activity)
            rv_carDeals_home.adapter = carDealAdapter
            rv_carDeals_home.addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        }
    }

    private fun createItemTouchHelper() : ItemTouchHelper {
        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val carDealToDelete = mainCarDeals[position]

                viewModel.deleteCarDeal(carDealToDelete)

                Snackbar.make(rv_carDeals_home, R.string.delete_carDeal_deleted_hint, Snackbar.LENGTH_LONG).setAction(R.string.delete_undo_hint){
                    viewModel.insertCarDeal(carDealToDelete)
                }.show()
            }
        }
        return ItemTouchHelper(callback)
    }

    private fun onCarDealClicked(carDeal: CarDeal){
        val intent = Intent(activity, EditCarDealActivity::class.java)
        val bundle = Bundle()
        bundle.putParcelable(EXTRA_CARDEAL, carDeal)
        intent.putExtras(bundle)
        startActivity(intent)
    }

}
