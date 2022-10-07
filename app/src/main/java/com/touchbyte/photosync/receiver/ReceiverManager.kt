package com.touchbyte.photosync.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.touchbyte.photosync.receiver.ReceiverManager
import com.touchbyte.photosyncmdmtest.MainActivity
import java.lang.IllegalArgumentException
import java.lang.NullPointerException
import java.util.ArrayList

object ReceiverManager {
    val TAG = ReceiverManager::class.java.simpleName

    private val receivers: MutableList<BroadcastReceiver> = ArrayList()

    fun registerReceiver(receiver: BroadcastReceiver, intentFilter: IntentFilter?): Intent? {
        receivers.add(receiver)
        return MainActivity.applicationContext().registerReceiver(receiver, intentFilter)
    }

    fun isReceiverRegistered(receiver: BroadcastReceiver): Boolean {
        return receivers.contains(receiver)
    }

    fun unregisterReceiver(receiver: BroadcastReceiver) {
        if (isReceiverRegistered(receiver)) {
            receivers.remove(receiver)
            try {
                MainActivity.applicationContext().unregisterReceiver(receiver)
            } catch (e: IllegalArgumentException) {
            } catch (e: NullPointerException) {
            }
        }
    }
}