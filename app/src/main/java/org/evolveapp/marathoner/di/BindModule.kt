package org.evolveapp.marathoner.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.evolveapp.data.repositories.*
import org.evolveapp.domain.repositories.*


@Module
@InstallIn(SingletonComponent::class)
abstract class BindModule {

    @Binds
    abstract fun bindProfileRepository(profileRepositoryImpl: ProfileRepositoryImpl): ProfileRepository

    @Binds
    abstract fun bindFriendsRepository(friendsRepositoryImpl: FriendsRepositoryImpl): FriendsRepository

    @Binds
    abstract fun bindRegistrationRepository(registrationRepositoryImpl: RegistrationRepositoryImpl): RegistrationRepository

    @Binds
    abstract fun bindMarathonRepository(marathonRepositoryImpl: MarathonsRepositoryImpl): MarathonsRepository

    @Binds
    abstract fun bindPostsRepository(createPostRepositoryImpl: PostsRepositoryImpl): PostsRepository

    @Binds
    abstract fun bindTasksRepository(addTaskRepositoryImpl: TasksRepositoryImpl): TasksRepository

}