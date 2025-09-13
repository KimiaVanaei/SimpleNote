package ir.sharif.simplenote.app.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ir.sharif.simplenote.data.user.UserProfileRepository
import ir.sharif.simplenote.data.user.HybridUserProfileRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserProfileModule {

    @Provides
    @Singleton
    fun provideUserProfileRepository(
        @ApplicationContext context: Context
    ): UserProfileRepository {
        val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        return HybridUserProfileRepository(context, scope)
    }
}

