package com.griga.composition.presentation

import android.os.Build
import android.os.Bundle
import android.view.KeyCharacterMap
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    private lateinit var level: Level
    private var _binding: FragmentGameBinding? = null
    private val binding: FragmentGameBinding
        get() = _binding ?: throw RuntimeException("FragmentGameBinding == null")


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