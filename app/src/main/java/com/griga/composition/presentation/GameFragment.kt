package com.griga.composition.presentation

import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.view.KeyCharacterMap
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.griga.composition.R
import com.griga.composition.databinding.FragmentGameBinding
import com.griga.composition.databinding.FragmentGameFinishedBinding
import com.griga.composition.domain.entities.GameResult
import com.griga.composition.domain.entities.GameSettings
import com.griga.composition.domain.entities.Level
import com.griga.composition.domain.utils.parcelable
import com.griga.composition.domain.utils.serializable
import java.io.Serializable


class GameFragment : Fragment() {

    private val viewModelFactory by lazy {
        GameViewModelFactory(requireActivity().application, level)
    }
    private val gameViewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory
        )[GameViewModel::class.java]
    }

    private lateinit var level: Level
    private var _binding: FragmentGameBinding? = null
    private val binding: FragmentGameBinding
        get() = _binding ?: throw RuntimeException("FragmentGameBinding == null")

    private val tvOptions by lazy {
        mutableListOf<TextView>().apply {
            add(binding.tvOption1)
            add(binding.tvOption2)
            add(binding.tvOption3)
            add(binding.tvOption4)
            add(binding.tvOption5)
            add(binding.tvOption6)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvOption1.setOnClickListener {
            val gameResult = GameResult(
                true,
                1,
                2,
                GameSettings(
                    1,
                    1,
                    1,
                    1
                )
            )
            launchGameFinishedFragment(gameResult)
        }
        observeLiveData()
        setTvOptionsOnClickListeners()
    }

    private fun setTvOptionsOnClickListeners() {
        for (tvOption in tvOptions) {
            tvOption.setOnClickListener {
                gameViewModel.chooseAnswer(tvOption.text.toString().toInt())
            }
        }
    }

    private fun observeLiveData() {
        with(gameViewModel) {
            formattedTime.observe(viewLifecycleOwner) {
                binding.tvTimer.text = it
            }
            question.observe(viewLifecycleOwner) {
                binding.tvSum.text = it.sum.toString()
                binding.tvLeftNumber.text = it.visibleNumber.toString()
                for (i in 0 until tvOptions.size) {
                    tvOptions[i].text = it.options[i].toString()
                }
            }
            percentOfRightAnswers.observe(viewLifecycleOwner) {
                binding.progressBar.setProgress(it, true)
            }
            enoughCount.observe(viewLifecycleOwner) {
                binding.tvAnswersProgress.setTextColor(getRightColor(it))
            }
            progressAnswers.observe(viewLifecycleOwner) {
                binding.tvAnswersProgress.text = it
            }
            enoughPercent.observe(viewLifecycleOwner) {
                val color = getRightColor(it)
                binding.progressBar.progressTintList = ColorStateList.valueOf(color)
            }
            minPercent.observe(viewLifecycleOwner) {
                binding.progressBar.secondaryProgress = it
            }
            gameResult.observe(viewLifecycleOwner) {
                launchGameFinishedFragment(it)
            }
        }

    }

    private fun getRightColor(state: Boolean): Int {
        return if (state) {
            requireContext().getColor(R.color.green)
        } else {
            requireContext().getColor(R.color.red)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun parseArgs() {
        requireArguments().parcelable<Level>(KEY_LEVEL)?.let {
            level = it
        }
    }

    private fun launchGameFinishedFragment(gameResult: GameResult) {
        val gameFinishedFragment = GameFinishedFragment.getNewInstance(gameResult)
        requireActivity().supportFragmentManager.popBackStack()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, gameFinishedFragment)
            .addToBackStack(NAME)
            .commit()
    }


    companion object {
        private const val KEY_LEVEL = "level"
        const val NAME = "GameFragment"

        fun getNewInstance(level: Level): GameFragment {
            return GameFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_LEVEL, level)
                }
            }
        }
    }
}