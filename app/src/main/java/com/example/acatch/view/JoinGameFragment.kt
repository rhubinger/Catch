package com.example.acatch.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.acatch.R
import com.example.acatch.databinding.FragmentJoinGameBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class JoinGameFragment : Fragment() {

    private var _binding: FragmentJoinGameBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentJoinGameBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonJoinGame.setOnClickListener {
            findNavController().navigate(R.id.action_JoinGameFragment_to_AwaitStartFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}