package org.evolveapp.data

import org.evolveapp.data.models.categories.Category
import org.evolveapp.data.models.friends.Avatar
import org.evolveapp.data.models.friends.Friend
import org.evolveapp.data.models.friends.FriendRequest
import org.evolveapp.data.models.login.SocialLoginResponse
import org.evolveapp.data.models.login.User
import org.evolveapp.data.models.profile.Profile


fun Profile.map() = org.evolveapp.domain.models.profile.Profile(
    id = id,
    username = username,
    email = email,
    firstName = firstName,
    lastName = lastName,
    avatar = avatar,
    country = country,
    dateOfBirthday = dateOfBirthday,
    dateJoined = dateJoined,
    firstTime = firstTime,
    isActive = isActive
)

fun Avatar.map() = org.evolveapp.domain.models.friends.Avatar(localUrl = localUrl)

fun Friend.map() = org.evolveapp.domain.models.friends.Friend(
    id = id,
    username = username,
    firstName = firstName,
    lastName = lastName,
    avatar = avatar?.map()
)

fun FriendRequest.map() = org.evolveapp.domain.models.friends.FriendRequest()

fun User.map() = org.evolveapp.domain.models.login.User(
    id = id,
    username = username,
    firstName = firstName,
    lastName = lastName,
    avatar = avatar,
    firstTime = firstTime
)


fun SocialLoginResponse.map() = org.evolveapp.domain.models.login.SocialLoginResponse(
    user = user,
    refresh = refresh,
    access = access
)

fun org.evolveapp.data.models.marathon.participants.User.map() =
    org.evolveapp.domain.models.marathon.participants.User(
        id = id,
        username = username,
        firstName = firstName,
        lastName = lastName,
        avatar = avatar,
        firstTime = firstTime
    )

fun org.evolveapp.data.models.marathon.participants.Participant.map() =
    org.evolveapp.domain.models.marathon.participants.Participant(
        user = user?.map(),
        requestSent = requestSent
    )

fun org.evolveapp.data.models.marathon.participants.MarathonParticipants.map() =
    org.evolveapp.domain.models.marathon.participants.MarathonParticipants(
        friends = friends?.map { it.map() },
        participants = participants?.map { it.map() }
    )

fun Category.map() = org.evolveapp.domain.models.categories.Category(
    id = id,
    name = name
)