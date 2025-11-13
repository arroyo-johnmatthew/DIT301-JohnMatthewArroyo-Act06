package com.example.apiconnectapp

import WeatherResponse
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var cityInput: EditText
    private lateinit var fetchButton: Button
    private lateinit var loadingIndicator: ProgressBar
    private lateinit var weatherResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        cityInput = findViewById(R.id.cityInput)
        fetchButton = findViewById(R.id.fetchButton)
        loadingIndicator = findViewById(R.id.loadingIndicator)
        weatherResult = findViewById(R.id.weatherResult)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        fetchButton.setOnClickListener {
            val city = cityInput.text.toString().trim()

            if (city.isEmpty()) {
                Toast.makeText(this, getString(R.string.enter_city_name), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!isConnected()) {
                Toast.makeText(this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            loadingIndicator.visibility = View.VISIBLE
            weatherResult.text = ""

            RetrofitClient.instance.getWeather(city, "aa3875d65d6e215967fd71b802c0070f")
                .enqueue(object : Callback<WeatherResponse> {
                    override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                        loadingIndicator.visibility = View.GONE

                        val weather = response.body()
                        if (response.isSuccessful && weather != null) {
                            val desc = weather.weather.firstOrNull()?.description ?: getString(R.string.no_description)
                            val temp = weather.main.temp
                            val humidity = weather.main.humidity

                            val resultText = String.format(
                                Locale.US,
                                "City: %s\nTemp: %.1f°C\nHumidity: %d%%\nCondition: %s",
                                weather.name,
                                temp,
                                humidity,
                                desc
                            )

                            weatherResult.text = resultText
                        } else {
                            weatherResult.text = getString(R.string.invalid_response)
                            Toast.makeText(this@MainActivity, getString(R.string.invalid_response), Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                        loadingIndicator.visibility = View.GONE
                        val errorText = getString(R.string.error_prefix, t.message ?: "Unknown error")
                        weatherResult.text = errorText

                        val toastText = getString(R.string.network_error_prefix, t.message ?: "Unknown error")
                        Toast.makeText(this@MainActivity, toastText, Toast.LENGTH_LONG).show()
                    }
                })
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}