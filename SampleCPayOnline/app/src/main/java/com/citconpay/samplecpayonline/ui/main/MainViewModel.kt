package com.citconpay.samplecpayonline.ui.main

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.citconpay.samplecpayonline.databinding.MainFragmentBinding
import sdk.CPayLaunchType
import sdk.CPayMode
import sdk.CPaySDK
import sdk.interfaces.InquireResponse
import sdk.interfaces.OrderResponse
import sdk.models.CPayInquireResult
import sdk.models.CPayOrder
import sdk.models.CPayOrderResult

class MainViewModel : ViewModel() {
    val AUTH_TOKEN = "XYIL2W9BCQSTNN1CXUQ6WEH9JQYZ3VLM"
    val ENV_MODE = CPayMode.UAT
    lateinit var order: CPayOrder

    fun inquireOrder( listerner: InquireResponse<CPayInquireResult>) {
        CPaySDK.getInstance().inquireOrderByRef(order.mReferenceId, order.currency, order.vendor,
            order.isAccelerateCNPay, listerner)
    }

    fun requestOrder(activity: Activity, _binding: MainFragmentBinding, listener: OrderResponse<CPayOrderResult>) {
        CPaySDK.setMode(ENV_MODE)
        CPaySDK.setToken(AUTH_TOKEN)

        when (_binding.vendorSpinner.selectedItem.toString()) {
            "kcp" -> {
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
            "sbps" -> {
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