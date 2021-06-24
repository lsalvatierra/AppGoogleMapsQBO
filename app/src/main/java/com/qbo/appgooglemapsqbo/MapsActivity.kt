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
import com.google.android.gms.maps.model.*
import com.google.maps.android.PolyUtil
import com.qbo.appgooglemapsqbo.commom.AppMensaje
import com.qbo.appgooglemapsqbo.commom.Constantes
import com.qbo.appgooglemapsqbo.commom.TipoMensaje


class MapsActivity : AppCompatActivity(), OnMapReadyCallback,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerDragListener, LocationListener, GoogleMap.OnPolygonClickListener {

    private lateinit var mMap: GoogleMap
    private var  lstLatLong = ArrayList<LatLng>()
    private lateinit var locationManager: LocationManager

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
        agregarPoligono()
    }

    private fun agregarPoligono(){
        // Add polygons to indicate areas on the map.
        val polygon1 = mMap.addPolygon(
            PolygonOptions()
                .clickable(true)
                .add(
                    LatLng(-12.114093, -77.036811),
                    LatLng(-12.113846, -77.029859),
                    LatLng(-12.118865, -77.029087),
                    LatLng(-12.118912, -77.036962)))
        // Store a data object with the polygon, used here to indicate an arbitrary type.
        polygon1.tag = "alpha"
        polygon1.strokeColor = -0xc771c4
        //polygon1.fillColor = -0x7e387c

        mMap.setOnPolygonClickListener(this)
        //Validar si un punto X se encuentra dentro del polígono
        //Utilizamos la libreria https://github.com/googlemaps/android-maps-utils
        var inside = PolyUtil.containsLocation(LatLng(-12.114872, -77.033678), polygon1.points, false)
        val mensaje = if(inside){
            "El punto buscando se encuentra dentro del polígono"
        }else{
            "El punto buscando NO se encuentra dentro del polígono"
        }
        Toast.makeText(applicationContext,
            mensaje,
            Toast.LENGTH_LONG).show()
    }

    private fun obtenerUbicacion(){
        try{
            /**Instancia al Sistema, solicitando el servicio de Localización*/
            locationManager = getSystemService(LOCATION_SERVICE) as LocationManager;
            /**El método requestLocationUpdate, donde le pasamos el Proveedor (GPS en este caso)
             * El tiempo en milisegundos cuando debe actualizar, La distancia en metros cuando
             * debe cambiar y la interfaz LocationListener this ya esta clase la implementa. */
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                   Constantes.LOCATION_REFRESH_TIME, Constantes.LOCATION_REFRESH_DISTANCE, this)
        }catch (ex: SecurityException){
            Toast.makeText(applicationContext,
                    "Error mostrando la unicacion",
                    Toast.LENGTH_LONG).show()
        }
    }

    override fun onMapClick(p0: LatLng?) {
        mMap.addMarker(
                MarkerOptions()
                    .position(p0!!)
                    .title("Nuevo marcador")
                    //.draggable(true)
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

    }

    override fun onMarkerDragEnd(p0: Marker?) {
        var posicion = p0!!.position
        p0!!.snippet = posicion.latitude.toString() + " - "+ posicion.longitude.toString()
        p0!!.showInfoWindow()
        mMap.animateCamera(CameraUpdateFactory.newLatLng(p0!!.position))
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

    override fun onPolygonClick(p0: Polygon?) {
        // Flip the values of the red, green, and blue components of the polygon's color.
        var color = p0!!.strokeColor xor -0xa80e9
        p0!!.strokeColor = color
        color = p0.fillColor xor -0xa80e9
        p0.fillColor = color
    }
}