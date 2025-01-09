package dev.n4n5.sms2call

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import dev.n4n5.sms2call.databinding.EditCallInfoBinding
import dev.n4n5.sms2call.databinding.ListItemBinding


class CustomListAdapter(private val context: Activity, private val infos: ArrayList<CallInfo>)
    : ArrayAdapter<CallInfo>(context, R.layout.list_item, infos) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val rowView = ListItemBinding.inflate(context.layoutInflater)
        val currentCallInfo = infos[position]
        rowView.phoneNumber.text = currentCallInfo.smsMessage
        rowView.smsMessage.text =  currentCallInfo.phoneNumber
        rowView.buttonCall.setOnClickListener { _ ->
            CallMaker(context).makeCallActivity("button call", currentCallInfo.smsMessage)
        }
        rowView.buttonEdit.setOnClickListener { _ ->
            val bindings = EditCallInfoBinding.inflate(context.layoutInflater)
            bindings.editCallInfoSms.setText(currentCallInfo.smsMessage)
            bindings.editCallInfoPhone.setText(currentCallInfo.phoneNumber)
            AlertDialog.Builder(context).setTitle("Edition")
                .setView(bindings.root)
                .setPositiveButton("OK") { _, _ ->
                    currentCallInfo.phoneNumber = bindings.editCallInfoPhone.text.toString()
                    currentCallInfo.smsMessage = bindings.editCallInfoSms.text.toString()
                    CallInfo.saveAllCallInfo(context, this.infos)
                    this.notifyDataSetChanged()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
        rowView.buttonDelete.setOnClickListener { _ ->
            AlertDialog.Builder(context)
                .setMessage("Are you sure?")
                .setPositiveButton("Yes") { _, _ ->
                    this.remove(currentCallInfo)
                    CallInfo.saveAllCallInfo(context, this.infos)
                }
                .setNegativeButton("No", null)
                .show()
        }

        return rowView.root
    }
}