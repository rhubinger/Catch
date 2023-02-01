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
import com.macc.catchgame.control.CatchAdapter
import com.macc.catchgame.control.StartGameUserAdapter
import com.macc.catchgame.databinding.FragmentResultBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    private val itemsList = ArrayList<String>()
    private lateinit var catchAdapter: CatchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFinish.setOnClickListener {
            findNavController().navigate(R.id.action_ResultFragment_to_MainMenuFragment)
        }

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewResult)
        catchAdapter = CatchAdapter(itemsList)
        val layoutManager = LinearLayoutManager(requireContext().applicationContext)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = catchAdapter
        prepareItems()
    }

    private fun prepareItems() {
        itemsList.add("Konrad Zuse caught Alan Turing")
        itemsList.add("Alan Turing caught Konrad Zuse")
        itemsList.add("Konrad Zuse caught Edsger Dijkstra")
        itemsList.add("Konrad Zuse caught Ada Lovelace")
        itemsList.add("Ada Lovelace caught John Backus")
        catchAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}