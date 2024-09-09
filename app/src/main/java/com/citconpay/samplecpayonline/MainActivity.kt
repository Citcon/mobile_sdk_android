package com.citconpay.samplecpayonline

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.citconpay.samplecpayonline.ui.main.MainFragment
import sdk.CPaySDK

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }

        CPaySDK.initInstance(this, null)
    }
}