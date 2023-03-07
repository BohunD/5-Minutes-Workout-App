package com.example.a7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.a7minutesworkout.databinding.ActivityBmiBinding
import kotlin.math.pow

class BMIActivity : AppCompatActivity() {

    private var binding: ActivityBmiBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiBinding.inflate(layoutInflater)
        setSupportActionBar(binding?.toolbarBmi)
        setContentView(binding?.root)
        setSupportActionBar(binding?.toolbarBmi)
        if(supportActionBar!= null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "CALCULATE BMI"
        }
        binding?.toolbarBmi?.setNavigationOnClickListener{
            onBackPressed()
        }
        binding?.btnCalculate?.setOnClickListener {
            if(validateMetricUnits()){
                showBMI()
            }
        }

    }

    private fun showBMI(){
        val heightValue : Float = binding?.etMetricHeight?.text.toString().toFloat()/100
        val weightValue : Float = binding?.etMetricWeight?.text.toString().toFloat()
        val bmi = weightValue / heightValue.pow(2)
        binding?.llBmiResult?.visibility = View.VISIBLE
        binding?.tvBmiValue?.text = bmi.toString()
        if(bmi > 25){
            binding?.tvBmiType?.text = "Overweight"
            binding?.tvBmiDescription?.text = "Oops. I think you need to workout more often. Lets`s start now)"
        }
        else if(bmi<=25 && bmi > 18.5f){
            binding?.tvBmiType?.text = "Normal"
            binding?.tvBmiDescription?.text = "Oh, congratulations! You have a super condition"
        }
        else{
            binding?.tvBmiType?.text = "Underweight"
            binding?.tvBmiDescription?.text = "You need to take care of yourself!(("
        }
    }

    private fun validateMetricUnits():Boolean{
        var isValid = true
        if(binding?.etMetricWeight?.text.toString().isEmpty() || binding?.etMetricHeight?.text.toString().isEmpty() ) {

            Toast.makeText(this@BMIActivity, "Enter data please", Toast.LENGTH_SHORT).show()
            isValid = false
        }
        return isValid
    }

}