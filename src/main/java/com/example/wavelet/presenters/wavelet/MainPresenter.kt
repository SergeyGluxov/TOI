package com.example.wavelet.presenters.wavelet


import android.provider.Contacts
import android.util.Log
import com.example.wavelet.helpers.HDoubleArray
import com.example.wavelet.models.Coordinate
import com.example.wavelet.models.Function
import com.example.wavelet.models.Image
import com.example.wavelet.views.IMainView
import kotlinx.coroutines.*
import moxy.InjectViewState
import moxy.MvpPresenter
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.ReentrantLock
import kotlin.math.*


@InjectViewState
class MainPresenter : MvpPresenter<IMainView>() {

    val TAG = MainPresenter::class.java.simpleName

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
        createWaveletTransformBefore()
        createWaveletTransformBefore()
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

    private var job: Job = Job()
    private var scope = CoroutineScope(newFixedThreadPoolContext(4, "synchronizationPool"))

    fun createWaveletTransformBefore() {
        scope.launch {
            val deferredList = listOf(
                async { job1() },
                async { job2() },
                async { job3() },
                async { job4() }
            )
            deferredList.awaitAll()
        }
    }

    fun createWaveletTransform() {
        viewState.showProgressBar()
        val start = System.nanoTime()
        scope.launch {
            val deferredList = listOf(
                async { job1() },
                async { job2() },
                async { job3() },
                async { job4() }
            )
            deferredList.awaitAll()
            scope.launch(Dispatchers.Main) {
                val finish = System.nanoTime()
                val timeConsumedMillis = finish - start
                val convert: Long =
                    TimeUnit.SECONDS.convert(timeConsumedMillis, TimeUnit.NANOSECONDS)
                viewState.showTimeSuccess(convert.toString())
                viewState.hideProgressBar()
                viewState.drawWaveletImage(convertToColor(matrixCoordinate))
            }
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
        Log.d(TAG, "job1")
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
        Log.d(TAG, "job2")
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
            Log.d(TAG, "job3")
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
        Log.d(TAG, "job4")
        viewState.setProgressBar(100)
    }

    //---------------------------------------------------------------------------------------------
    private val sharedCounterLock = ReentrantLock()

    fun updateCounterIfNecessary(w: Int, h: Int) {
        if (w == 1) {
            try {
                sharedCounterLock.lock()
                for (x in 0 until image.width / 2) {
                    for (y in 0 until image.height / 2) {
                        tau = x * dTau
                        s = y * dS + 0.001
                        matrixCoordinate[x][y] += 1.0 / sqrt(abs(s)) * integral(tau, s)
                    }
                }
                Log.d("JOB1", "step")
                viewState.setProgressBar(25)
            } finally {
                sharedCounterLock.unlock()
            }
        } else if (w == 2) {
            try {
                sharedCounterLock.lock()
                for (x in 200 until image.width) {
                    for (y in 200 until image.height) {
                        tau = x * dTau
                        s = y * dS + 0.001
                        matrixCoordinate[x][y] += 1.0 / sqrt(abs(s)) * integral(tau, s)
                    }
                }
                Log.d("JOB2", "step")
                viewState.setProgressBar(50)
            } finally {
                sharedCounterLock.unlock()
            }

        } else if (w == 3) {
            try {
                sharedCounterLock.lock()
                for (x in 0 until image.width / 2) {
                    for (y in 200 until image.height) {
                        tau = x * dTau
                        s = y * dS + 0.001
                        matrixCoordinate[x][y] += 1.0 / sqrt(abs(s)) * integral(tau, s)
                    }
                }
                Log.d("JOB3", "step")
                viewState.setProgressBar(75)
            } finally {
                sharedCounterLock.unlock()
            }

        } else if (w == 4) {
            try {
                sharedCounterLock.lock()
                for (x in 200 until image.width) {
                    for (y in 0 until image.height / 2) {
                        tau = x * dTau
                        s = y * dS + 0.001
                        matrixCoordinate[x][y] += 1.0 / sqrt(abs(s)) * integral(tau, s)
                    }
                }
                viewState.setProgressBar(100)
            } finally {
                sharedCounterLock.unlock()
            }
        }
    }
}