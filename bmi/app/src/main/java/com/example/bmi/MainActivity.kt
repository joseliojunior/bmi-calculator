package com.example.bmi

/*
* Author: Josélio de S. C. Júnior
* Last modification date: April 6th, 2021
*
* Modifications
* - Replaced findViewById() with View Binding feature.
* - Updated background colors to CDC, 2020 reference in.
* - Updated function riskColorAPI23().
* - Updated function riskColorAPI22().
*
*/

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.core.view.isVisible
import com.example.bmi.databinding.ActivityMainBinding
import kotlin.math.pow
import kotlin.math.round

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        var ref = 0
        val ref1Button = binding.ref1
        val ref2Button = binding.ref2
        val h = binding.height
        val w = binding.weight
        val calculate = binding.calculate
        val resultText = binding.result
        val referenceText = binding.reference

        ref1Button.setOnClickListener {
            ref = 1
            referenceText.text = getString(R.string.ref_who97)
            ref1Button.isSelected = true
            ref2Button.isSelected = false
        }
        ref2Button.setOnClickListener {
            ref = 0
            referenceText.text = getString(R.string.ref_cdc20)
            ref1Button.isSelected = false
            ref2Button.isSelected = true
        }

        calculate.setOnClickListener {
            val bmi = bmi(w.text.toString(), h.text.toString())
            riskClassification(resultText, referenceText, ref, bmi)
        }
    }

    private fun bmi(w: String, h: String): Float{
        val weight = w.toFloatOrNull()
        val height = h.toFloatOrNull()

        return when {
            weight == 0f || height == 0f -> {
                -1.3f
            }
            weight != null && weight !in 10f..600f -> {
                -1.1f
            }
            height != null && height !in 0.2f..3f -> {
                -1.2f
            }
            weight != null && height != null -> {
                weight / height.pow(2f)
            }
            else -> {
                -1f
            }
        }
    }

    private fun riskClassification(resultText: TextView, referenceText: TextView, reference:Int, bmi: Float) {
        val r = round(bmi*100f)/100f

        fun convert(a:String) = "${r.toString().replace(".",",")} kg/m²\n$a"

        resultText.isVisible = true
        resultText.text = if (reference == 1) {
                when (bmi) {
                    -1.3f -> {
                        getString(R.string.zero_input)
                    }
                    -1.2f -> {
                        getString(R.string.h_out_of_bounds)
                    }
                    -1.1f -> {
                        getString(R.string.w_out_of_bounds)
                    }
                    -1f -> {
                        getString(R.string.no_input)
                    }
                    in 1f..15.99f -> {
                        convert(getString(R.string.u3))
                    }
                    in 16f..16.99f -> {
                        convert(getString(R.string.u2))
                    }
                    in 17f..18.49f -> {
                        convert(getString(R.string.u1))
                    }
                    in 18.5f..24.99f -> {
                        convert(getString(R.string.nw))
                    }
                    in 25f..29.99f -> {
                        convert(getString(R.string.pre))
                    }
                    in 30f..34.99f -> {
                        convert(getString(R.string.o1))
                    }
                    in 35f..39.99f -> {
                        convert(getString(R.string.o2))
                    }
                    else -> {
                        convert(getString(R.string.o3))
                    }
                }
            } else {
                when (bmi) {
                    -1.3f -> {
                        getString(R.string.zero_input)
                    }
                    -1.2f -> {
                        getString(R.string.h_out_of_bounds)
                    }
                    -1.1f -> {
                        getString(R.string.w_out_of_bounds)
                    }
                    -1f -> {
                        getString(R.string.no_input)
                    }
                    in 1f..18.49f -> {
                        convert(getString(R.string.uw))
                    }
                    in 18.5f..24.99f -> {
                        convert(getString(R.string.nw))
                    }
                    in 25f..29.99f -> {
                        convert(getString(R.string.pre))
                    }
                    else ->  {
                        convert(getString(R.string.ob))
                    }
                }
            }

        referenceText.isVisible = bmi !in -1.3f..-1f
        referenceText.text = if (reference == 1) {
            getString(R.string.ref_who97)
        } else { getString(R.string.ref_cdc20) }

        riskColorAPI23(resultText, bmi, reference) ?: riskColorAPI22(resultText, bmi, reference)
    }

    private fun riskColorAPI23(textView: TextView, bmi: Float, ref: Int): Unit? {
        val black = Color.parseColor("#000000")
        val white = Color.parseColor("#ffffff")

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            fun setColor(background: Int, text: Int) {
                textView.setTextColor(text)
                textView.backgroundTintList = ColorStateList.valueOf(getColor(background))
            }

            if (ref == 1) {
                when (bmi) {
                    in -1.3f..-1f -> {
                        setColor(R.color.alert, black)
                    }
                    in 1f..15.99f -> {
                        setColor(R.color.risk4, white)
                    }
                    in 16f..16.99f -> {
                        setColor(R.color.risk3, white)
                    }
                    in 17f..18.49f -> {
                        setColor(R.color.risk2, white)
                    }
                    in 18.5f..24.99f -> {
                        setColor(R.color.risk0, white)
                    }
                    in 25f..29.99f -> {
                        setColor(R.color.risk1, black)
                    }
                    in 30f..34.99f -> {
                        setColor(R.color.risk2, white)
                    }
                    in 35f..39.99f -> {
                        setColor(R.color.risk3, white)
                    }
                    else -> {
                        setColor(R.color.risk4, white)
                    }
                }
            } else {
                when (bmi) {
                    in -1.3f..-1f -> {
                        setColor(R.color.alert, black)
                    }
                    in 1f..18.49f -> {
                        setColor(R.color.risk4, white)
                    }
                    in 18.5f..24.99f -> {
                        setColor(R.color.risk0, white)
                    }
                    in 25f..29.99f -> {
                        setColor(R.color.risk1, black)
                    }
                    else -> {
                        setColor(R.color.risk4, white)
                    }
                }
            }
        } else { null }
    }

    private fun riskColorAPI22(textView: TextView, bmi: Float, ref: Int) {
        val black = "#000000"
        val white = "#ffffff"

        fun setColor(background: Int, text: String) {
            textView.setTextColor(Color.parseColor(text))
            textView.backgroundTintList =
                ColorStateList.valueOf(Color.parseColor(getString(background)))
        }

        if (ref == 1) {
            when (bmi) {
                in -1.3f..-1f -> {
                    setColor(R.string.alert, black)
                }
                in 1f..15.99f -> {
                    setColor(R.string.risk4, white)
                }
                in 16f..16.99f -> {
                    setColor(R.string.risk3, white)
                }
                in 17f..18.49f -> {
                    setColor(R.string.risk2, white)
                }
                in 18.5f..24.99f -> {
                    setColor(R.string.risk0, white)
                }
                in 25f..29.99f -> {
                    setColor(R.string.risk1, black)
                }
                in 30f..34.99f -> {
                    setColor(R.string.risk2, white)
                }
                in 35f..39.99f -> {
                    setColor(R.string.risk3, white)
                }
                else -> {
                    setColor(R.string.risk4, white)
                }
            }
        } else {
            when (bmi) {
                in -1.3f..-1f -> {
                    setColor(R.string.alert, black)
                }
                in 1f..18.49f -> {
                    setColor(R.string.risk4, white)
                }
                in 18.5f..24.99f -> {
                    setColor(R.string.risk0, white)
                }
                in 25f..29.99f -> {
                    setColor(R.string.risk1, black)
                }
                else -> {
                    setColor(R.string.risk4, white)
                }
            }
        }
    }

}