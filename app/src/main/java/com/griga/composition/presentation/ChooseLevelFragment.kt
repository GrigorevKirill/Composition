package com.griga.composition.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.griga.composition.R
import com.griga.composition.databinding.FragmentChooseLevelBinding
import com.griga.composition.domain.entities.Level

class ChooseLevelFragment : Fragment() {

    private var _binding: FragmentChooseLevelBinding? = null
    private val binding: FragmentChooseLevelBinding
    get() = _binding ?: throw RuntimeException("FragmentChooseLevelBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       _binding = FragmentChooseLevelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chooseGameMode()
    }

    private fun chooseGameMode() {
        binding.btTestLevel.setOnClickListener {
            launchGameFragment(Level.TEST)
        }
        binding.btEasyLevel.setOnClickListener {
            launchGameFragment(Level.EASY)
        }
        binding.btNormalLevel.setOnClickListener {
            launchGameFragment(Level.NORMAL)
        }
        binding.btHardLevel.setOnClickListener {
            launchGameFragment(Level.HARD)
        }
    }

    private fun launchGameFragment(level: Level) {
        val gameFragment = GameFragment.getNewInstance(level)
        requireActivity().supportFragmentManager
            .beginTransaction()
            .addToBackStack(NAME)
            .replace(R.id.main_container, gameFragment)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val NAME = "ChooseLevelFragment"
        fun getNewInstance(): ChooseLevelFragment {
            return ChooseLevelFragment()
        }
    }
}