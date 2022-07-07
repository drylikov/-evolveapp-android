package org.evolveapp.marathoner.ui.main.profile

import com.google.common.truth.Truth.assertThat
import org.junit.Test


class ProfileUtilsTest {

    private val profileUtils = ProfileUtils


    @Test
    fun capitalizeUserName_testcase1() {

        // Given
        val name = "jhon adam ronald"

        // When
        val result = profileUtils.capitalizeUserName(name)

        // Then
        assertThat(result).isEqualTo("Jhon Adam Ronald")

    }

    @Test
    fun capitalizeUserName_testcase2() {

        // Given
        val name = "abd elrahman hossam ali"

        // When
        val result = profileUtils.capitalizeUserName(name)

        // Then
        assertThat(result).isEqualTo("Abd Elrahman Hossam Ali")

    }

    @Test
    fun capitalizeUserName_testcase3() {

        // Given
        val name = "Ahmed mohamed Abdalla"

        // When
        val result = profileUtils.capitalizeUserName(name)

        // Then
        assertThat(result).isEqualTo("Ahmed Mohamed Abdalla")

    }

    @Test
    fun capitalizeUserName_testcase4() {

        // Given
        val name = "Ahmed mohamed "

        // When
        val result = profileUtils.capitalizeUserName(name)

        // Then
        assertThat(result).isEqualTo("Ahmed Mohamed")

    }

    @Test
    fun buildUserName_testcase1(){

        // Given
        val firstName = "cristiano "
        val lastName = " ronaldo"

        // When
        val result = profileUtils.buildUserName(firstName, lastName)

        // Then
        assertThat(result).isEqualTo("Cristiano Ronaldo")

    }

    @Test
    fun buildUserName_testcase2(){

        // Given
        val firstName = "cristiano adam"
        val lastName = " ronaldo"

        // When
        val result = profileUtils.buildUserName(firstName, lastName)

        // Then
        assertThat(result).isEqualTo("Cristiano Adam Ronaldo")

    }


}