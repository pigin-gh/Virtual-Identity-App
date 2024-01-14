package com.experience.virtualidentityapp.ui.profile

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.experience.virtualidentityapp.databinding.FragmentProfileBinding
import com.google.gson.Gson

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        fillTheFields()
        init()

        return binding.root
    }

    private fun init() {

        binding.saveBt.setOnClickListener {
            val userData = UserData(
                userName = binding.usernameEt.text.toString(),
                country = binding.countryEt.text.toString(),
                dateOfBirth = binding.dateEt.text.toString(),
                phoneNumber = binding.phoneEt.text.toString(),
                passportNo = binding.passportEt.text.toString(),
                pesel = binding.peselEt.text.toString()
            )

            val gson = Gson()
            val json = gson.toJson(userData)
            var prefs: SharedPreferences =
                requireContext().getSharedPreferences("PREFS", MODE_PRIVATE)
            prefs.edit().putString(USER_DATA_PREFS_KEY, json).apply()

            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fillTheFields() {
        var prefs: SharedPreferences = requireContext().getSharedPreferences("PREFS", MODE_PRIVATE)

        val json: String = prefs.getString(USER_DATA_PREFS_KEY, "")!!

        if (json.isNotEmpty()) {
            val gson = Gson()
            val userData: UserData = gson.fromJson(json, UserData::class.java)

            binding.apply {
                usernameEt.setText(userData.userName)
                countryEt.setText(userData.country)
                dateEt.setText(userData.dateOfBirth)
                phoneEt.setText(userData.phoneNumber)
                passportEt.setText(userData.passportNo)
                peselEt.setText(userData.pesel)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val USER_DATA_PREFS_KEY = "USER_DATA_PREFS_KEY"
    }
}