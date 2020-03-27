package com.example.wavelet.models

object Polinome {
    fun getA0(function: Function, index: Int): Double {
        return function.coordinateList[index + 1].y
    }

    fun getA1(function: Function, index: Int): Double {
        return (1.0 / 2) * (function.coordinateList[index + 2].y - function.coordinateList[index].y) - getA3(
            function,
            index
        )
    }

    fun getA2(function: Function, index: Int): Double {
        return function.coordinateList[index + 2].y - function.coordinateList[index + 1].y - getA1(
            function,
            index
        ) - getA3(function, index)
    }

    fun getA3(function: Function, index: Int): Double {
        return (1.0 / 6.0) * (function.coordinateList[index + 2].y -
                function.coordinateList[index - 1].y) + 1.0 / 2 *
                (function.coordinateList[index].y - function.coordinateList[index + 1].y)
    }
}