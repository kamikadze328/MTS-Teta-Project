package com.kamikadze328.mtstetaproject.presentation.profile

import android.os.Bundle
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.kamikadze328.mtstetaproject.R
import com.kamikadze328.mtstetaproject.adapter.LinearHorizontalItemDecorator
import com.kamikadze328.mtstetaproject.adapter.genre.GenreAdapter
import com.kamikadze328.mtstetaproject.adapter.settings.SettingsAdapter
import com.kamikadze328.mtstetaproject.data.dto.User
import com.kamikadze328.mtstetaproject.data.util.UIState
import com.kamikadze328.mtstetaproject.data.util.phone.PhoneTextWatcher
import com.kamikadze328.mtstetaproject.data.util.phone.PhoneTextWatcherImpl
import com.kamikadze328.mtstetaproject.data.util.phone.formatPhoneNumber
import com.kamikadze328.mtstetaproject.databinding.FragmentProfileBinding
import com.kamikadze328.mtstetaproject.presentation.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by activityViewModels()

    private val defaultTextWatcher: PhoneTextWatcher by lazy {
        PhoneTextWatcher { applyChangedUserToViewModel() }
    }

    private val phoneTextWatcher: PhoneTextWatcherImpl by lazy {
        object : PhoneTextWatcherImpl(binding.profileTextInputPhone) {
            override fun applyChangedUserToViewModel() =
                this@ProfileFragment.applyChangedUserToViewModel()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ProfileFragment().apply {
                arguments = Bundle().apply {}
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.init()
        if (savedInstanceState == null) {
            arguments?.let {}
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        setupRecyclerAdapters()

        binding.profileLogoutButton.setOnClickListener {
            viewModel.logout()
        }

        setOnChangeListeners()

        viewModel.userState.observe(viewLifecycleOwner) {
            when (it) {
                is UIState.LoadingState -> {
                    updateUserInfoHeaderUI(viewModel.loadUserLoading())
                    setEditTextEnable(false)
                }
                is UIState.ErrorState -> {
                    updateUserInfoHeaderUI(viewModel.loadUserError())
                    setEditTextEnable(false)
                }
                is UIState.DataState -> {
                    updateUserInfoUI(it.data)
                    setEditTextEnable(true)
                }
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
                uid = "",
            )
        )
    }

    private fun setEditTextEnable(newState: Boolean) {
        binding.profileTextInputName.isEnabled = newState
        binding.profileTextInputPassword.isEnabled = newState
        binding.profileTextInputEmail.isEnabled = newState
        binding.profileTextInputPhone.isEnabled = newState
    }

    private fun updateUserInfoUI(user: User) {
        updateUserInfoHeaderUI(user)
        updateUserInfoBodyUI(user)
    }

    private fun updateUserInfoHeaderUI(user: User) {
        binding.profileName.text = user.name
        binding.profileEmail.text = user.email
    }

    private fun updateUserInfoBodyUI(user: User) {
        removeOnChangeListeners()

        binding.profileTextInputName.setText(user.name)
        binding.profileTextInputPassword.setText(user.password)
        binding.profileTextInputEmail.setText(user.email)
        binding.profileTextInputPhone.setText(user.phone)
        formatPhoneNumber(binding.profileTextInputPhone.text, binding.profileTextInputPhone)

        setOnChangeListeners()
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

        viewModel.favouriteGenresState.observe(viewLifecycleOwner) {
            when (it) {
                is UIState.LoadingState -> adapter.submitList(viewModel.loadGenreLoading())
                is UIState.ErrorState -> adapter.submitList(viewModel.loadGenreError())
                is UIState.DataState -> adapter.submitList(it.data)
            }
        }
        binding.profileFavouriteMoviesRecycler.adapter = adapter

        val offset = resources.getDimension(R.dimen.profile_movies_offset).toInt()
        val firstLastOffset = resources.getDimension(R.dimen.profile_padding_horizontal).toInt()

        val itemDecorator = LinearHorizontalItemDecorator(offset, firstLastOffset, firstLastOffset)
        binding.profileFavouriteMoviesRecycler.addItemDecoration(itemDecorator)
    }

    private fun onSettingsItemClick(name: String) {
        Toast.makeText(context, name, Toast.LENGTH_SHORT).show()
    }

    private fun onClickListenerGenre(genreId: Long) {
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