package com.kamikadze328.mtstetaproject.presentation.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.kamikadze328.mtstetaproject.R
import com.kamikadze328.mtstetaproject.data.util.network.auth.FirebaseAuthState
import com.kamikadze328.mtstetaproject.databinding.FragmentWaitCodeBinding

class WaitCodeFragment : Fragment() {
    private var _binding: FragmentWaitCodeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SharedLoginViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWaitCodeBinding.inflate(inflater, container, false)

        binding.waitCodeDescription.text =
            getString(R.string.wait_code_description, viewModel.getPhoneNumber())

        binding.waitCodeSubmit.setOnClickListener {
            viewModel.submitCode(binding.waitCodeCode.text.toString())
        }

        binding.waitCodeChooseAnotherNumber.setOnClickListener {
            viewModel.chooseAnotherPhoneNumber()
        }

        viewModel.codeResendTimeout.observe(viewLifecycleOwner, {
            binding.waitCodeResendTimer.text = getString(R.string.wait_code_timer, it)
        })


        viewModel.codeResendTimeoutIsOver.observe(viewLifecycleOwner, {
            if (it) {
                binding.waitCodeResendTimer.apply {
                    setOnClickListener { viewModel.sendVerificationCode(true) }
                    text = getString(R.string.wait_code_send_code_again)
                    isEnabled = true
                    setBackgroundColor(ContextCompat.getColor(context, R.color.purple_500))
                }
            } else {
                binding.waitCodeResendTimer.apply {
                    setOnClickListener(null)
                    isEnabled = false
                    setBackgroundColor(ContextCompat.getColor(context, R.color.grey))
                }
            }

        })

        viewModel.state.observe(viewLifecycleOwner, {
            when (it) {
                is FirebaseAuthState.CodeWasSentToUser -> {
                    if (it.isCodeInvalid) setStatus(getString(R.string.wait_code_code_wrong))
                }
                is FirebaseAuthState.CodeChecking -> {
                    setStatus(getString(R.string.wait_code_code_checking))
                }
                is FirebaseAuthState.CodeChecked -> {
                    setStatus(getString(R.string.wait_code_code_right))
                }
                is FirebaseAuthState.AuthWasSuccessful -> {
                    setStatus(getString(R.string.wait_code_success))
                    toProfileFragment()
                }
                is FirebaseAuthState.WaitUserPhoneNumber -> {
                    toLoginFragment()
                }
                else -> {
                    //throw IllegalStateException()
                    toLoginFragment()
                }
            }
        })

        return binding.root
    }

    private fun setStatus(status: String) {
        binding.waitCodeStatus.text = status
    }

    private fun toLoginFragment() {
        findNavController().navigate(R.id.action_to_login)
    }

    private fun toProfileFragment() {
        findNavController().navigate(R.id.action_to_profile)

    }
}