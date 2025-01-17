package dev.n4n5.sms2call

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.PowerManager
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import dev.n4n5.sms2call.databinding.ActivityMainBinding
import dev.n4n5.sms2call.databinding.EditCallInfoBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ArrayAdapter<CallInfo>
    private lateinit var callInfoList: ArrayList<CallInfo>

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection.
        return when (item.itemId) {
            R.id.menu_check -> {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(this, "Requesting permission for CALL_PHONE", Toast.LENGTH_SHORT).show()
                    requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), 1)
                }
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(this, "Requesting permission for RECEIVE_SMS", Toast.LENGTH_SHORT).show()
                    requestPermissions(arrayOf(Manifest.permission.RECEIVE_SMS), 2)
                }
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.BROADCAST_SMS) == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(this, "Requesting permission for BROADCAST_SMS", Toast.LENGTH_SHORT).show()
                    requestPermissions(arrayOf(Manifest.permission.BROADCAST_SMS), 2)
                }
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.SYSTEM_ALERT_WINDOW) == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(this, "Window overlay LOOKS NOT enabled", Toast.LENGTH_SHORT).show()
                    requestPermissions(arrayOf(Manifest.permission.SYSTEM_ALERT_WINDOW), 3)
                }
                val pm = getSystemService(POWER_SERVICE) as PowerManager
                if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                    Toast.makeText(this, "Battery optimization LOOKS NOT enabled for this app", Toast.LENGTH_SHORT).show()
                }
                true
            }
            R.id.menu_reset -> {
                AlertDialog.Builder(this)
                    .setMessage("Are you sure to delete all data?")
                    .setPositiveButton("Yes") { _, _ ->
                        CallInfo.saveAllCallInfo(this, ArrayList())
                        callInfoList.clear()
                        adapter.notifyDataSetChanged()
                    }
                    .setNegativeButton("No", null)
                    .show()

                true
            }
            R.id.menu_about -> {
                startActivity(Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse("https://sms2call.n4n5.dev")
                })
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        callInfoList = CallInfo.getAllCallInfo(this)
        adapter = CustomListAdapter(
            this@MainActivity,
            callInfoList
        )
        binding.idLVLanguages.adapter = adapter
        binding.idBtnAdd.setOnClickListener {
            val bindings = EditCallInfoBinding.inflate(layoutInflater)
            AlertDialog.Builder(this).setTitle("Create new")
                .setView(bindings.root)
                .setPositiveButton("OK") { _, _ ->
                    val phone = bindings.editCallInfoPhone.text.toString()
                    val sms = bindings.editCallInfoSms.text.toString()
                    val newCallInfo = CallInfo(phone, sms)
                    callInfoList.add(newCallInfo)
                    CallInfo.saveAllCallInfo(this, callInfoList)
                    adapter.notifyDataSetChanged()

                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }
}
