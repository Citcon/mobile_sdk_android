package com.citconpay.samplecpayonline.ui.main

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.citconpay.samplecpayonline.databinding.MainFragmentBinding
import org.apache.commons.lang3.RandomStringUtils
import sdk.CPayLaunchType
import sdk.CPayMode
import sdk.CPaySDK
import sdk.interfaces.InquireResponse
import sdk.interfaces.OrderResponse
import sdk.models.CPayInquireResult
import sdk.models.CPayOrder
import sdk.models.CPayOrderResult
import java.util.Locale

class MainViewModel : ViewModel() {
//    val AUTH_TOKEN = "XYIL2W9BCQSTNN1CXUQ6WEH9JQYZ3VLM"
    val AUTH_TOKEN = "C02CTJAFQ3XV5BNHCWKSLLYVTQADM9H0"
    val ENV_MODE = CPayMode.QA
    lateinit var order: CPayOrder

    fun inquireOrder( listerner: InquireResponse<CPayInquireResult>) {
        CPaySDK.getInstance().inquireOrderByRef(order.mReferenceId, order.currency, order.vendor,
            order.isAccelerateCNPay, listerner)
    }

    fun requestOrder(activity: Activity, _binding: MainFragmentBinding, listener: OrderResponse<CPayOrderResult>) {
        CPaySDK.setMode(ENV_MODE)
        CPaySDK.setToken(AUTH_TOKEN)

        _binding.referenceIdEditText.setText(RandomStringUtils.randomAlphanumeric(10))

        when (_binding.vendorSpinner.selectedItem.toString()) {
            "cashapppay" -> {
                order = CPayOrder.Builder()
                    .setLaunchType(CPayLaunchType.OTHERS)
                    .setReferenceId(_binding.referenceIdEditText.text.toString())
                    .setSubject(_binding.subjectEditText.text.toString())
                    .setBody(_binding.bodyEditText.text.toString())
                    .setAmount(_binding.amountEditText.text.toString())
                    .setCurrency(_binding.currencySpinner.selectedItem.toString())
                    .setCountry(Locale.US)
                    .setVendor(_binding.vendorSpinner.selectedItem.toString())
                    .setIpnUrl(_binding.ipnEditText.text.toString())
                    .setCallbackUrl(_binding.callbackEditText.text.toString())
                    .setCallbackFailUrl(_binding.callbackFailEditText.text.toString())
                    .setAllowDuplicate(_binding.duplicateSwitch.isChecked)
                    .enableCNPayAcceleration(_binding.switchCnpay.isChecked)
                    .setDeepLink("citcon://cpay.sdk")
                    .build()
            }
            else -> {
                order = CPayOrder.Builder()
                    .setLaunchType(CPayLaunchType.OTHERS)
                    .setReferenceId(_binding.referenceIdEditText.text.toString())
                    .setSubject(_binding.subjectEditText.text.toString())
                    .setBody(_binding.bodyEditText.text.toString())
                    .setAmount(_binding.amountEditText.text.toString())
                    .setCurrency(_binding.currencySpinner.selectedItem.toString())
                    .setVendor(_binding.vendorSpinner.selectedItem.toString())
                    .setIpnUrl(_binding.ipnEditText.text.toString())
                    .setCallbackUrl(_binding.callbackEditText.text.toString())
                    .setAllowDuplicate(_binding.duplicateSwitch.isChecked)
                    .enableCNPayAcceleration(_binding.switchCnpay.isChecked)
                    .build()
            }

        }
        CPaySDK.getInstance().requestOrder(activity, order, listener)

    }
}