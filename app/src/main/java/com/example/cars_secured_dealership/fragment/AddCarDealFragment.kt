package com.example.cars_secured_dealership.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.cars_secured_dealership.R
import com.example.cars_secured_dealership.model.CarDeal
import com.example.cars_secured_dealership.model.Country
import com.example.cars_secured_dealership.viewmodel.SharedViewModel
import kotlinx.android.synthetic.main.fragment_add_cardeal.*
import java.util.*

class AddCarDealFragment : Fragment() {

    private lateinit var viewModel : SharedViewModel

    private val sCurDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    private val sCurMonth = Calendar.getInstance().get(Calendar.MONTH)
    private val sCurYear = Calendar.getInstance().get(Calendar.YEAR)

    private var mChosenDay = sCurDay
    private var mChosenMonth = sCurMonth+1
    private var mChosenYear = sCurYear
    private var mChosenDate: String = ""

    private var mChosenCountry: String = ""

    override fun onCreateView(inflater: LayoutInflater, container : ViewGroup?, savedInstanceState : Bundle?) : View?{
        viewModel = ViewModelProviders.of(activity as AppCompatActivity).get(SharedViewModel::class.java)

        return inflater.inflate(R.layout.fragment_add_cardeal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        initCountrySpinner()
        initDatePicker()

        fab_add.setOnClickListener{
            addCarDeal()
        }
    }


    private fun validateFields() : Boolean{
        val givenAmount = input_add_amount.text.toString().toDoubleOrNull()

        return if (givenAmount != null &&
                    input_add_amount.text.toString().isNotBlank() &&
                    input_add_carDeal.text.toString().isNotBlank()){
            true
        }else{
            if (input_add_amount.text.toString().isNotBlank()){
                Toast.makeText(context, R.string.validation_amount_hint, Toast.LENGTH_SHORT).show()
                false
            } else{
                Toast.makeText(context, R.string.validation_fields_hint, Toast.LENGTH_SHORT).show()
                false
            }
        }
    }

    private fun addCarDeal(){
        if (validateFields()){

            // making sure that both the date and the month are of 2 digits long
            validateDate()

            val carDeal = CarDeal(
                input_add_carDeal.text.toString(),
                mChosenCountry,
                mChosenDate,
                input_add_amount.text.toString().toDouble()
            )

            viewModel.insertCarDeal(carDeal)
            findNavController().navigateUp()

        }
    }

    private fun validateDate(){
        var monthString = mChosenMonth.toString()
        var dayString = mChosenDay.toString()

        // Concatenate zero to single digits of both the month and the day
        if (mChosenMonth.toString().length == 1){
            monthString = "0$mChosenMonth"
        }

        if (mChosenDay.toString().length == 1){
            dayString = "0$mChosenDay"
        }

        mChosenDate = "${mChosenYear}-${monthString}-${dayString}"
    }

    private fun initCountrySpinner(){
        val spinner = spinner_country

        spinner.adapter = ArrayAdapter(
            context!!,
            R.layout.support_simple_spinner_dropdown_item,
            Country.COUNTRY_NAMES
        )

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mChosenCountry = spinner.selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                /* nothing will be done if nothing is selected. */
            }

        }
    }

    private fun initDatePicker(){
        val datePicker = view!!.findViewById<DatePicker>(R.id.picker_date)

        datePicker.init(sCurYear, sCurMonth, sCurDay){
            _,year, month, day ->
            mChosenYear = year
            mChosenMonth = month + 1
            mChosenDay = day

        }
    }

}
