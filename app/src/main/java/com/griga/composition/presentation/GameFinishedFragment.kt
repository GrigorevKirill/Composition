package com.griga.composition.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.griga.composition.R
import com.griga.composition.databinding.FragmentChooseLevelBinding
import com.griga.composition.databinding.FragmentGameFinishedBinding
import com.griga.composition.domain.entities.GameResult
import com.griga.composition.domain.utils.parcelable
import com.griga.composition.domain.utils.serializable
import javax.security.auth.callback.Callback

class GameFinishedFragment : Fragment() {

    private lateinit var gameResult: GameResult
    private var _binding: FragmentGameFinishedBinding? = null
    private val binding: FragmentGameFinishedBinding
        get() = _binding ?: throw RuntimeException("FragmentGameFinishedBinding == null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    retryGame()
                }
            })
        binding.buttonRetry.setOnClickListener {
            retryGame()
        }
        setResults()
    }

    private fun setResults() {
        binding.emojiResult.setImageResource(setIcon(gameResult.winner))
        binding.tvRequiredAnswers.text = String.format(
            getString(R.string.required_score),
            gameResult.gameSettings.minCountOfRightAnswers
        )
        binding.tvScoreAnswers.text = String.format(
            getString(R.string.score_answers),
            gameResult.countOfRightAnswers
        )
        binding.tvRequiredPercentage.text = String.format(
            getString(R.string.required_percentage),
            gameResult.gameSettings.minPercentOfRightAnswers
        )
        binding.tvScorePercentage.text = String.format(
            getString(R.string.score_percentage),
            getPercentage()
        )
    }

    private fun setIcon(winner: Boolean): Int {
        return if (winner) {
            R.drawable.ic_smile
        } else {
            R.drawable.ic_sad
        }
    }

    private fun getPercentage() = with(gameResult) {
        if (countOfQuestions == 0) {
            0
        } else {
            ((countOfRightAnswers / countOfQuestions.toDouble()) * 100).toInt()
        }
    }

    private fun parseArgs() {
        requireArguments().parcelable<GameResult>(KEY_RESULT)?.let {
            gameResult = it
        }
    }

    private fun retryGame() {
        requireActivity().supportFragmentManager.popBackStack(GameFragment.NAME, 1)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val KEY_RESULT = "result"

        fun getNewInstance(gameResult: GameResult): GameFinishedFragment {
            return GameFinishedFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_RESULT, gameResult)
                }
            }
        }
    }
}