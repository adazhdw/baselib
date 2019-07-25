package com.adazhdw.baselibrary.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.adazhdw.baselibrary.LibUtil
import com.adazhdw.baselibrary.R
import com.adazhdw.baselibrary.annotation.IntentCode
import com.adazhdw.baselibrary.ext.launchSettings

object PermissionUtil {

    const val PERMISSION_SP_NAME = "permission_sp"

    fun isGranted(permissions: Array<String>, context: Context? = null): Boolean {
        return permissions.all {
            isGranted(it, context)
        }
    }

    fun isGranted(permission: String, context: Context? = null): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
            context ?: LibUtil.getApp().applicationContext,
            permission
        )
    }

    private var mOnGranted: ((Array<String>) -> Unit?)? = null
    private var mOnDenied: ((Array<String>) -> Unit?)? = null

    /**
     * one method request
     */
    fun requestPermissions(
        context: Context? = null,
        permissions: Array<String>,
        granted: ((Array<String>) -> Unit?)? = null,
        denied: ((Array<String>) -> Unit?)? = null
    ) {

        if (isGranted(permissions, context)) {
            granted?.invoke(permissions)
            return
        }
        mOnGranted = granted
        mOnDenied = denied

        if (context != null) {
            PermissionUtil.PermissionActivity.start(context, permissions)
        } else {
            PermissionUtil.PermissionActivity.start(LibUtil.getApp(), permissions)
        }
    }

    /**
     * the activity that request permission really
     */
    class PermissionActivity : AppCompatActivity() {

        companion object {
            fun start(context: Context, permissions: Array<String>) {
                val starter = Intent(context, PermissionActivity::class.java)
                starter.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                starter.putExtra("permissions", permissions)
                context.startActivity(starter)
            }
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            val permissions = intent.getStringArrayExtra("permissions") ?: emptyArray()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val needRequest = permissions.any { PERMISSION_SP.getParam(it, 0) == 0 }
                val shouldNotShow = permissions.filter { !shouldShowRequestPermissionRationale(it) }
                if (needRequest) {
                    requestPermissions(permissions, IntentCode.REQUEST_PERMISSION_CODE)
                } else {
                    if (shouldNotShow.isNotEmpty()) {
                        showLogDialog()
                    } else {
                        requestPermissions(permissions, IntentCode.REQUEST_PERMISSION_CODE)
                    }
                }
            }
        }

        /**
         * permission tip dialog
         */
        private fun showLogDialog() {
            AlertDialog.Builder(this, R.style.AlertDialogCorner)
                .setTitle(getString(R.string.permission_need_grant_title))
                .setMessage("应用需要相关权限才能运行，请前往应用详情页面授予相关权限")
                .setPositiveButton(getString(R.string.confirm_text)) { dialog, _ ->
                    launchSettings(IntentCode.USER_CHANGE_PERMISSION_CODE)
                    dialog?.dismiss()
                    finish()
                }
                .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                    dialog?.dismiss()
                    finish()
                }
                .show()
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
            if (requestCode == IntentCode.REQUEST_PERMISSION_CODE) {
                onResultCallback(permissions, grantResults)
                finish()
            }
        }
    }

    private fun onResultCallback(permissions: Array<out String>, grantResults: IntArray) {
        val onGranted = mutableListOf<String>()
        val onDenied = mutableListOf<String>()
        grantResults.forEachIndexed { index, i ->
            if (i == PackageManager.PERMISSION_GRANTED) {
                onGranted.add(permissions[index])
                PERMISSION_SP.putParam(permissions[index], 1)
            } else {
                onDenied.add(permissions[index])
                PERMISSION_SP.putParam(permissions[index], 0)
            }
        }
        if (onDenied.isNotEmpty()) {
            mOnDenied?.invoke(onDenied.toTypedArray())
            return
        }
        if (onGranted.isNotEmpty() && onGranted.size == grantResults.size) {
            mOnGranted?.invoke(onGranted.toTypedArray())
            return
        }
    }
}