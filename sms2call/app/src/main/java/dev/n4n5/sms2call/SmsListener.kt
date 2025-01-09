package dev.n4n5.sms2call

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log

class SmsListener : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.w("myApp", "onReceive")
        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            for (message in messages) {
                val sender = message.originatingAddress
                val msgBody = message.messageBody
                CallMaker(context).makeCallActivity(sender, msgBody)
            }
            Log.w("myApp", "onReceive SMS end")

        }
    }
}
