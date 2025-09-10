package ir.sharif.simplenote.data.repo

import android.content.Context
import java.text.SimpleDateFormat
import java.util.*

class LastSyncStore(context: Context) {
    private val prefs = context.getSharedPreferences("sync_prefs", Context.MODE_PRIVATE)
    private val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    fun getLastSuccessfulPull(): String? = prefs.getString("last_pull_iso", null)

    fun setLastSuccessfulPullToNow() {
        val nowIso = sdf.format(Date())
        prefs.edit().putString("last_pull_iso", nowIso).apply()
    }
}