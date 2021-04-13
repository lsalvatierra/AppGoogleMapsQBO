package com.qbo.appgooglemapsqbo

import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions


class MapsActivity : AppCompatActivity(), OnMapReadyCallback,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerDragListener, LocationListener {

    private lateinit var mMap: GoogleMap
    private var  lstLatLong = ArrayList<LatLng>()
    private lateinit var locationManager: LocationManager
    private val LOCATION_REFRESH_TIME: Long = 10000
    private val LOCATION_REFRESH_DISTANCE: Float = 10F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        //Agregar el evento para hacer click sobre el mapa.
        mMap.setOnMapClickListener(this)
        //Agregar eventos de arrastre de marcadores
        mMap.setOnMarkerDragListener(this)
        val sydney = LatLng(-12.066957, -77.035824)

        mMap.addMarker(MarkerOptions()
                .position(sydney)
                .title("IDAT Lima Centro")
                .snippet("Ahora mismo no estamos aqui")
                .draggable(true)
                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location))
                .flat(true))
        //mMap.isTrafficEnabled = true
        //mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
        //mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16.0f))

        try {
            /**Habilitadmos que se muestre nuestra el ícono de Ubicación
             * en la Parte superior del Mapa */
            mMap.isMyLocationEnabled = true
            /**Llamamos al método para obtener la ubicación actual */
            obtenerUbicacion()
        } catch (e: SecurityException) {
            Toast.makeText(applicationContext, "Error activando Ubicación", Toast.LENGTH_LONG).show()
        }
    }

    private fun obtenerUbicacion(){
        try{
            /**Instancia al Sistema, solicitando el servicio de Localización*/
            locationManager = getSystemService(LOCATION_SERVICE) as LocationManager;
            /**El método requestLocationUpdate, donde le pasamos el Proveedor (GPS en este caso)
             * El tiempo en milisegundos cuando debe actualizar, La distancia en metros cuando
             * debe cambiar y la interfaz LocationListener this ya esta clase la implementa. */
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    LOCATION_REFRESH_TIME, LOCATION_REFRESH_DISTANCE, this)
        }catch (ex: SecurityException){
            Toast.makeText(applicationContext,
                    "Error mostrando la unicacion",
                    Toast.LENGTH_LONG).show()
        }
    }

    override fun onMapClick(p0: LatLng?) {
        mMap.addMarker(
                MarkerOptions().position(p0!!)
                        .title("Nuevo marcador")
        )
        lstLatLong.add(p0)
        val p = PolylineOptions()
        p.color(Color.RED)
        p.width(5F)
        p.addAll(lstLatLong)
        mMap.addPolyline(p)
        mMap.animateCamera(CameraUpdateFactory.newLatLng(p0!!))
    }

    override fun onMarkerDragStart(p0: Marker?) {
        p0!!.hideInfoWindow()
    }

    override fun onMarkerDrag(p0: Marker?) {
        var posicion = p0!!.position
        p0!!.snippet = posicion.latitude.toString() + " - "+ posicion.longitude.toString()
        p0!!.showInfoWindow()
    }

    override fun onMarkerDragEnd(p0: Marker?) {
        TODO("Not yet implemented")
    }


    /*Llamado cuando la Localización cambia*/
    override fun onLocationChanged(location: Location?) {
        var ubicacion = LatLng(location!!.latitude, location.longitude)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 15F))
    }

    /*Llamado cuando el estado del proveedor cambia. */

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

    }

    /*Llamado cuando el proveedor es habilitado por el usuario. */
    override fun onProviderEnabled(provider: String?) {

    }

    /*Llamado cuando el proveedor es deshabilitado por el usuario. */
    override fun onProviderDisabled(provider: String?) {

    }
}