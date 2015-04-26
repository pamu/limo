package utils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import ws.empirelimo.BookRideActivity;

import com.google.android.gms.maps.model.LatLng;
//import com.google.android.maps.GeoPoint;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class WebServices {

	// Google Map Distance Count

	public static String GetDistance(String currentLocation,
			String destinationLocation) {

		String url = "http://maps.googleapis.com/maps/api/distancematrix/json?origins="
				+ currentLocation
				+ "&destinations="
				+ destinationLocation
				+ "&units=imperial";

		url = url.replace(" ", "%20");
		try {

			String response = CustomHttpClient.executeHttpGet(url);
			JSONObject object = new JSONObject(response);

			JSONArray arrayrow = object.getJSONArray("rows");
			for (int a = 0; a < arrayrow.length(); a++) {
				JSONObject objrow = arrayrow.getJSONObject(a);
				JSONArray arrayelement = objrow.getJSONArray("elements");
				for (int b = 0; b < arrayelement.length(); b++) {
					JSONObject objElement = arrayelement.getJSONObject(b);
					JSONObject objDistance = objElement
							.getJSONObject("distance");
					return objDistance.getString("text");

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";

	}

	// Google Map Get LatLang from PlaceName

	public static LatLng getLatLng(Context context, String placesName) {
		double lat = 0.0;
		double lng = 0.0;
		String url = "http://maps.google.com/maps/api/geocode/json?address="
				+ placesName + "&ka&sensor=false";
		url = url.replace(" ", "%20");
		try {
			String response = CustomHttpClient.executeHttpGet(url);
			JSONObject object = new JSONObject(response);

			lng = new Double(0);
			lat = new Double(0);

			lng = ((JSONArray) object.get("results")).getJSONObject(0)
					.getJSONObject("geometry").getJSONObject("location")
					.getDouble("lng");

			lat = ((JSONArray) object.get("results")).getJSONObject(0)
					.getJSONObject("geometry").getJSONObject("location")
					.getDouble("lat");

		} catch (Exception e) {

			BookRideActivity.layoutVisibility(View.GONE, View.VISIBLE);

			Toast.makeText(context, "Enter Addess Properly", Toast.LENGTH_SHORT)
					.show();
			e.printStackTrace();
		}

		return new LatLng(lat, lng);

	}

	public static String bookCab(String sourcePlacename,
			String destinationPlacename, String sourceLat, String sourceLng,
			String destinationLat, String destinationLng, String distance,
			String time, String date, String imei, String deviceId,
			String email, String name, String mobile, String totalFare) {

		try {

			SoapObject request = new SoapObject(Constants.NAMESPACE,
					Constants.METHOD_NAME_BOOKCAB);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);

			request.addProperty(Constants.Ws_Source_Place_Name, sourcePlacename);
			request.addProperty(Constants.Ws_Destination_Place_Name,
					destinationPlacename);
			request.addProperty(Constants.Ws_Email_Id, email);
			request.addProperty(Constants.Ws_Source_Latitude, sourceLat);
			request.addProperty(Constants.Ws_Source_Longitude, sourceLng);
			request.addProperty(Constants.Ws_Destination_Latitude,
					destinationLat);
			request.addProperty(Constants.Ws_Destination_Longitude,
					destinationLng);
			request.addProperty(Constants.Ws_Distance,
					distance.replace("mi", "miles"));
			request.addProperty(Constants.Ws_Time, time);
			request.addProperty(Constants.Ws_Date, date);
			request.addProperty(Constants.Ws_IMEI, imei);
			request.addProperty(Constants.Ws_DeviceId, deviceId);
			request.addProperty(Constants.Ws_Name, name);
			request.addProperty(Constants.Ws_Mobile, mobile);
			request.addProperty(Constants.Ws_TotalFare, totalFare);
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);

			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					Constants.URL, 60000);
			androidHttpTransport.debug = true;

			androidHttpTransport.call(Constants.SOAP_ACTION, envelope);
			Object result = envelope.getResponse();
			String response = result.toString();

			Log.d("--Response--", response);

			return response;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "0";

	}

}
