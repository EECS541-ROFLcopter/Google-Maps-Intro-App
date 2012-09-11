package com.eecs541.googlemapsintro;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
 
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class GoogleMaps extends MapActivity {
	
	/*************************
	 * 
	 * MEMBER VARIABLES
	 * 
	 * ***********************/
	
	private MapController mapController; 	//MapController Object
	private MapView mapView;				//MapView Object
	private LocationManager locationMgr;	//LocationManager Object
	private MapsItemizedOverlay itemizedOverlay;	//List of Overlay Items
	private List<Overlay> overlays;			//List of overlays
	private Location location;				//Current Location
	
	//Menu item locations
	public static final int INSERT_ID = Menu.FIRST;
	public static final int CENTER_ID = Menu.FIRST + 1;
	public static final int SATELLITE_ID = Menu.FIRST + 2;
	public static final int MAP_ID = Menu.FIRST + 3;
	
	
	/*************************
	 * 
	 * CLASS FUNCTIONS
	 * 
	 * ***********************/

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		//Find and set up the Map View
		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);

		//Set up Map Overlays
		overlays = mapView.getOverlays();
		Drawable drawable = this.getResources().getDrawable(R.drawable.ic_launcher);
		itemizedOverlay = new MapsItemizedOverlay(drawable, this);

		//Set up the GPS Location service
		locationMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, new GeoUpdateHandler());
		

		//Set up the map controller
		mapController = mapView.getController();
		//mapController.animateTo(point);
		mapController.setZoom(14);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		menu.add(0, INSERT_ID, 0, R.string.menu_add);
		menu.add(0, CENTER_ID, 0, R.string.menu_location);
		menu.add(0, SATELLITE_ID, 0, R.string.menu_satellite);
		menu.add(0, MAP_ID, 0, R.string.menu_map);
		return result;
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	/*************************
	 * 
	 * ADDITIONAL CLASS MEMBER FUNCTIONS
	 * 
	 * ***********************/
	
	public GeoPoint getGeoPoint() {
		int lat = (int) (location.getLatitude() * 1E6);
		int lng = (int) (location.getLongitude() * 1E6);
		GeoPoint point = new GeoPoint(lat, lng);
		return point;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
			case INSERT_ID:
				createMarker();
				return true;
			case CENTER_ID:
				centerMap();
				return true;
			case SATELLITE_ID:
				satelliteMap(true);
				return true;
			case MAP_ID:
				satelliteMap(false);
				return true;
		}

		return super.onMenuItemSelected(featureId, item);
	}

	private void satelliteMap(boolean satellite) {
		if (satellite) {
			mapView.setSatellite(true);
		} else {
			mapView.setSatellite(false);
		}
	}

	private void centerMap() {
		mapController.animateTo(getGeoPoint());
	}

	private void createMarker() {
		GeoPoint point = mapView.getMapCenter();
		OverlayItem overlayItem = new OverlayItem(point, "", "");
		itemizedOverlay.addOverlay(overlayItem);
		overlays.add(itemizedOverlay);
	}
	
	/*************************
	 * 
	 * INTERNAL SUB-CLASSES
	 * 
	 * ***********************/
	
	public class GeoUpdateHandler implements LocationListener {

		public void onLocationChanged(Location newLocation) {
			System.out.println("Called");
			location = newLocation;
		}

		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	}
}
