package ca.gbc.comp3074.lab6

import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class PressureActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var pressureSensor: Sensor? = null

    private lateinit var root: ConstraintLayout
    private lateinit var tvTitle: TextView
    private lateinit var tvReading: TextView
    private lateinit var tvStatus: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pressure)


        root = findViewById(R.id.rootPressure)
        tvTitle = findViewById(R.id.tvTitle)
        tvReading = findViewById(R.id.tvReading)
        tvStatus = findViewById(R.id.tvStatus)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)

        if (pressureSensor == null) {
            Toast.makeText(this, R.string.no_sensor, Toast.LENGTH_LONG).show()
            tvTitle.text = getString(R.string.app_name)
            tvReading.text = getString(R.string.no_sensor)
            tvStatus.text = ""
            root.setBackgroundColor(Color.DKGRAY)
            setTextColor(Color.WHITE)
        }
    }

    override fun onResume() {
        super.onResume()
        pressureSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type != Sensor.TYPE_PRESSURE) return

        val hPa = event.values[0]
        tvReading.text = String.format("Pressure: %.1f hPa", hPa)

        val (status, bgColor, txtColor) = when {
            hPa < 950f -> Triple(
                "Stormy / Low pressure (<950 hPa)",
                Color.DKGRAY,
                Color.WHITE
            )
            hPa <= 1010f -> Triple(
                "Normal weather (950–1010 hPa)",
                Color.BLUE,
                Color.WHITE
            )
            else -> Triple(
                "High pressure / Sunny (>1010 hPa)",
                Color.YELLOW,
                Color.BLACK
            )
        }

        tvStatus.text = status
        root.setBackgroundColor(bgColor)
        setTextColor(txtColor)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit

    private fun setTextColor(color: Int) {
        tvTitle.setTextColor(color)
        tvReading.setTextColor(color)
        tvStatus.setTextColor(color)
    }
}
