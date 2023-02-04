package com.macc.catchgame.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.macc.catchgame.R
import com.macc.catchgame.control.GameUserAdapter

class GameActivity : AppCompatActivity(), OnMapReadyCallback, LocationListener {

    private lateinit var buttonMoveOnToResults: Button

    private val itemsList = ArrayList<String>()
    private lateinit var userAdapter: GameUserAdapter

    private lateinit var locationManager: LocationManager
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var location = Location("")
    private val locationPermissionCode = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val mapFragment = SupportMapFragment.newInstance()
        supportFragmentManager.beginTransaction().add(R.id.map_frame, mapFragment).commit()
        mapFragment.getMapAsync(this)

        // TODO refactor to automatic transition when time is over
        buttonMoveOnToResults = findViewById(R.id.buttonMoveOnToResults)
        buttonMoveOnToResults.setOnClickListener {
            var intent = Intent(applicationContext, ResultActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewGame)
        userAdapter = GameUserAdapter(itemsList)
        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = userAdapter
        prepareItems()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLocation()
    }

    private fun prepareItems() {
        itemsList.add("Alan Turing")
        itemsList.add("Konrad Zuse")
        itemsList.add("John Backus")
        itemsList.add("Ada Lovelace")
        itemsList.add("Edsger Dijkstra")
        userAdapter.notifyDataSetChanged()
    }


    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.addMarker(
            MarkerOptions()
                .position(LatLng(this.location.latitude, this.location.longitude))
                .title("MyPosition")
        )

        googleMap.addMarker(
            MarkerOptions()
                .position(LatLng(49.226861, 10.725891))
                .title("Home")
        )
    }

    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
    }

    override fun onLocationChanged(location: Location) {
        this.location = location
        Log.d("MOCK", "Latitude: " + location.latitude + " , Longitude: " + location.longitude)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}