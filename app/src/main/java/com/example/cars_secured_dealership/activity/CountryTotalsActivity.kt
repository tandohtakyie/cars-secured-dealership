package com.example.cars_secured_dealership.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cars_secured_dealership.R
import com.example.cars_secured_dealership.adapter.SharedCarDealAdapter
import com.example.cars_secured_dealership.model.CarDeal
import com.example.cars_secured_dealership.model.Quote
import com.example.cars_secured_dealership.viewmodel.SharedViewModel
import com.example.cars_secured_dealership.viewmodel.TriviaViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_all_totals.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt
import kotlin.random.Random


const val EXTRA_COUNTRY = "EXTRA_COUNTRY"

class CountryTotalsActivity : AppCompatActivity() {

    private var carDeals = arrayListOf<CarDeal>()
    private lateinit var viewModel: SharedViewModel
    private lateinit var triviaViewModel: TriviaViewModel
    private val mainScope = CoroutineScope(Dispatchers.Main)

    private val carDealAdapter = SharedCarDealAdapter(carDeals){
        carDeal -> onCarDealClicked(carDeal)
    }

    private var mTotal: Double = 0.0
    private var mChosenCountry: String = ""
    private var mQuote: String = ""

    private fun onCarDealClicked(carDeal : CarDeal){
        val intent = Intent(this, EditCarDealActivity::class.java)
        val bundle = Bundle()
        bundle.putParcelable(EXTRA_CARDEAL, carDeal)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState : Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_country_totals)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.menu_totals_hint)

        // preparing the quotes
        val endIndex = Quote.QUOTE_NAMES.size

        val randomInt : Int = Random.nextInt(0, endIndex)
        mQuote = Quote.QUOTE_NAMES[randomInt]

        initViewModel()
        initViews()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_top, menu)
        return true
    }

    override fun onResume() {
        super.onResume()
        initViews()
    }

    private fun initViews(){
        text_trivia.text = getString(R.string.trivia_empty_hint)

        val bundle = intent.extras
        if (bundle != null){
            val country : String = bundle.get(EXTRA_COUNTRY) as String
            mChosenCountry = country
        }

        text_title_all.text = mChosenCountry

        viewModel.getCarDealsPerCountry(mChosenCountry)

        createItemTouchHelper().attachToRecyclerView(rv_carDealsPerCountry)

        rv_carDealsPerCountry.apply {
            layoutManager = LinearLayoutManager(context)
            rv_carDealsPerCountry.adapter = carDealAdapter
            rv_carDealsPerCountry.addItemDecoration(DividerItemDecoration(
                this.context, DividerItemDecoration.VERTICAL
            ))
        }

        mTotal = viewModel.getSumPerCountry(mChosenCountry)

        var number = (mTotal).roundToInt()

        while (number > 100){
            number = (number / 10.0).roundToInt()
        }

        triviaViewModel.getRandomTrivia(number)

        var fTotal = "%.2f".format(0-mTotal)
        if (mChosenCountry.equals("Own Place")){
            fTotal = "+"+"%.2f".format(mTotal)
        }

        text_total_totals.text = getString(R.string.totals_total_hint, fTotal)

    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(SharedViewModel::class.java)

        viewModel.carDealsPerCountry.observe(this, Observer {
            carDealPerCountry ->
            this.carDeals.clear()
            this.carDeals.addAll(carDealPerCountry)

            carDealAdapter.notifyDataSetChanged()
        })

        triviaViewModel = ViewModelProviders.of(this).get(TriviaViewModel::class.java)

        triviaViewModel.trivia.observe(this, Observer {
            if (it?.number.equals("0")){
                text_trivia.text = getString(R.string.trivia_empty_hint)
            } else {
                text_trivia.text = getString(R.string.trivia_text_hint, it?.number, mQuote, it?.text)
            }
        })

        triviaViewModel.error.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        })
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

                val carDealToDelete = carDeals[position]

                viewModel.deleteCarDeal(carDealToDelete)

                Snackbar.make(rv_carDealsPerCountry, "Car deal deleted", Snackbar.LENGTH_LONG)
                    .setAction("Undo"){
                        viewModel.insertCarDeal(carDealToDelete)
                        initViews()
                    }.show()
            }

        }
        return ItemTouchHelper(callback)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.action_delete_carDeals -> {
                deleteButtonPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun deleteButtonPressed() {

        val builder = AlertDialog.Builder(this)

        builder.setTitle(R.string.delete_all_hint)
        builder.setMessage(R.string.delete_first_confirmation_hint)

        // Set a positive button and its click listener on alert dialog
        builder.setPositiveButton(R.string.delete_yes_hint) { _, _ ->
            secondConfirm()
        }
        // Display a negative button on alert dialog
        builder.setNegativeButton(R.string.delete_cancel_hint) { _, _ ->
            Toast.makeText(applicationContext, R.string.delete_canceled_hint, Toast.LENGTH_SHORT).show()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun secondConfirm(){
        // If the user clicked yes on the first dialog, show another dialog to truly confirm
        val builder = AlertDialog.Builder(this)

        builder.setTitle(R.string.delete_all_hint)
        builder.setMessage(R.string.delete_second_confirmation_hint)

        builder.setPositiveButton(R.string.delete_yes_hint) { _, _ ->
            deleteCarDeals()
            Toast.makeText(applicationContext, R.string.delete_deleted_hint, Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton(R.string.delete_cancel_hint) { _, _ ->
            Toast.makeText(applicationContext, R.string.delete_canceled_hint, Toast.LENGTH_SHORT)
                .show()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()

    }

    private fun deleteCarDeals() {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                viewModel.deleteCarDealsOfCountry(mChosenCountry)
            }
        }
        finish()
        startActivity(intent)
    }

}
