package com.example.wavelet.presenters.scale

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.wavelet.R
import com.example.wavelet.models.Function
import com.example.wavelet.views.IScaleView
import kotlinx.android.synthetic.main.activity_scale.*
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter

class ScaleActivity : MvpAppCompatActivity(), IScaleView {

    val TAG = ScaleActivity::class.java.simpleName

    @InjectPresenter
    lateinit var scalePresenter: ScalePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scale)
        nextLab.setOnClickListener {
            startActivity(Intent(this, ScaleActivity::class.java))
        }
    }
}
