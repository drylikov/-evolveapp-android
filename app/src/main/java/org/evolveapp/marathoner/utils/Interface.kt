package org.evolveapp.marathoner.utils

fun interface OnClick<T> {

    operator fun invoke(item: T, position: Int)

}

fun interface OnClickWithResult<T, Result> {

    suspend operator fun invoke(item: T, position: Int): Result

}