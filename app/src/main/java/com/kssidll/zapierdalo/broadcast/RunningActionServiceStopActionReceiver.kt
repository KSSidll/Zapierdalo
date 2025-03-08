package com.kssidll.zapierdalo.broadcast

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.kssidll.zapierdalo.service.RunningActionService

/**
 * Receiver that stops the [RunningActionService]
 */
class RunningActionServiceStopActionReceiver: BroadcastReceiver() {
    @SuppressLint("LongLogTag")
    override fun onReceive(
        context: Context?,
        intent: Intent?
    ) {
        Log.d(
            TAG,
            "onReceive: received"
        )

        context?.let {
            RunningActionService.stop(it, null)
        }
    }

    companion object {
        const val TAG = "RUNNING_ACTION_SERVICE_STOP_RECEIVER"
    }
}