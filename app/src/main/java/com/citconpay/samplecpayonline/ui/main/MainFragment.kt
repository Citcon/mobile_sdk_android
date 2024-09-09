package com.citconpay.samplecpayonline.ui.main

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.citconpay.samplecpayonline.R
import com.citconpay.samplecpayonline.databinding.MainFragmentBinding
import sdk.CPaySDK

class MainFragment : Fragment() {
    private var _binding: MainFragmentBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        _binding?.requestButton?.setOnClickListener {
            binding.tvResult.text = getString(R.string.requesting)
            viewModel.requestOrder(activity as Activity, _binding!!) { orderResult ->
                binding.tvResult.text = getString(R.string.finished)
                if (orderResult == null) {
                    Toast.makeText(activity,
                        "Error: Get requestOrder null failed",
                        Toast.LENGTH_LONG
                    ).show()
                    return@requestOrder
                }

                if (orderResult.mStatus != "0" && orderResult.mStatus != "initiated"
                    && orderResult.mStatus != "pending"
                    && orderResult.mStatus != "success"
                    && orderResult.mStatus != "authorized"
                    ) {
                    Toast.makeText(activity,
                        "Error: Get requestOrder failed, status: " + orderResult.mStatus + " message: " + orderResult.mMessage,
                        Toast.LENGTH_LONG
                    ).show()
                    return@requestOrder
                }

                Log.d("Return", "requestOrder success")
            }
        }

        binding.inquireButton.setOnClickListener {
            binding.tvResult.text = getString(R.string.inquiring)
            viewModel.inquireOrder {
//                binding.tvResult.text = it?.mNote?:"NULL response, inquire failed"
                binding.tvResult.text =
                "transaction id: " +it.mId + "\n" +
                "status: " + it.mStatus + "\n" +
                "reference: "+ it.mReference + "\n" +
                "note: " + it.mNote;

            }
        }

        CPaySDK.mInquireResult.observe(viewLifecycleOwner) {
//            binding.tvResult.text = it?.mNote?:"NULL response, inquire failed"
            binding.tvResult.text =
                "transaction id: " +it.mId + "\n" +
                        "status: " + it.mStatus + "\n" +
                        "reference: "+ it.mReference + "\n" +
                        "note: " + it.mNote;
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}