package utils;

import com.google.android.gms.maps.model.LatLng;

public class GoogleMapData {

	LatLng currentLatLng;
	LatLng destinationLatLng;
	String distance;
	String sourcePlace;
	String destinationPlace;
	String time;
	String date;
	String totalFare;

	public GoogleMapData(LatLng currentLatLng, LatLng destinationLatLng,
			String distance, String sourcePlace, String destinationPlace,
			String time, String date, String totalFare) {

		this.currentLatLng = currentLatLng;
		this.destinationLatLng = destinationLatLng;
		this.distance = distance;
		this.sourcePlace = sourcePlace;
		this.destinationPlace = destinationPlace;
		this.time = time;
		this.date = date;
		this.totalFare = totalFare;

	}

	public String getTotalFare() {
		return totalFare;
	}

	public LatLng getCurrentLatLng() {
		return currentLatLng;
	}

	public LatLng getDestinationLatLng() {
		return destinationLatLng;
	}

	public String getDate() {
		return date;
	}

	public String getTime() {
		return time;
	}

	public String getDistance() {
		return distance;
	}

	public String getSourcePlace() {
		return sourcePlace;
	}

	public String getDestinationPlace() {
		return destinationPlace;
	}

}
