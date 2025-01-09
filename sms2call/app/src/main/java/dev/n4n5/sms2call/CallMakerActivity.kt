package dev.n4n5.sms2call

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dev.n4n5.sms2call.databinding.ActivityCallBinding

class CallMakerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bindings = ActivityCallBinding.inflate(layoutInflater)
        setContentView(bindings.root)
        val extras = intent.extras
        val phoneNumber = extras!!.getString("phone_number")
        if(phoneNumber != null){
            CallMaker(this).makeCallTo( phoneNumber)
        }
        bindings.button.setOnClickListener {
            finishAffinity()  // This closes all activities and exits the app
        }

    }
}
