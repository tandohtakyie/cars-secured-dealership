package com.example.cars_secured_dealership.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cars_secured_dealership.R
import com.example.cars_secured_dealership.activity.AllTotalsActivity
import com.example.cars_secured_dealership.activity.CountryTotalsActivity
import com.example.cars_secured_dealership.activity.EXTRA_COUNTRY
import kotlinx.android.synthetic.main.fragment_totals.*

class TotalsFragment : Fragment() {

    private fun startIntent(country: String){
        val intent = Intent(activity, CountryTotalsActivity::class.java)
        val bundle = Bundle()
        bundle.putString(EXTRA_COUNTRY, country)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_totals, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initButtons()
    }

    private fun initButtons(){
        button_all.setOnClickListener{
            val intent = Intent(activity, AllTotalsActivity::class.java)
            startActivity(intent)
        }

        button_china.setOnClickListener{
            startIntent("China")
        }
        button_united_states.setOnClickListener{
            startIntent("United States")
        }
        button_japan.setOnClickListener{
            startIntent("Japan")
        }
        button_south_korea.setOnClickListener{
            startIntent("South Korea")
        }
        button_spain.setOnClickListener{
            startIntent("Spain")
        }
        button_france.setOnClickListener{
            startIntent("France")
        }
        button_canada.setOnClickListener{
            startIntent("Canada")
        }
        button_italy.setOnClickListener{
            startIntent("Italy")
        }
        button_germany.setOnClickListener{
            startIntent("Germany")
        }
        button_united_kingdom.setOnClickListener{
            startIntent("United Kingdom")
        }
        button_netherlands.setOnClickListener{
            startIntent("Netherlands")
        }
        button_australia.setOnClickListener{
            startIntent("Australia")
        }
        button_belgium.setOnClickListener{
            startIntent("Belgium")
        }
        button_south_africa.setOnClickListener{
            startIntent("South Africa")
        }
    }
}
