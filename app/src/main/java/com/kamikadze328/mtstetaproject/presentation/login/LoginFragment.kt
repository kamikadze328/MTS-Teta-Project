package com.kamikadze328.mtstetaproject.presentation.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.kamikadze328.mtstetaproject.R
import com.kamikadze328.mtstetaproject.data.util.network.auth.FirebaseAuthState
import com.kamikadze328.mtstetaproject.data.util.phone.PhoneTextWatcherImpl
import com.kamikadze328.mtstetaproject.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SharedLoginViewModel by activityViewModels()

    private val phoneTextWatcher: PhoneTextWatcherImpl by lazy {
        object : PhoneTextWatcherImpl(binding.loginPhone) {
            override fun applyChangedUserToViewModel() =
                this@LoginFragment.applyChangedUserToViewModel()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("kek", "onCreate login")
        viewModel.init(requireActivity())
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("kek", "onCreateView login")
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginPhone.apply {
            addTextChangedListener(phoneTextWatcher)
            setText(viewModel.getPhoneNumber())
        }

        binding.loginSubmit.setOnClickListener {
            viewModel.sendVerificationCode()
        }

        viewModel.state.observe(viewLifecycleOwner, {
            Log.d("kek", "$it")
            when (it) {
                is FirebaseAuthState.WaitUserPhoneNumber -> {
                    when {
                        it.isPhoneNumberNotValid -> setStatus(getString(R.string.login_wrong_phone))
                        else -> setStatus(getString(R.string.login_input_phone))
                    }
                }
                is FirebaseAuthState.CodeSendingToUserInvalid -> {
                    when {
                        it.isInvalidRequest -> setStatus(getString(R.string.login_couldnt_send_code))
                        it.isSmsQuotaHasBeenExceeded -> setStatus(getString(R.string.login_couldnt_send_code_sms_quota))
                        else -> setStatus(getString(R.string.login_couldnt_send_code))
                    }
                }
                is FirebaseAuthState.PhoneValidSendingCodeToUser -> {
                    setStatus(getString(R.string.login_code_sending))
                }
                is FirebaseAuthState.CodeWasSentToUser -> {
                    setStatus(getString(R.string.login_code_sent))
                    toWaitCodeFragment()
                }
                is FirebaseAuthState.AuthWasSuccessful -> {
                    setStatus(getString(R.string.login_sign_in_process))
                    toProfileFragment()
                }
                else -> {
                    setStatus(getString(R.string.login_to_forward))
                    toWaitCodeFragment()
                }
            }
        })
    }

    private fun toWaitCodeFragment() {
        findNavController().navigate(R.id.action_to_wait_code)
    }

    private fun toProfileFragment() {
        findNavController().navigate(R.id.action_to_profile)
    }

    private fun setStatus(status: String) {
        binding.loginStatus.text = status
    }

    fun applyChangedUserToViewModel() {
        viewModel.setPhoneNumber(binding.loginPhone.text.toString())
    }

}