package com.example.wavelet.presenters.sampling

import android.os.Bundle
import com.example.wavelet.R
import com.example.wavelet.views.ISamplingView
import moxy.MvpAppCompatActivity

class SamplingActivity : MvpAppCompatActivity(), ISamplingView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sampling)
    }
}
