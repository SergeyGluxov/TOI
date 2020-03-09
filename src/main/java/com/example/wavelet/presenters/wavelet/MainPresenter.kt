package com.example.wavelet.presenters.wavelet

import com.example.wavelet.helpers.HDoubleArray
import com.example.wavelet.models.Coordinate
import com.example.wavelet.models.Function
import com.example.wavelet.models.Image
import com.example.wavelet.views.IMainView
import moxy.InjectViewState
import moxy.MvpPresenter
import kotlin.math.*

@InjectViewState
class MainPresenter : MvpPresenter<IMainView>() {
    companion object {
        private const val maxColor = 255
        private const val period = 10 * Math.PI
        var step: Double = period / (200 - 1)
    }

    var image = Image(500, 500)

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        createSimpleFunc()
    }

    //-----------------------Построение функций-----------------------------------------------------
    fun createSimpleFunc() {
        var i: Double = -period
        val coordinate = ArrayList<Coordinate>()
        while (i <= period) {
            coordinate.add(
                Coordinate(
                    i,
                    simpleFunc(i)
                )
            )
            i += step
        }
        viewState.drawFunc(Function(coordinate))
    }

    fun createWaveletFunc() {
        var i: Double = -period
        val coordinate = ArrayList<Coordinate>()
        while (i <= period) {
            coordinate.add(
                Coordinate(
                    i,
                    waveletFunc(i)
                )
            )
            i += step
        }
        viewState.drawFunc(Function(coordinate))
    }

    fun createWaveletTransform() {
        val matrixCoordinate = Array(image.width) { DoubleArray(image.height) }
        val dTau = 10 * Math.PI / image.width
        val dS = (2 - 0.001) / image.height
        var tau: Double
        var s: Double
        for (x in 0 until image.width) {
            for (y in 0 until image.height) {
                tau = x * dTau
                s = y * dS + 0.001
                matrixCoordinate[x][y] += 1.0 / sqrt(abs(s)) * integral(tau, s)
            }
        }
        viewState.drawWaveletImage(convertToColor(matrixCoordinate))
    }

    private fun simpleFunc(j: Double): Double {
        if (j < 4 * Math.PI)
            return atan(cos(0.5 * j));
        if ((j > 4 * Math.PI) && (j < 8 * Math.PI))
            return atan(cos(0.5 * j)) + 0.5 * sin(5 * j);
        else return sin(5 * j);
        /*
        return cos(Math.E.pow(sin(3 * j * 0.33)))*/
    }

    private fun waveletFunc(t: Double): Double {
        return -1 / sqrt(step) * t * exp(-t.pow(2.0) / 2)
    }

    //-----------------------Other methods----------------------------------------------------------
    private fun integral(tau: Double, s: Double): Double {
        var result = 0.0
        var x = 0.0
        while (x < 10 * Math.PI + 10) {
            result += simpleFunc(x) * waveletFunc((x - tau) / s)
            x += 0.1
        }
        return result
    }

    private fun convertToColor(array: Array<DoubleArray>): Array<DoubleArray> {
        val ratio = maxColor / (HDoubleArray.getMax(array) - HDoubleArray.getMin(array))
        val min = HDoubleArray.getMin(array)
        for (i in 0 until image.width) {
            for (j in 0 until image.height) {
                array[i][j] = (array[i][j] - min) * ratio
            }
        }
        return array
    }
}