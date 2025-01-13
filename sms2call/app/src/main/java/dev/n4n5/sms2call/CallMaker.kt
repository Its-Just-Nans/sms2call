package dev.n4n5.sms2call

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.core.content.ContextCompat

class CallMaker (constructorContext: Context) {

    private var context: Context = constructorContext


    fun writeLog(logMsg: String){
        Log.w("myApp", logMsg)
    }

    fun makeCallTo(phoneNumber: String) {
        val phoneNumberWithTel = "tel:$phoneNumber"
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            writeLog( "Making call to $phoneNumberWithTel")
            val intent = Intent(Intent.ACTION_CALL).apply {
                data = Uri.parse(phoneNumberWithTel)
            }
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }else{
            writeLog( "Permissions was not granted")
        }
    }

    private fun getPhoneNumber( sender: String?, message: String): String? {
        writeLog( "getPhoneNumber(): '$sender' sent '$message'")
        val allCallInfo = CallInfo.getAllCallInfo(context)
        for (oneCallInfo in allCallInfo) {
            if(message.lowercase().startsWith(oneCallInfo.smsMessage)) {
                return oneCallInfo.phoneNumber
            }
        }
        writeLog("Invalid message - can't find number")
        return null
    }

    fun makeCallActivity(sender: String?, message: String) {
        writeLog("makeCallActivity(): started")
        val phoneNumber = getPhoneNumber(sender, message)
        if(phoneNumber == null){
            writeLog("makeCallActivity(): No number found")
            return
        }
        writeLog("makeCallActivity(): '$phoneNumber' found for '$message'")
        val intent = Intent(context, CallMakerActivity::class.java)
        intent.putExtra("phone_number", phoneNumber)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }


}