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

class AllTotalsActivity : AppCompatActivity() {
    private var allCarDeals = arrayListOf<CarDeal>()
    private lateinit var viewModel : SharedViewModel
    private lateinit var triviaViewModel: TriviaViewModel
    private val mainScope = CoroutineScope(Dispatchers.Main)

    val carDealAdapter = SharedCarDealAdapter(allCarDeals, onClick = {onCarDealClicked(it)})

    private var mCarDeals : Double = 0.0
    private var mOwnPlace : Double = 0.0
    private var mTotal : Double = 0.0
    private var mQuote : String = ""

    // To edit a car deal
    private fun onCarDealClicked(carDeal : CarDeal){
        val intent = Intent(this, EditCarDealActivity::class.java)
        val bundle = Bundle()
        bundle.putParcelable(EXTRA_CARDEAL, carDeal)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    override fun onCreate(savedInstanceState : Bundle?){
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_all_totals)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.menu_totals_hint)

        // preparing the quotes
        val endIndex = Quote.QUOTE_NAMES.size

        // getting random number for getting a random quote from the product list
        val randomInt: Int = Random.nextInt(0, endIndex)
        mQuote = Quote.QUOTE_NAMES[randomInt]

        initViewModels()
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

        createItemTouchHelper().attachToRecyclerView(rv_carDealsPerCountry)

        rv_carDealsPerCountry.apply {
            layoutManager = LinearLayoutManager(context)
            rv_carDealsPerCountry.adapter = carDealAdapter
            rv_carDealsPerCountry.addItemDecoration(DividerItemDecoration(
                this.context, DividerItemDecoration.VERTICAL
            ))
        }

        mCarDeals = viewModel.getSumAll()
        mOwnPlace = viewModel.getSumOwnPlace()
        mTotal = mOwnPlace - mCarDeals


        // rounding the total to int to get a quote
        var number = mTotal.roundToInt()
        if (number < 0) number *= -1

        // if the total is let's say greater than 100, it keeps dividing it
        // high numbers are less likely to have any quote
        while (number > 100){
            number = (number / 10.0).roundToInt()
        }

        triviaViewModel.getRandomTrivia(number)

        val fCarDeals = "%.2f".format(mCarDeals)
        val fOwnPlace = "%.2f".format(mOwnPlace)
        var fTotal = "%.2f".format(mTotal)

        if (mTotal > 0.0){
            fTotal = "+$fTotal"
        }

        text_carDeals_totals.text = getString(R.string.totals_carDeals_hint, fCarDeals)
        text_ownPlace_totals.text = getString(R.string.totals_own_place_hint, fOwnPlace)
        text_total_totals.text = getString(R.string.totals_total_hint, fTotal)
    }

    private fun initViewModels(){
        // ViewModel for the car deals list
        viewModel = ViewModelProviders.of(this).get(SharedViewModel::class.java)

        viewModel.allCarDeals.observe(this, Observer {
            allCarDeals ->
            this.allCarDeals.clear()
            this.allCarDeals.addAll(allCarDeals)
            carDealAdapter.notifyDataSetChanged()
        })

        triviaViewModel = ViewModelProviders.of(this).get(TriviaViewModel::class.java)

        triviaViewModel.trivia.observe(this, Observer {
            text_trivia.text = getString(R.string.trivia_text_hint, it?.number, mQuote, it?.text)
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

            // when an item swiped is triggered
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

                // take into account of the deleted car deal
                val carDealToDelete = allCarDeals[position]

                // delete car deal
                viewModel.deleteCarDeal(carDealToDelete)

                // giving the user the chance to restore the deleted car deal
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

    // when delete icon is clicked, start car deal deletion process
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, as long as
        // a parent activity is specified in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_delete_carDeals -> {
                deleteButtonPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun deleteButtonPressed(){
        // a dialog message to user if they really
        // want to delete all car deals
        // from that country
        val builder = AlertDialog.Builder(this)

        builder.setTitle(R.string.delete_all_hint)
        builder.setMessage(R.string.delete_first_confirmation_hint)

        // giving them the option to choose
        builder.setPositiveButton(R.string.delete_yes_hint){ _, _ ->
            secondConfirm()
        }

        builder.setNegativeButton(R.string.delete_cancel_hint){ _, _ ->
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

    private fun deleteCarDeals(){
        mainScope.launch {
            withContext(Dispatchers.IO){
                viewModel.deleteAllCarDeals()
            }
        }
        finish()
        startActivity(intent)
    }
}
