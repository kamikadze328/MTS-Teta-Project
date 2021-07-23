package com.kamikadze328.mtstetaproject.fragment

import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.kamikadze328.mtstetaproject.MainActivity
import com.kamikadze328.mtstetaproject.R
import com.kamikadze328.mtstetaproject.adapter.LinearHorizontalItemDecorator
import com.kamikadze328.mtstetaproject.adapter.movieshort.MovieShortAdapter
import com.kamikadze328.mtstetaproject.adapter.settings.SettingsAdapter
import com.kamikadze328.mtstetaproject.data.dto.User
import com.kamikadze328.mtstetaproject.data.features.movies.MoviesDataSourceImpl
import com.kamikadze328.mtstetaproject.data.features.users.UsersDataSourceImpl
import com.kamikadze328.mtstetaproject.databinding.FragmentProfileBinding
import com.kamikadze328.mtstetaproject.model.MoviesModel
import com.kamikadze328.mtstetaproject.model.UserModel


class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var userModel: UserModel
    private lateinit var moviesModel: MoviesModel

    private lateinit var user: User
    private lateinit var changedUser: User

    private val wasDataChanged: Boolean
        get() {
            return if (_binding == null) false
            else binding.profileTextInputName.text.toString() != user.name ||
                    binding.profileTextInputPassword.text.toString() != user.password ||
                    binding.profileTextInputEmail.text.toString() != user.email ||
                    !PhoneNumberUtils.compare(
                        binding.profileTextInputPhone.text.toString(),
                        user.phone
                    )
        }

    private val defaultTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            checkChangedUser()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("kek", "onCreate profile")
        super.onCreate(savedInstanceState)
        initDataSource()
        user = userModel.getCurrentUser()
        arguments?.let { }
        if (savedInstanceState != null) {
            changedUser = savedInstanceState.getParcelable(CHANGED_USER) ?: user.copy()
        } else {
            changedUser = user.copy()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("kek", "onCreateView profile")
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        initDataSources()
        initProfileData()
        initEditText()
        initRecyclerViewsSetting()
        initRecycleViewFavouriteMovies()
        formatPhoneNumber(binding.profileTextInputPhone.text)
        setOnChangeListeners()

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.d("kek", "onSaveInstanceState profile")

        changedUser.name = binding.profileTextInputName.toString()
        changedUser.password = binding.profileTextInputPassword.toString()
        changedUser.email = binding.profileTextInputEmail.toString()
        changedUser.phone = binding.profileTextInputPhone.toString()
        outState.putParcelable(CHANGED_USER, changedUser)
        super.onSaveInstanceState(outState)
    }

    companion object {
        private const val space = ' '
        private const val minus = '-'
        private const val plus = '+'
        private const val seven = '7'
        private const val CHANGED_USER = "changed_user"

        @JvmStatic
        fun newInstance() =
            ProfileFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    private fun initDataSources() {
        moviesModel = MoviesModel(MoviesDataSourceImpl())
    }

    private fun initProfileData() {
        binding.profileName.text = user.name
        binding.profileEmail.text = user.email
    }

    private fun initEditText() {
        binding.profileTextInputName.setText(changedUser.name)
        binding.profileTextInputPassword.setText(changedUser.password)
        binding.profileTextInputEmail.setText(changedUser.email)
        binding.profileTextInputPhone.setText(changedUser.phone)
    }

    private fun initDataSource() {
        userModel = UserModel(UsersDataSourceImpl())
    }

    private fun initRecyclerViewsSetting() {
        val settingsItem = arrayOf(
            R.string.settings_header_notification_and_sound,
            R.string.settings_header_privacy_policy,
            R.string.settings_header_theme,
            R.string.settings_header_language,
            R.string.settings_header_data_and_memory,
        )

        val appInfoItem = arrayOf(
            R.string.app_info_header_ask_question,
            R.string.app_info_header_notify_about_problem,
        )

        binding.profileSettingsRecycler.adapter =
            SettingsAdapter(settingsItem, ::onSettingsItemClick)
        binding.profileAppInfoRecycler.adapter = SettingsAdapter(appInfoItem, ::onSettingsItemClick)
    }

    private fun initRecycleViewFavouriteMovies() {
        val movies = moviesModel.getFavouriteMoviesByUserId(user.id)
        val adapter = MovieShortAdapter(::onClickListenerMovies)

        adapter.submitList(movies)
        binding.profileFavouriteMoviesRecycler.adapter = adapter

        val offset = resources.getDimension(R.dimen.profile_movies_offset).toInt()
        val firstLastOffset = resources.getDimension(R.dimen.profile_padding_horizontal).toInt()

        val itemDecorator = LinearHorizontalItemDecorator(offset, firstLastOffset, firstLastOffset)
        binding.profileFavouriteMoviesRecycler.addItemDecoration(itemDecorator)
    }

    private fun onSettingsItemClick(nameId: Int) {
        Toast.makeText(context, context?.resources?.getString(nameId), Toast.LENGTH_SHORT).show()
    }

    private fun onClickListenerMovies(id: Int) {
        (activity as MainActivity).onMovieClicked(id)
    }

    private fun checkChangedUser() {
        binding.profileSavePersonalInfoButton.visibility =
            if (wasDataChanged) View.VISIBLE else View.INVISIBLE
    }

    private fun setOnChangeListeners() {
        setOnChangeTextListenerName()
        setOnChangeTextListenerPassword()
        setOnChangeTextListenerEmail()
        setOnChangeTextListenerPhone()
    }


    private fun setOnChangeTextListener(
        editText: EditText,
        textWatcher: TextWatcher = defaultTextWatcher
    ) {
        editText.addTextChangedListener(textWatcher)
    }

    private fun setOnChangeTextListenerPassword() {
        setOnChangeTextListener(binding.profileTextInputPassword)
    }

    private fun setOnChangeTextListenerName() {
        setOnChangeTextListener(binding.profileTextInputName)
    }

    private fun setOnChangeTextListenerEmail() {
        setOnChangeTextListener(binding.profileTextInputEmail)
    }

    private fun setOnChangeTextListenerPhone() {
        val phoneTextWatcher: TextWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s != null && s.isNotEmpty()) {
                    val editText = binding.profileTextInputPhone
                    editText.removeTextChangedListener(this)
                    formatPhoneNumber(s)
                    editText.addTextChangedListener(this)
                }
                checkChangedUser()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
        setOnChangeTextListener(binding.profileTextInputPhone, phoneTextWatcher)
    }

    private fun formatPhoneNumber(s: Editable) {
        val editText = binding.profileTextInputPhone
        var countDeleted = 0
        for (i in s.indices) {
            val currI = i - countDeleted
            if (s.length == currI) break
            if (s[currI] == space || s[currI] == minus || s[currI] == plus || currI == 11) {
                s.delete(currI, currI + 1)
                countDeleted++
            }
        }
        if (s.isNotEmpty()) {
            when (s[0]) {
                seven -> {
                    s.insert(0, "$plus")
                    s.insert(2, "$space")
                }
                plus -> s.insert(1, "$seven$space")
                else -> s.insert(0, "$plus$seven$space")
            }
            if (s.length > 3) {
                if (editText.selectionStart == 3) editText.setSelection(4)
            }
            if (s.length > 6) {
                s.insert(6, space.toString())
                if (editText.selectionStart == 7) editText.setSelection(6)
            }
            if (s.length > 10) {
                s.insert(10, minus.toString())
                if (editText.selectionStart == 11) editText.setSelection(10)
            }
            if (s.length > 13) {
                s.insert(13, minus.toString())
                if (editText.selectionStart == 14) editText.setSelection(13)
            }
        }
    }
}