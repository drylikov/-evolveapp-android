package org.evolveapp.marathoner.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.evolveapp.domain.repositories.*
import org.evolveapp.domain.usecases.friends.GetFriendsRequestsUseCase
import org.evolveapp.domain.usecases.friends.GetFriendsUseCase
import org.evolveapp.domain.usecases.marathon.CreateMarathonUseCase
import org.evolveapp.domain.usecases.marathon.GetCategoriesUseCase
import org.evolveapp.domain.usecases.marathon.GetParticipantsUseCase
import org.evolveapp.domain.usecases.marathon.posts.CreatePostUseCase
import org.evolveapp.domain.usecases.marathon.tasks.AddTaskUseCase
import org.evolveapp.domain.usecases.profile.*
import org.evolveapp.domain.usecases.registration.LoginUseCase

@Module
@InstallIn(SingletonComponent::class)
object UseCasesModule {

    @Provides
    fun provideGetProfileIeUseCase(profileRepository: ProfileRepository): GetProfileIeUseCase {
        return GetProfileIeUseCase(profileRepository)
    }

    @Provides
    fun provideGetFriendsUseCase(friendsRepository: FriendsRepository): GetFriendsUseCase {
        return GetFriendsUseCase(friendsRepository)
    }

    @Provides
    fun provideGetFriendsRequestsUseCase(friendsRepository: FriendsRepository): GetFriendsRequestsUseCase {
        return GetFriendsRequestsUseCase(friendsRepository)
    }

    @Provides
    fun provideLoginUseCase(registrationRepository: RegistrationRepository): LoginUseCase {
        return LoginUseCase(registrationRepository)
    }

    @Provides
    fun provideGetParticipantsUseCase(marathonsRepository: MarathonsRepository): GetParticipantsUseCase {
        return GetParticipantsUseCase(marathonsRepository)
    }

    @Provides
    fun provideSendFriendRequestUseCase(profileRepository: ProfileRepository): SendFriendRequestUseCase {
        return SendFriendRequestUseCase(profileRepository)
    }

    @Provides
    fun provideUpdateProfileUseCase(profileRepository: ProfileRepository): UpdateProfileUseCase {
        return UpdateProfileUseCase(profileRepository)
    }

    @Provides
    fun provideUpdateProfilePhotoUseCase(profileRepository: ProfileRepository): UpdateProfilePhotoUseCase {
        return UpdateProfilePhotoUseCase(profileRepository)
    }

    @Provides
    fun provideCreatePostUseCase(postsRepository: PostsRepository): CreatePostUseCase {
        return CreatePostUseCase(postsRepository)
    }

    @Provides
    fun provideAddTaskUseCase(tasksRepository: TasksRepository): AddTaskUseCase {
        return AddTaskUseCase(tasksRepository)
    }

    @Provides
    fun provideLogoutUseCase(profileRepository: ProfileRepository): LogoutUseCase {
        return LogoutUseCase(profileRepository)
    }

    @Provides
    fun provideCreateMarathonUseCase(marathonsRepository: MarathonsRepository): CreateMarathonUseCase {
        return CreateMarathonUseCase(marathonsRepository)
    }

    @Provides
    fun provideGetCategoriesUseCase(marathonsRepository: MarathonsRepository): GetCategoriesUseCase {
        return GetCategoriesUseCase(marathonsRepository)
    }

}