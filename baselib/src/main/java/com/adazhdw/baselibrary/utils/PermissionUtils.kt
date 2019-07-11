package com.adazhdw.baselibrary.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.adazhdw.baselibrary.LibUtil
import com.adazhdw.baselibrary.R
import com.adazhdw.baselibrary.annotation.IntentCode.REQUEST_PERMISSION_CODE
import com.adazhdw.baselibrary.annotation.IntentCode.USER_CHANGE_PERMISSION_CODE
import com.adazhdw.baselibrary.ext.launchSettings

object PermissionUtils {

    const val PERMISSION_SP = "permission_sp"

    fun isGranted(permissions: Array<String>, context: Context? = null): Boolean {
        return permissions.all { permission ->
            isGranted(permission, context)
        }
    }

    fun isGranted(permission: String, context: Context? = null): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
            context ?: LibUtil.getApp(),
            permission
        )
    }

    private var mOnGranted: ((Array<String>) -> Unit?)? =null
    private var mOnDenied: ((Array<String>) -> Unit?)? =null
    private val mPermissionList: ArrayList<String> = arrayListOf()
    private var mExplainString: String = ""

    /**
     * one method request
     */
    fun requestPermissions(
        context: Context? = null,
        permissions: Array<String>,
        onGranted: ((Array<String>) -> Unit?)? =null,
        onDenied: ((Array<String>) -> Unit?)? =null,
        mExplainString: String? = null
    ) {
        if (permissions.isEmpty())
            return
        if (isGranted(permissions, context)) {
            onGranted?.invoke(permissions.filter { isGranted(it, context) }.toTypedArray())
            return
        }
        this.mExplainString = mExplainString ?: ""
        mPermissionList.clear()
        mPermissionList.addAll(permissions.map { it }.toList())
        mOnGranted = onGranted
        mOnDenied = onDenied
        if (context != null) {
            PermissionActivity.start(context)
        } else {
            PermissionActivity.start(LibUtil.getApp())
        }
    }

    /**
     * get permission is granted status
     */
    fun getPermissionState(permissions: Array<out String>, grantResults: IntArray) {
        val grantedArray: Array<String> = permissions.filterIndexed { index, s ->
            //save permission has requested status
            permissionSP.putParam(s, 1)
            grantResults[index] == PackageManager.PERMISSION_GRANTED
        }.toTypedArray()

        val deniedArray: Array<String> = permissions.filterIndexed { index, s ->
            //save permission has requested status
            permissionSP.putParam(s, 1)
            grantResults[index] != PackageManager.PERMISSION_GRANTED
        }.toTypedArray()
        mOnGranted?.invoke(grantedArray)
        mOnDenied?.invoke(deniedArray)
    }

    /**
     * the activity that request permission really
     */
    class PermissionActivity : AppCompatActivity() {

        companion object {
            fun start(context: Context) {
                val starter = Intent(context, PermissionActivity::class.java)
                starter.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(starter)
            }
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val isNotGranted = mPermissionList.filter { !isGranted(it, this) }.toMutableList()
                val needRequest = mPermissionList.any {
                    //get permission has requested status
                    permissionSP.getParam(it, 0) == 0
                }
                val shouldNotShow = isNotGranted.filter { !shouldShowRequestPermissionRationale(it) }.toMutableList()
                if (isNotGranted.isNotEmpty()) {
                    if (needRequest) {
                        requestPermissions(isNotGranted.toTypedArray(), REQUEST_PERMISSION_CODE)
                    } else {
                        if (shouldNotShow.isNotEmpty()) {
                            showLogDialog(shouldNotShow)
                            return
                        } else {
                            requestPermissions(isNotGranted.toTypedArray(), REQUEST_PERMISSION_CODE)
                        }
                    }
                }
            }
        }

        /**
         * permission tip dialog
         */
        private fun showLogDialog(permissions: MutableList<String>) {
            AlertDialog.Builder(this, R.style.AlertDialogCorner)
                .setTitle(getString(R.string.permission_need_grant_title))
                .setMessage(
                    if (mExplainString.isNotEmpty()) {
                        mExplainString
                    } else {
                        "App needs permissions:\n" + TextUtils.join(
                            "\n",
                            getPermissionText(permissions)
                        ) + "\nplease grant permission"
                    }
                )
                .setCancelable(false)
                .setPositiveButton(
                    getString(R.string.confirm_text)
                ) { dialog, _ ->
                    launchSettings(USER_CHANGE_PERMISSION_CODE)
                    dialog?.dismiss()
                    finish()
                }
                .setNegativeButton(
                    getString(R.string.cancel)
                ) { dialog, _ ->
                    dialog?.dismiss()
                    finish()
                }
                .show()
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (requestCode == USER_CHANGE_PERMISSION_CODE) {
                getPermissionState(mPermissionList.toTypedArray(), getGrantResult())
            }
        }

        /**
         * get permissions grantResults by mPermissionList
         */
        private fun getGrantResult(): IntArray {
            val intArray = IntArray(mPermissionList.size)
            mPermissionList.forEachIndexed { index, s ->
                if (isGranted(s, this)) {
                    intArray[index] = PackageManager.PERMISSION_GRANTED
                } else {
                    intArray[index] = PackageManager.PERMISSION_DENIED
                }
            }
            return intArray
        }

        /**
         * permission result callback
         */
        override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
        ) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            if (requestCode == REQUEST_PERMISSION_CODE) {
                getPermissionState(permissions, grantResults)
                finish()
            }
        }
    }

    private const val normalReplaceStr = "android.permission."
    private const val replaceStr1 = "com.android.launcher.permission."
    private const val replaceStr2 = "com.android.voicemail.permission."
    fun getPermissionText(permissions: MutableList<String>): MutableList<String> {
        return permissions.map {
            when {
                it.contains(replaceStr1) -> it.replace(replaceStr1, "").toLowerCase()
                it.contains(replaceStr2) -> it.replace(replaceStr2, "").toLowerCase()
                it.contains(normalReplaceStr) -> it.replace(normalReplaceStr, "").toLowerCase()
                else -> {
                    ""
                }
            }
        }.toMutableList()
    }

}
/*

*/
/**
 * permissions callback
 *//*

interface OnPermissionCallback {
    fun onGranted(permissions: Array<out String>)
    fun onDenied(permissions: Array<out String>)
}

*/
/**
 * Permissions simple
 *//*


//CALENDAR
const val READ_CALENDAR = Manifest.permission.READ_CALENDAR
const val WRITE_CALENDAR = Manifest.permission.WRITE_CALENDAR
//CALL_LOG
const val READ_CALL_LOG = Manifest.permission.READ_CALL_LOG
const val WRITE_CALL_LOG = Manifest.permission.WRITE_CALL_LOG
const val PROCESS_OUTGOING_CALLS = Manifest.permission.PROCESS_OUTGOING_CALLS
//CONTACTS
const val READ_CONTACTS = Manifest.permission.READ_CONTACTS
const val WRITE_CONTACTS = Manifest.permission.WRITE_CONTACTS
const val GET_ACCOUNTS = Manifest.permission.GET_ACCOUNTS
//LOCATION
const val ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
const val ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
//MICROPHONE
const val RECORD_AUDIO = Manifest.permission.RECORD_AUDIO
//PHONE
const val READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE
@RequiresApi(Build.VERSION_CODES.O)
const val READ_PHONE_NUMBERS = Manifest.permission.READ_PHONE_NUMBERS
const val CALL_PHONE = Manifest.permission.CALL_PHONE
@RequiresApi(Build.VERSION_CODES.O)
const val ANSWER_PHONE_CALLS = Manifest.permission.ANSWER_PHONE_CALLS
const val ADD_VOICEMAIL = Manifest.permission.ADD_VOICEMAIL
const val USE_SIP = Manifest.permission.USE_SIP
//SENSORS
@RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
const val BODY_SENSORS = Manifest.permission.BODY_SENSORS
//SMS
const val SEND_SMS = Manifest.permission.SEND_SMS
const val RECEIVE_SMS = Manifest.permission.RECEIVE_SMS
const val READ_SMS = Manifest.permission.READ_SMS
const val RECEIVE_WAP_PUSH = Manifest.permission.RECEIVE_WAP_PUSH
const val RECEIVE_MMS = Manifest.permission.RECEIVE_MMS
//STORAGE
const val READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE
const val WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE

*/
