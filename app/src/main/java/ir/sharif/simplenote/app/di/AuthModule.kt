package ir.sharif.simplenote.app.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.core.DataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import ir.sharif.simplenote.feature.auth.data.local.AuthDataStore
import ir.sharif.simplenote.feature.auth.data.repository.AuthRepositoryImpl
import ir.sharif.simplenote.feature.auth.domain.repository.AuthRepository
import ir.sharif.simplenote.feature.auth.data.remote.AuthApi

private val Context.dataStore by preferencesDataStore(name = "auth_prefs")

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        context.dataStore

    @Provides
    @Singleton
    fun provideAuthDataStore(ds: DataStore<Preferences>): AuthDataStore =
        AuthDataStore(ds)

    @Provides
    @Singleton
    fun provideAuthRepository(
        api: AuthApi,
        ds: AuthDataStore
    ): AuthRepository = AuthRepositoryImpl(api, ds)
}
