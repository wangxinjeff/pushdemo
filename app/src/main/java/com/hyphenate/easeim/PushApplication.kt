package com.hyphenate.easeim

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Process
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMOptions
import com.hyphenate.easeim.utils.PreferenceManager
import com.hyphenate.push.EMPushConfig

class PushApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        PreferenceManager.init(this)
        if (isMainProcess(this)) {
            val option = EMOptions()
            option.isUseFCM = PreferenceManager.getInstance().isUseFCM
            val builder = EMPushConfig.Builder(this)
            builder.enableVivoPush().enableHWPush()
                .enableMiPush("2882303761517426801", "5381742660801")
                .enableOppoPush(
                    "0bb597c5e9234f3ab9f821adbeceecdb",
                    "cd93056d03e1418eaa6c3faf10fd7537"
                )
                .enableMeiZuPush("134952", "f00e7e8499a549e09731a60a4da399e3")
                .enableFCM("921300338324")
            option.pushConfig = builder.build()
            EMClient.getInstance().init(this, option)
            EMClient.getInstance().setDebugMode(true)
        }
    }

    /**
     * 判断是否在主进程
     * @param context
     * @return
     */
    private fun isMainProcess(context: Context): Boolean {
        val pid = Process.myPid()
        val activityManager = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (appProcess in activityManager.runningAppProcesses) {
            if (appProcess.pid == pid) {
                return context.applicationInfo.packageName == appProcess.processName
            }
        }
        return false
    }
}