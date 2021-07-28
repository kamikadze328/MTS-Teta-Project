package com.kamikadze328.mtstetaproject.presentation.profile

import android.os.Bundle
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kamikadze328.mtstetaproject.R
import com.kamikadze328.mtstetaproject.adapter.LinearHorizontalItemDecorator
import com.kamikadze328.mtstetaproject.adapter.genre.GenreAdapter
import com.kamikadze328.mtstetaproject.adapter.settings.SettingsAdapter
import com.kamikadze328.mtstetaproject.data.dto.User
import com.kamikadze328.mtstetaproject.databinding.FragmentProfileBinding
import com.kamikadze328.mtstetaproject.presentation.main.MainActivity
import com.kamikadze328.mtstetaproject.presentation.profile.textwatcher.ProfilePhoneTextWatcher
import com.kamikadze328.mtstetaproject.presentation.profile.textwatcher.ProfileTextWatcher
import com.kamikadze328.mtstetaproject.presentation.profile.textwatcher.formatPhoneNumber
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()

    private val defaultTextWatcher: ProfileTextWatcher by lazy {
        ProfileTextWatcher {
            applyChangedUserToViewModel()
        }
    }

    private val phoneTextWatcher: ProfilePhoneTextWatcher by lazy {
        object : ProfilePhoneTextWatcher(binding.profileTextInputPhone) {
            override fun applyChangedUserToViewModel() =
                this@ProfileFragment.applyChangedUserToViewModel()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(accountId: Int) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putInt(ProfileViewModel.PROFILE_ID_ARG, accountId)
                }
            }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("kek", "onCreate profile")
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            arguments?.let {
                viewModel.setAccountId(it.getInt(ProfileViewModel.PROFILE_ID_ARG))
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("kek", "onCreateView profile")
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        setupRecyclerAdapters()

        setOnChangeListeners()
        viewModel.wasDataChanged.observe(viewLifecycleOwner, ::updateSubmitButtonUI)

        if (savedInstanceState == null) {
            viewModel.user.observe(viewLifecycleOwner, ::updateUserInfoUI)
        } else {
            viewModel.changedUser.value?.let {
                updateUserInfoUI(it)
            }
        }

        return binding.root
    }

    private fun setupRecyclerAdapters() {
        setupRecyclerAdapterSetting()
        setupRecycleAdapterFavouriteGenres()
    }

    private fun applyChangedUserToViewModel() {
        viewModel.updateChangedUser(
            User(
                name = binding.profileTextInputName.text.toString(),
                password = binding.profileTextInputPassword.text.toString(),
                email = binding.profileTextInputEmail.text.toString(),
                phone = binding.profileTextInputPhone.text.toString(),
                id = viewModel.getAccountId()
            )
        )
    }


    private fun updateUserInfoUI(user: User) {
        removeOnChangeListeners()
        binding.profileTextInputName.setText(user.name)
        binding.profileTextInputPassword.setText(user.password)
        binding.profileTextInputEmail.setText(user.email)
        binding.profileTextInputPhone.setText(user.phone)
        formatPhoneNumber(binding.profileTextInputPhone.text, binding.profileTextInputPhone)

        binding.profileName.text = user.name
        binding.profileEmail.text = user.email

        setOnChangeListeners()
    }


    private fun updateSubmitButtonUI(wasDataChanged: Boolean) {
        binding.profileSavePersonalInfoButton.visibility =
            if (wasDataChanged) View.VISIBLE else View.INVISIBLE
    }


    private fun setupRecyclerAdapterSetting() {
        binding.profileSettingsRecycler.adapter = SettingsAdapter(
            resources.getStringArray(R.array.settings_headers),
            ::onSettingsItemClick
        )
        binding.profileAppInfoRecycler.adapter = SettingsAdapter(
            resources.getStringArray(R.array.app_info_headers),
            ::onSettingsItemClick
        )
    }

    private fun setupRecycleAdapterFavouriteGenres() {
        val adapter = GenreAdapter(::onClickListenerGenre)

        listOf(viewModel.getLoadingGenre(resources)).let {
            adapter.submitList(it)
        }

        viewModel.favouriteGenres.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })
        binding.profileFavouriteMoviesRecycler.adapter = adapter

        val offset = resources.getDimension(R.dimen.profile_movies_offset).toInt()
        val firstLastOffset = resources.getDimension(R.dimen.profile_padding_horizontal).toInt()

        val itemDecorator = LinearHorizontalItemDecorator(offset, firstLastOffset, firstLastOffset)
        binding.profileFavouriteMoviesRecycler.addItemDecoration(itemDecorator)
    }

    private fun onSettingsItemClick(name: String) {
        Toast.makeText(context, name, Toast.LENGTH_SHORT).show()
    }

    private fun onClickListenerGenre(genreId: Int) {
        (activity as MainActivity).onGenreClicked(genreId)
    }

    private fun setOnChangeListeners() {
        setOnChangeTextListenerName()
        setOnChangeTextListenerPassword()
        setOnChangeTextListenerEmail()
        setOnChangeTextListenerPhone()
    }

    private fun removeOnChangeListeners() {
        removeOnChangeTextListenerName()
        removeOnChangeTextListenerPassword()
        removeOnChangeTextListenerEmail()
        removeOnChangeTextListenerPhone()
    }

    private fun setOnChangeTextListener(
        editText: EditText,
        textWatcher: TextWatcher = defaultTextWatcher
    ) {
        editText.addTextChangedListener(textWatcher)
    }

    private fun removeOnChangeTextListener(
        editText: EditText,
        textWatcher: TextWatcher = defaultTextWatcher
    ) {
        editText.removeTextChangedListener(textWatcher)
    }

    private fun removeOnChangeTextListenerPassword() {
        removeOnChangeTextListener(binding.profileTextInputPassword)
    }

    private fun removeOnChangeTextListenerName() {
        removeOnChangeTextListener(binding.profileTextInputName)
    }

    private fun removeOnChangeTextListenerEmail() {
        removeOnChangeTextListener(binding.profileTextInputEmail)
    }

    private fun removeOnChangeTextListenerPhone() {
        removeOnChangeTextListener(
            binding.profileTextInputPhone,
            phoneTextWatcher
        )
    }

    private fun setOnChangeTextListenerName() {
        setOnChangeTextListener(binding.profileTextInputName)
    }

    private fun setOnChangeTextListenerPassword() {
        setOnChangeTextListener(binding.profileTextInputPassword)
    }

    private fun setOnChangeTextListenerEmail() {
        setOnChangeTextListener(binding.profileTextInputEmail)
    }

    private fun setOnChangeTextListenerPhone() {
        setOnChangeTextListener(
            binding.profileTextInputPhone,
            phoneTextWatcher
        )
    }
}