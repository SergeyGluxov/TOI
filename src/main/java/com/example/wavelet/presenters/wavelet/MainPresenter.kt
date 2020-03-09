package com.example.wavelet.presenters.wavelet


import com.example.wavelet.helpers.HDoubleArray
import com.example.wavelet.models.Coordinate
import com.example.wavelet.models.Function
import com.example.wavelet.models.Image
import com.example.wavelet.views.IMainView
import kotlinx.coroutines.*
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

    var image = Image(400, 400)
    val matrixCoordinate = Array(image.width) { DoubleArray(image.height) }
    val dTau = 10 * Math.PI / image.width
    val dS = (2 - 0.001) / image.height
    var tau: Double = 0.0

    var s: Double = 0.0

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
        viewState.showProgressBar()


        GlobalScope.launch(Dispatchers.Main) {

            for (i in 0 until 4) {
                for(j in 0 until 4){
                    JOB5(j, i)
                }
            }

            viewState.hideProgressBar()
            viewState.drawWaveletImage(convertToColor(matrixCoordinate))
        }
    }

    private suspend fun  JOB5(i:Int, j: Int) = withContext(Dispatchers.Default) {
        for (x in i*100 until image.width/(4-i)) {
            for (y in j*100 until image.height/(4-j)) {
                tau = x * dTau
                s = y * dS + 0.001
                matrixCoordinate[x][y] += 1.0 / sqrt(abs(s)) * integral(tau, s)
            }
        }
        if(i>0){
            viewState.setProgressBar(100/i)
        }
    }

    private fun simpleFunc(j: Double): Double {
        if (j < 4 * Math.PI)
            return atan(cos(0.5 * j));
        if ((j > 4 * Math.PI) && (j < 8 * Math.PI))
            return atan(cos(0.5 * j)) + 0.5 * sin(5 * j);
        else return sin(5 * j);
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

    private suspend fun job1() = withContext(Dispatchers.Default) {
        for (x in 0 until image.width / 2) {
            for (y in 0 until image.height / 2) {
                tau = x * dTau
                s = y * dS + 0.001
                matrixCoordinate[x][y] += 1.0 / sqrt(abs(s)) * integral(tau, s)
            }
        }
        viewState.setProgressBar(25)
    }

    private suspend fun job2() = withContext(Dispatchers.Default) {
        for (x in 200 until image.width) {
            for (y in 200 until image.height) {
                tau = x * dTau
                s = y * dS + 0.001
                matrixCoordinate[x][y] += 1.0 / sqrt(abs(s)) * integral(tau, s)
            }
        }
        viewState.setProgressBar(50)
    }

    private suspend fun job3() =
        withContext(Dispatchers.Default) {
            for (x in 0 until image.width / 2) {
                for (y in 200 until image.height) {
                    tau = x * dTau
                    s = y * dS + 0.001
                    matrixCoordinate[x][y] += 1.0 / sqrt(abs(s)) * integral(tau, s)
                }
            }
            viewState.setProgressBar(75)
        }

    private suspend fun job4() = withContext(Dispatchers.Default) {
        for (x in 200 until image.width) {
            for (y in 0 until image.height / 2) {
                tau = x * dTau
                s = y * dS + 0.001
                matrixCoordinate[x][y] += 1.0 / sqrt(abs(s)) * integral(tau, s)
            }
        }
        viewState.setProgressBar(100)
    }

}