package ca.gbc.comp3074.myapplication

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var lightSensor:Sensor? = null
    private lateinit var lightTV: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        lightTV = findViewById<TextView>(R.id.light)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        showAllSensors()
        setUpLightSensor()
    }


    private fun setUpLightSensor(){
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        if (lightSensor != null){
            Log.d("SENSOR", "Light sensor present")
        }else{
            Log.d("SENSOR", "Light sensor is not present")
        }
    }


    private fun showAllSensors() {
        val deviceSensors: List<Sensor> = sensorManager.getSensorList(Sensor.TYPE_ALL)
        val sensorsTV = findViewById<TextView>(R.id.sensor_list)
        sensorsTV.setText("")
        for (s in deviceSensors) {
            sensorsTV.append(s.toString()+"\n\n")
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.d("SENSOR","Accuracy for"+sensor?.name+" changed to " +accuracy)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_LIGHT){
            val light = event.values[0]
            Log.d("SENSOR", "Light:"+light)
            lightTV.setText(brightness(light))
        }
    }

    private fun brightness(value: Float): String {
        return when (value.toInt()) {
            0 -> "Pitch black"
            in 1..10 -> "Dark"
            in 11..50 -> "Grey"
            in 51..5000 -> "Normal"
            in 5001..25000 -> "Incredibly bright"
            else -> "This light will blind you"
        }
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener( this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL)
        Log.d( "SENSOR",  "Light listener registered")
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener( this)
        Log.d( "SENSOR", "Light listener unregistered")
    }

}
