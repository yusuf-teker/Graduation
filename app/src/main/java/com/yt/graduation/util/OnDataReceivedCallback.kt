package com.yt.graduation.util

import kotlin.collections.ArrayList

interface OnDataReceivedCallback<T> {
        fun onDataReceived(list: ArrayList<T>)
        fun onDataReceived(data: String)
}