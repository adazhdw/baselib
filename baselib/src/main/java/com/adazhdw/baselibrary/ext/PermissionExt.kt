package com.adazhdw.baselibrary.ext

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
import com.adazhdw.baselibrary.utils.permissionSP

object PermissionExt {

    const val PERMISSION_SP = "permission_sp"

    fun isGranted(permissions: Array<String>, context: Context? = null): Boolean {
        return permissions.all {
            isGranted(it, context)
        }
    }

    fun isGranted(permission: String, context: Context? = null): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
            context ?: LibUtil.getApp(),
            permission
        )
    }

    private var mOnGranted: ((Array<String>) -> Unit?)? = null
    private var mOnDenied: ((Array<String>) -> Unit?)? = null
    private val mPermissions = hashMapOf<String, Boolean>()

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
        this.mOnGranted = granted
        this.mOnDenied = denied

        permissions.forEach {
            if (!isGranted(it, context))
                mPermissions[it] = false
        }
        if (context != null) {
            PermissionActivity.start(context)
        } else {
            PermissionActivity.start(LibUtil.getApp())
        }
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
                val needRequest = mPermissions.filterKeys { permissionSP.getParam(it, 0) == 0 }.isNotEmpty()
                val shouldNotShow = mPermissions.filter { !shouldShowRequestPermissionRationale(it.key) }
                if (needRequest) {
                    requestPermissionss()
                } else {
                    if (shouldNotShow.isNotEmpty()) {
                        showLogDialog()
                    } else {
                        requestPermissionss()
                    }
                }
            }
        }

        private fun requestPermissionss() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(mPermissions.map { it.key }.toTypedArray(), IntentCode.REQUEST_PERMISSION_CODE)
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
            } else {
                onDenied.add(permissions[index])
            }
        }
        this.mOnGranted?.invoke(onGranted.toTypedArray())
        this.mOnDenied?.invoke(onDenied.toTypedArray())
    }
}