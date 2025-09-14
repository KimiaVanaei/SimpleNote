package ir.sharif.simplenote.core.util

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import ir.sharif.simplenote.feature.auth.data.local.AuthDataStore

fun Context.authDataStore(): AuthDataStore {
    return AuthDataStore.getInstance(this)
}
