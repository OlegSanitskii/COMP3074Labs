package ca.gbc.comp3074.assignment1

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import com.google.android.material.appbar.MaterialToolbar
import kotlin.math.max

class MainActivity : AppCompatActivity() {

    private lateinit var etHours: EditText
    private lateinit var etRate: EditText
    private lateinit var etTax: EditText

    private lateinit var tvPay: TextView
    private lateinit var tvOvertime: TextView
    private lateinit var tvTotal: TextView
    private lateinit var tvTax: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.action_about) {
                startActivity(Intent(this, AboutActivity::class.java))
                true
            } else false
        }

        etHours = findViewById(R.id.etHours)
        etRate  = findViewById(R.id.etRate)
        etTax   = findViewById(R.id.etTax)

        tvPay      = findViewById(R.id.tvPay)
        tvOvertime = findViewById(R.id.tvOvertime)
        tvTotal    = findViewById(R.id.tvTotal)
        tvTax      = findViewById(R.id.tvTax)

        findViewById<Button>(R.id.btnCalc).setOnClickListener { calculate() }

        findViewById<Button>(R.id.btnAbout)?.setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
        }
    }

    private fun parseOrNull(s: String): Double? = s.trim().toDoubleOrNull()

    private fun calculate() {
        val hours = parseOrNull(etHours.text.toString())
        val rate  = parseOrNull(etRate.text.toString())
        var tax   = parseOrNull(etTax.text.toString())

        if (hours == null || rate == null || tax == null) {
            Toast.makeText(this, getString(R.string.error_required), Toast.LENGTH_SHORT).show()
            return
        }

        if (tax > 1.0) tax /= 100.0

        val baseHours = max(0.0, minOf(hours, 40.0))
        val overtimeHours = max(0.0, hours - 40.0)

        val pay = baseHours * rate
        val overtimePay = overtimeHours * rate * 1.5
        val totalPay = pay + overtimePay

        val taxAmount = pay * tax

        tvPay.text      = getString(R.string.out_pay, pay)
        tvOvertime.text = getString(R.string.out_ot, overtimePay)
        tvTotal.text    = getString(R.string.out_total, totalPay)
        tvTax.text      = getString(R.string.out_tax, taxAmount)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_about -> {
                startActivity(Intent(this, AboutActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
