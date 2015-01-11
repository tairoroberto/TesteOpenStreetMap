package br.com.tairoroberto.teteonpenstreetmap;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;


public class MainActivity extends ActionBarActivity implements LocationListener{
    //MapView OSM
    private MapView mapView;
    private MapController mapController;

    //LocationManager para o GPS
    LocationManager locationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapView = (MapView)findViewById(R.id.mapView);
        mapView.setTileSource(TileSourceFactory.MAPNIK);

        //habilita o zoom
        mapView.setBuiltInZoomControls(true);

        //habilita o touch
        mapView.setMultiTouchControls(true);

        //Inicializa o controller do mapa
        mapController = (MapController) mapView.getController();

        //seta o ponto de localização do mapa = equivalente ao LatLng da Api do coogleMaps
        GeoPoint geoPoint = new GeoPoint(-20.1698,-40.2487);

        //configura o zoom no mapa
        mapController.setZoom(10);

        //Sem animação do mapa
        // mapController.setCenter(geoPoint);

        //com animação do mapa
        mapController.animateTo(geoPoint);

        //Adiciona o marcador no mapa
        addMarker(geoPoint);

    }


    public void addMarker(GeoPoint geoPoint){
        Marker marker = new Marker(mapView);

        //configura a posição do marcador
        marker.setPosition(geoPoint);
        marker.setAnchor(Marker.ANCHOR_CENTER,Marker.ANCHOR_BOTTOM);

        //Adiciona um icone ao mapa para o marcador
        //marker.setIcon(getResources().getDrawable(R.drawable.ic_launcher));

        //limpa todos os marcadores do mapa, para poder adicionar apenas o nosso
        mapView.getOverlays().clear();

        //Adiciona nosso marcador ao mapa
        mapView.getOverlays().add(marker);

        //Dá um refresh no mapa para aparecer o marcador
        mapView.invalidate();

        //configura o GPS
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //Pega as atualizaçãoes do GPS
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);
    }

    @Override
    public void onLocationChanged(Location location) {
        GeoPoint geoPoint = new GeoPoint(location.getLatitude(),location.getLongitude());
        mapController.animateTo(geoPoint);
        addMarker(geoPoint);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationManager  != null){
            locationManager.removeUpdates(this);
        }
    }
}
