package com.adazhdw.ktlib.core.lifecycle

import android.app.Activity
import java.util.*

object ActivityManager {

    private val mActivityList = LinkedList<Activity>()

    val currentActivity: Activity?
        get() = if (mActivityList.isEmpty()) null
        else mActivityList.last


    /**
     * push the specified [activity] into the list
     */
    fun pushActivity(activity: Activity) {
        if (mActivityList.contains(activity)) {
            if (mActivityList.last != activity) {
                mActivityList.remove(activity)
                mActivityList.add(activity)
            }
        }
    }

    /**
     * pop the specified [activity] out the list
     */
    fun popActivity(activity: Activity) {
        mActivityList.remove(activity)
    }

    /**
     * finish current activity
     */
    fun finishCurrentActivity() {
        currentActivity?.finish()
    }

    /**
     * finish [activity]
     */
    fun finishActivity(activity: Activity) {
        mActivityList.remove(activity)
        activity.finish()
    }

    /**
     * finish activity [clazz]
     */
    fun finishActivity(clazz: Class<*>) {
        for (activity in mActivityList) {
            if (activity.javaClass == clazz) {
                activity.finish()
            }
        }
    }

    /**
     * finish all activities
     */
    fun finishAllActivities() {
        for (activity in mActivityList) {
            activity.finish()
        }
        mActivityList.clear()
    }

}