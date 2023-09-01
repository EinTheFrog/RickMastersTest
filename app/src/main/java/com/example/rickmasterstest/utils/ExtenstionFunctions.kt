package com.example.rickmasterstest.utils

fun<T> List<T>.substituteElement(oldElement: T, newElement: T): List<T> {
    val newList = this.toMutableList()
    val cameraIndex = newList.indexOf(oldElement)
    newList.remove(oldElement)
    newList.add(cameraIndex, newElement)
    return newList
}