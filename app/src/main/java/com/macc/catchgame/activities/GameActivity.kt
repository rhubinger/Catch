package com.macc.catchgame.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ArrayRes
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import com.macc.catchgame.R
import com.macc.catchgame.adapters.GameUserAdapter
import kotlin.random.Random

class GameActivity : AppCompatActivity(), OnMapReadyCallback{

    private lateinit var gameId: String
    private var timeLeft: Long = -1

    private lateinit var mapFragment: SupportMapFragment
    private lateinit var googleMap: GoogleMap

    private var catcherList = ArrayList<String>()
    private val playerList = ArrayList<String>()
    private lateinit var userAdapter: GameUserAdapter
    private lateinit var recyclerView: RecyclerView

    private lateinit var textViewTimeLeft: TextView

    private lateinit var fusedLocationProvider: FusedLocationProviderClient
    private lateinit var location: LatLng

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        textViewTimeLeft = findViewById(R.id.timeLeft)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val extras = intent.extras
        if (extras != null) {
            gameId = extras.getString("gameId").toString()
        } else {
            Toast.makeText(this, "Failed to get the gameId", Toast.LENGTH_SHORT).show()
        }

        mapFragment = SupportMapFragment.newInstance()
        supportFragmentManager.beginTransaction().add(R.id.map_frame, mapFragment).commit()
        mapFragment.getMapAsync(this)

        db.collection("games").document(gameId).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val players = document.data?.get("players") as ArrayList<String>
                    for(player in players){
                        playerList.add(player)
                    }
                    userAdapter.notifyDataSetChanged()

                    timeLeft = document.data?.get("length") as Long
                    object : CountDownTimer(timeLeft * 1000, 1000) {
                        override fun onTick(millisUntilFinished: Long) {
                            val secsLeft = millisUntilFinished / 1000
                            val secs = String.format("%02d", secsLeft % 60)
                            val mins = String.format("%02d", secsLeft / 60 % 60)
                            val hours = secsLeft / 3600
                            textViewTimeLeft.text = "$hours:$mins:$secs"
                        }
                        override fun onFinish() {
                            val game = hashMapOf(
                                "state" to "finished",
                            )
                            db.collection("games").document(gameId).set(game, SetOptions.merge())
                                .addOnFailureListener {
                                    Toast.makeText(applicationContext, "Failed to end game", Toast.LENGTH_SHORT).show()
                                }
                        }
                    }.start()
                } else {
                    Toast.makeText(this, "Game not found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to get players from db", Toast.LENGTH_SHORT).show()
            }
        recyclerView = findViewById(R.id.recyclerViewGame)
        userAdapter = GameUserAdapter(playerList, gameId)
        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = userAdapter


        db.collection("games").document(gameId).
        addSnapshotListener { snapshot, e ->
            if (e != null) {
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val catchers = snapshot.data?.get("catchers") as ArrayList<String>
                val players = snapshot.data?.get("players") as ArrayList<String>
                if(catchers.size == players.size){
                    val game = hashMapOf(
                        "state" to "finished",
                    )
                    db.collection("games").document(gameId).set(game, SetOptions.merge())
                        .addOnFailureListener {
                            Toast.makeText(applicationContext, "Failed to end game", Toast.LENGTH_SHORT).show()
                        }
                }

                val state = snapshot.data?.get("state").toString()
                if(state == "finished"){
                    var intent = Intent(applicationContext, ResultActivity::class.java)
                    intent.putExtra("gameId", gameId)
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                    finish()
                }
            }
        }

        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(this)
        location = LatLng(0.0,0.0)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap

        db.collection("locations").addSnapshotListener { value, e ->
            if (e != null) {
                return@addSnapshotListener
            }

            db.collection("games").document(gameId).get().addOnSuccessListener {
                    document ->
                if (document != null) {
                    catcherList = document.data?.get("catchers") as ArrayList<String>
                }
            }

            for (doc in value!!) {
                Log.d("MOCK", "name ${doc.id}")
                if(playerList.contains(doc.id)){
                    val location = LatLng(
                        doc.data?.get("latitude") as Double,
                        doc.data?.get("longitude") as Double,
                    )
                    if(doc.id != auth.currentUser?.email.toString()) {
                        if (catcherList.contains(doc.id)) {
                            googleMap.addMarker(
                                MarkerOptions().position(location).title("${doc.id}'s location")
                            )
                                ?.setIcon(
                                    BitmapDescriptorFactory.defaultMarker(
                                        BitmapDescriptorFactory.HUE_RED
                                    )
                                )
                        } else {
                            googleMap.addMarker(
                                MarkerOptions().position(location).title("${doc.id}'s location")
                            )
                                ?.setIcon(
                                    BitmapDescriptorFactory.defaultMarker(
                                        BitmapDescriptorFactory.HUE_AZURE
                                    )
                                )
                        }
                    }
                }
            }
        }

        getCurrentLocation()
    }


    private fun getCurrentLocation(){
        if(!checkPermissions()) {
            requestPermission()
        }

        if(!isLocationEnabled()) {
            Toast.makeText(this, "Turn on location", Toast.LENGTH_SHORT).show()
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        }

        fusedLocationProvider.lastLocation.addOnCompleteListener(this){ task->
            val location: Location?=task.result
            if(location==null){
                Toast.makeText(this, "Null Received", Toast.LENGTH_SHORT).show()
            }else{
                Log.d("MOCK", "Position: Lat: ${location.latitude}, Lng: ${location.longitude}")
                this.location = LatLng(location.latitude, location.longitude)
                googleMap.addMarker(MarkerOptions().position(this.location).title("Your Location"))
                    ?.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(this.location, 12f))
                val location = hashMapOf(
                    "latitude" to location.latitude,
                    "longitude" to location.longitude,
                )
                db.collection("locations").document(auth.currentUser?.email.toString()).set(location)
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to post location", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    private fun isLocationEnabled(): Boolean{
        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    companion object{
        private const val PERMISSION_REQUEST_ACCESS_LOCATION=100
    }
    private fun checkPermissions(): Boolean{
        if(ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED){
            return true
        }
        return false
    }

    private fun requestPermission(){
        ActivityCompat.requestPermissions(this, arrayOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_REQUEST_ACCESS_LOCATION)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == PERMISSION_REQUEST_ACCESS_LOCATION){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(applicationContext, "Granted", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(applicationContext, "Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}