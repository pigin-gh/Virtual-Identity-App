package com.experience.virtualidentityapp.ui.home

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.experience.virtualidentityapp.R
import com.experience.virtualidentityapp.databinding.FragmentHomeBinding
import com.experience.virtualidentityapp.ui.profile.ProfileFragment
import com.experience.virtualidentityapp.ui.profile.UserData
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import io.github.g00fy2.quickie.QRResult
import io.github.g00fy2.quickie.ScanCustomCode
import io.github.g00fy2.quickie.config.ScannerConfig


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private val scanQrCodeLauncher = registerForActivityResult(ScanCustomCode()) { result ->

        try {
            if (result is QRResult.QRSuccess) {
                val gson = Gson()
                val userData: UserData = gson.fromJson(result.content.rawValue, UserData::class.java)

                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(getString(R.string.user_data))

                    .setMessage(
                        "Name and Second name: " + userData.userName +
                                "\nCountry: " + userData.country +
                                "\nDate of Birth: " + userData.dateOfBirth +
                                "\nPhone Number: " + userData.phoneNumber +
                                "\nPassport No: " + userData.passportNo +
                                "\nPESEL: " + userData.pesel)

                    .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                        dialog.cancel()
                    }
                    .show()

            } else {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
            }

        } catch (e: Exception) {
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
            Log.e("TAG", result.toString())
            Log.e("TAG", e.toString())
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        init()

        return binding.root
    }

    private fun init() {
        binding.extendedFab.setOnClickListener {
            scanQrCodeLauncher.launch(
                ScannerConfig.build {
                    setShowCloseButton(true)
                }
            )
        }
    }

    override fun onResume() {
        super.onResume()

        var prefs: SharedPreferences = requireContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE)

        val json: String = prefs.getString(ProfileFragment.USER_DATA_PREFS_KEY, "")!!

        if (json.isEmpty()) {
            binding.mainTV.text = getString(R.string.set_user_data)
        } else {
            val barcodeEncoder = BarcodeEncoder()
            val bitmap: Bitmap = barcodeEncoder.encodeBitmap(json, BarcodeFormat.QR_CODE, 270, 270)
            binding.idIVQrcode.setImageBitmap(bitmap)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}