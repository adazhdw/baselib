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
            onGranted?.invoke(permissions)
            return
        }
        this.mExplainString = mExplainString ?: ""
        this.mPermissionList.clear()
        this.mPermissionList.addAll(permissions.toList())
        this.mOnGranted = onGranted
        this.mOnDenied = onDenied
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
        this.mOnGranted?.invoke(grantedArray)
        this.mOnDenied?.invoke(deniedArray)
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
                        "应用需要相关权限才能运行，请前往应用详情页面授予相关权限"
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
}