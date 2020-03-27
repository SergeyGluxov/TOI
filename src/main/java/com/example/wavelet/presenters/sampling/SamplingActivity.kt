package com.example.wavelet.presenters.sampling

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import com.example.wavelet.R
import com.example.wavelet.models.Function
import com.example.wavelet.presenters.scale.ScaleActivity
import com.example.wavelet.presenters.wavelet.MainActivity
import com.example.wavelet.views.ISamplingView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.PointsGraphSeries
import kotlinx.android.synthetic.main.activity_sampling.*
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter

class SamplingActivity : MvpAppCompatActivity(), ISamplingView {

    val TAG = SamplingActivity::class.java.simpleName

    @InjectPresenter
    lateinit var samplingPresenter: SamplingPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sampling)
        nextLab.setOnClickListener {
            startActivity(Intent(this, ScaleActivity::class.java))
        }
        sbOversampling.setOnSeekBarChangeListener(onSeekBarChangeListener)
    }

    override fun drawFunc(function: Function, overSamplingFunc: Function?) {
        Log.d("drawFunc", function.coordinateList.size.toString())

        graphSampling.series.clear()

        graphSampling.viewport.setMinX(-15.0)
        graphSampling.viewport.setMaxX(15.0)
        graphSampling.viewport.setMinY(-10.0)
        graphSampling.viewport.setMaxY(10.0)
        graphSampling.viewport.isScrollable = true
        graphSampling.viewport.isScrollable = true
        graphSampling.viewport.isScalable = true
        val series = PointsGraphSeries<DataPoint>()
        val series2 = PointsGraphSeries<DataPoint>()
        series2.color = Color.RED
        series.size = 5F
        series2.size = 5F
        for (coordinate in function.coordinateList) {
            series.appendData(DataPoint(coordinate.x, coordinate.y), true, 200)
        }
        graphSampling.addSeries(series)
        for (coordinate in overSamplingFunc!!.coordinateList) {
            series2.appendData(DataPoint(coordinate.x, coordinate.y), true, 1000)
        }
        graphSampling.addSeries(series2)
        graphSampling.addSeries(series)
    }

    //-----------------------------Callbacks--------------------------------------------------------
    var onSeekBarChangeListener = object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            Log.d(TAG, progress.toString())
            samplingPresenter.oversampling(samplingPresenter.createSimpleFunc(), progress)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {

        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {

        }

    }
}
