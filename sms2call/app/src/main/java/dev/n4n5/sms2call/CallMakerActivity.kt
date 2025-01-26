package dev.n4n5.sms2call

import android.content.Intent
import android.os.Bundle
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import dev.n4n5.sms2call.databinding.ActivityCallBinding


class CallMakerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bindings = ActivityCallBinding.inflate(layoutInflater)
        setContentView(bindings.root)
        val extras = intent.extras
        val phoneNumber = extras!!.getString("phone_number")
        if(phoneNumber == null){
            CallMaker(this).writeLog("CallMakerActivity(): bad phone_number received")
            return
        }
        CallMaker(this).makeCallTo(phoneNumber)

        android.os.Handler(Looper.getMainLooper()).postDelayed({
            val i: Intent = Intent(this, MainActivity::class.java)
            finish()
            startActivity(i)
        }, 10000)

        bindings.button.setOnClickListener {
            finish()
        }

    }
}
