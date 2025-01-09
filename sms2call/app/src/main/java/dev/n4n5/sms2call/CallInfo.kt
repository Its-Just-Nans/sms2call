package dev.n4n5.sms2call

import android.content.Context
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
class CallInfo(var phoneNumber: String, var smsMessage: String) {

    companion object {
        private const val STORAGE_KEY = "storage"
        private const val PREF_KEY = "pref"


        fun getAllCallInfo(context: Context) : ArrayList<CallInfo> {
            val dataStr = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE).getString(STORAGE_KEY, null)
                ?: return saveAllCallInfo(context, ArrayList())
            return try {
                Json.decodeFromString<ArrayList<CallInfo>>(dataStr)
            } catch(e : Exception){
                saveAllCallInfo(context, ArrayList())
            }
        }

        fun saveAllCallInfo(context: Context, data: ArrayList<CallInfo>): ArrayList<CallInfo> {
            val editor = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE).edit()
            val dataStr = Json.encodeToString(data)
            editor.putString(STORAGE_KEY, dataStr)
            editor.apply()
            return data
        }
    }

}
