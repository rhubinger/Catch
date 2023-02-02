package com.macc.catchgame.view

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.macc.catchgame.R
import com.macc.catchgame.control.GameUserAdapter
import com.macc.catchgame.databinding.FragmentGameBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class GameFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentGameBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val itemsList = ArrayList<String>()
    private lateinit var userAdapter: GameUserAdapter
    private lateinit var mainActivity: MenuActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGameBinding.inflate(inflater, container, false)

        val mapFragment = SupportMapFragment.newInstance()
        parentFragmentManager
            .beginTransaction().add(R.id.map_frame, mapFragment).commit()
        mapFragment.getMapAsync(this)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonMoveOnToResults.setOnClickListener {
            findNavController().navigate(R.id.action_GameFragment_to_ResultFragment)
        }

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewGame)
        userAdapter = GameUserAdapter(itemsList)
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

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        mainActivity = activity as MenuActivity
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.addMarker(
            MarkerOptions()
                .position(LatLng(mainActivity.location.latitude, mainActivity.location.longitude))
                .title("MyPosition")
        )

        googleMap.addMarker(
            MarkerOptions()
                .position(LatLng(49.226861, 10.725891))
                .title("Home")
        )
    }
}