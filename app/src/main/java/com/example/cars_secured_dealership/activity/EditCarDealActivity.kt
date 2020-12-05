package com.example.cars_secured_dealership.activity

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.cars_secured_dealership.R
import com.example.cars_secured_dealership.model.CarDeal
import com.example.cars_secured_dealership.model.Country
import com.example.cars_secured_dealership.viewmodel.SharedViewModel
import kotlinx.android.synthetic.main.activity_edit_cardeal.*

const val EXTRA_CARDEAL = "EXTRA_CARDEAL"

class EditCarDealActivity() : AppCompatActivity() {
    
    private lateinit var viewModel: SharedViewModel
    
    private var mCountryPos : Int = 0
    
    private var mId: Long = 0
    private var mCarDeal : String = ""
    private var mChosenCountry : String = ""
    private var mTotal : Double = 0.0
    private var mChosenDate : String = ""

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_cardeal)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Totals"
        initViews()
    }
    
    
    private fun initViews(){
        viewModel = ViewModelProviders.of(this).get(SharedViewModel::class.java)
        
        val bundle = intent.extras
        if (bundle != null){
            val carDealItem : CarDeal = bundle.get(EXTRA_CARDEAL) as CarDeal
            
            mId = carDealItem.id!!
            mCarDeal = carDealItem.carDeal
            mChosenCountry = carDealItem.country
            mChosenDate = carDealItem.date
            mTotal = carDealItem.total
            
            input_edit_amount.setText(carDealItem.total.toString())
            input_edit_carDeal.setText(carDealItem.carDeal)
            
            
            when(mChosenCountry){
                "China"  -> mCountryPos = 0
                "United States"  -> mCountryPos = 1
                "Japan"  -> mCountryPos = 2
                "South Korea"  -> mCountryPos = 3
                "Spain"  -> mCountryPos = 4
                "France"  -> mCountryPos = 5
                "Canada"  -> mCountryPos = 6
                "Italy"  -> mCountryPos = 7
                "Germany"  -> mCountryPos = 8
                "United Kingdom"  -> mCountryPos = 9
                "Netherlands"  -> mCountryPos = 10
                "Australia"  -> mCountryPos = 11
                "Belgium"  -> mCountryPos = 12
                "South Africa"  -> mCountryPos = 13
                "Own Place" -> mCountryPos = 14
                else -> {
                    mCountryPos = 15
                }
            }
        }


        initCountrySpinner()
        initDatePicker()

        fab_save.setOnClickListener{
            editCarDeal(mId)
        }

    }

    private fun editCarDeal(id: Long){
        if (validateFields()){
            mCarDeal = input_edit_carDeal.text.toString()
            mTotal = input_edit_amount.text.toString().toDouble()

            val newCarDeal = CarDeal(mCarDeal, mChosenCountry, mChosenDate, mTotal, id)

            viewModel.updateCarDeal(newCarDeal)

            Toast.makeText(this, R.string.carDeal_updated, Toast.LENGTH_SHORT).show()

            finish()
        }
    }

    private fun validateFields() : Boolean {
        val givenAmount = input_edit_amount.text.toString().toDoubleOrNull()
        return if(givenAmount != null && input_edit_amount.text.toString().isNotBlank() && input_edit_carDeal.text.toString().isNotBlank()){
            true
        }else{
            if (input_edit_amount.text.toString().isNotBlank()){
                Toast.makeText(this, R.string.validation_amount_hint, Toast.LENGTH_SHORT).show()
                false
            }else {
                Toast.makeText(this, R.string.validation_fields_hint, Toast.LENGTH_SHORT).show()
                false
            }
        }
    }

    private fun initCountrySpinner(){
        val spinner = spinner_Country_edit

        spinner.adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, Country.COUNTRY_NAMES)

        spinner.setSelection(mCountryPos)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mChosenCountry = spinner.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
    }

    private fun initDatePicker(){
        val datePicker = this.findViewById<DatePicker>(R.id.picker_date_edit)

        val origYear = mChosenDate.substring(startIndex = 0, endIndex = 4)
        val origMonth = mChosenDate.substring(startIndex = 5, endIndex = 7)
        val origDay = mChosenDate.substring(startIndex = 8, endIndex = 10)

        datePicker.init(origYear.toInt(), origMonth.toInt() - 1, origDay.toInt()){
            _, year, month, day ->

            var monthString = java.lang.String.valueOf(month+1)
            if (monthString.length == 1){
                monthString = "0$monthString"
            }

            var dayString = java.lang.String.valueOf(day)
            if (dayString.length == 1) {
                dayString = "0$dayString"
            }

            mChosenDate = "${year}-${monthString}-${dayString}"
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    
}
