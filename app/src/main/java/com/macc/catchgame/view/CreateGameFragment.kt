package com.macc.catchgame.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.macc.catchgame.R
import com.macc.catchgame.databinding.FragmentCreateGameBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class CreateGameFragment : Fragment() {

    private var _binding: FragmentCreateGameBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCreateGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonStartGame.setOnClickListener {
            findNavController().navigate(R.id.action_CreateGameFragment_to_StartGameFragment)
        }

        var timePicker = view.findViewById<TimePicker>(R.id.timePicker)
        timePicker.setIs24HourView(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}