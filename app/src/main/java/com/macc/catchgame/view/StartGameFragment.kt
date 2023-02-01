package com.macc.catchgame.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.macc.catchgame.R
import com.macc.catchgame.control.GameUserAdapter
import com.macc.catchgame.control.StartGameUserAdapter
import com.macc.catchgame.databinding.FragmentStartGameBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class StartGameFragment : Fragment() {

    private var _binding: FragmentStartGameBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val itemsList = ArrayList<String>()
    private lateinit var userAdapter: StartGameUserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStartGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonStartGame.setOnClickListener {
            findNavController().navigate(R.id.action_StartGameFragment_to_GameFragment)
        }

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewStartGame)
        userAdapter = StartGameUserAdapter(itemsList)
        val layoutManager = LinearLayoutManager(requireContext().applicationContext)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = userAdapter
        prepareItems()
    }

    private fun prepareItems() {
        itemsList.add("Alan Turing")
        itemsList.add("Konrad Zuse")
        itemsList.add("John Backus")
        itemsList.add("Ada Lovelace")
        itemsList.add("Edsger Dijkstra")
        userAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}