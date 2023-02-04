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
import android.util.Log
import android.widget.Button
import android.widget.TextView
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.macc.catchgame.R
import com.macc.catchgame.adapters.GameUserAdapter

class GameActivity : AppCompatActivity(), OnMapReadyCallback, LocationListener {

    // TODO remove this button from production
    private lateinit var buttonMoveOnToResults: Button

    private lateinit var gameId: String
    private var timeLeft: Long = -1

    private lateinit var mapFragment: SupportMapFragment
    private lateinit var recyclerView: RecyclerView
    private lateinit var textViewTimeLeft: TextView

    private val playerList = ArrayList<String>()
    private lateinit var userAdapter: GameUserAdapter

    private lateinit var locationManager: LocationManager
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var location: Location
    private val locationPermissionCode = 2

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

        location = Location("")
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLocation()

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

        // TODO refactor to automatic transition when time is over
        buttonMoveOnToResults = findViewById(R.id.buttonMoveOnToResults)
        buttonMoveOnToResults.setOnClickListener {
            val game = hashMapOf(
                "state" to "finished",
            )
            db.collection("games").document(gameId).set(game, SetOptions.merge())
                .addOnFailureListener {
                    Toast.makeText(applicationContext, "Failed to end game", Toast.LENGTH_SHORT).show()
                }
        }
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