package org.evolveapp.marathoner.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.evolveapp.data.local.PrefDao
import org.evolveapp.data.remote.HeaderInterceptor
import org.evolveapp.data.remote.WebService
import org.evolveapp.data.repositories.*

@Module
@InstallIn(SingletonComponent::class)
object ProvideModule {

    @Provides
    fun providePrefDao(@ApplicationContext context: Context): PrefDao {
        return PrefDao.getInstance(context)
    }

    @Provides
    fun provideHeaderInterceptor(
        @ApplicationContext context: Context,
        prefDao: PrefDao
    ): HeaderInterceptor {
        return HeaderInterceptor(context, prefDao)
    }

    @Provides
    fun provideWebService(headerInterceptor: HeaderInterceptor): WebService {
        return WebService.invoke(headerInterceptor)
    }

    @Provides
    fun provideProfileRepositoryImpl(webService: WebService): ProfileRepositoryImpl {
        return ProfileRepositoryImpl(webService)
    }

    @Provides
    fun provideFriendsRepositoryImpl(webService: WebService): FriendsRepositoryImpl {
        return FriendsRepositoryImpl(webService)
    }

    @Provides
    fun provideRegistrationRepositoryImpl(webService: WebService): RegistrationRepositoryImpl {
        return RegistrationRepositoryImpl(webService)
    }

    @Provides
    fun provideMarathonRepositoryImpl(webService: WebService): MarathonsRepositoryImpl {
        return MarathonsRepositoryImpl(webService)
    }

    @Provides
    fun providePostsRepositoryImpl(webService: WebService): PostsRepositoryImpl {
        return PostsRepositoryImpl(webService)
    }

    @Provides
    fun provideAddTaskRepositoryImpl(webService: WebService): TasksRepositoryImpl {
        return TasksRepositoryImpl(webService)
    }

}