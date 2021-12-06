package com.example.currencyconverter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.currencyconverter.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import kotlin.reflect.full.memberProperties

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var currencyArr: ArrayList<String>
    private lateinit var allCurrency: Cur

    var currencySelect = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        fetchAPI(result = {
            val currency = it

            binding.tvDate.text = "Date: ${currency?.date}"
            allCurrency = currency?.eur!!

            currencyArr = arrayListOf()
            Cur::class.memberProperties.forEach { member ->
                currencyArr.add(member.name)
            }
            setupSpinner()

        })



        binding.btnConvert.setOnClickListener{ currencyConvert("from EURO") }
        binding.btnConvertToEuro.setOnClickListener{ currencyConvert("to EURO") }


        }

    private fun fetchAPI(result: (Currency?) -> Unit) {
        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)
        apiInterface?.getCurrency()?.enqueue(object : Callback<Currency>{
            override fun onResponse(call: Call<Currency>, response: Response<Currency>) {
                try {
                    result(response.body()!!)

                } catch (e: Exception) {
                    Log.d("Main", "Error: $e")
                }
            }

            override fun onFailure(call: Call<Currency>, t: Throwable) {
                result(null)
            }

        })
    }

    private fun setupSpinner() {
        val spinner = binding.spinner

        if (spinner != null) {
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, currencyArr)
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                currencySelect = currencyArr[p2]
                binding.btnConvert.text = "Convert from EURO to $currencySelect".capitalize()
                binding.btnConvertToEuro.text = "Convert from $currencySelect to EURO".capitalize()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }

    private fun currencyConvert(convertType: String) {
        var num = 0.0
        Cur::class.memberProperties.forEach { member ->
            if (member.name == currencySelect) {
                num = member.get(allCurrency).toString().toDouble()
            }
        }
        val userInput = binding.etValue.text.toString().toDouble()

        if (convertType == "from EURO") {
            val result = userInput * num
            binding.tvResult.text = "Result: $result $currencySelect"
        } else {
            val result = userInput / num
            binding.tvResult.text = "Result: $result EURO"
        }
    }


    }