package com.adazhdw.baselibrary.component

import android.app.IntentService
import android.content.Intent

class DownloadService : IntentService("DownloadService") {
    override fun onHandleIntent(intent: Intent?) {
        intent?.runCatching {

        }
    }

}