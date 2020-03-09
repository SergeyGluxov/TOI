package com.example.wavelet.helpers

import kotlin.DoubleArray

object HDoubleArray {
    fun getMax(doubleArray: Array<DoubleArray>): Double {
        var max = 0.0
        for (i in doubleArray.indices) {
            for (j in 0 until doubleArray.count()) {
                if (max < doubleArray[i][j]) {
                    max = doubleArray[i][j]
                }
            }
        }
        return max
    }

    fun getMin(doubleArray: Array<DoubleArray>): Double {
        var min = 0.0
        for (i in doubleArray.indices) {
            for (j in 0 until doubleArray.count()) {
                if (min > doubleArray[i][j]) {
                    min = doubleArray[i][j]
                }
            }
        }
        return min
    }
}