package utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.SimpleAdapter;

public class AutoCompleteForPlace extends AsyncTask<String, Void, String> {

	private ParserTask parserTask;
	private AutoCompleteTextView autoCompleteTextView;
	private Context context;

	public AutoCompleteForPlace(Context context,
			AutoCompleteTextView autoCompleteTextView) {
		// TODO Auto-generated constructor stub
		this.autoCompleteTextView = autoCompleteTextView;
		this.context = context;
	}

	@Override
	protected String doInBackground(String... place) {
		// For storing data from web service
		String data = "";

		// Obtain browser key from https://code.google.com/apis/console
		String key = "key=" + Constants.Google_Browser_Key;

		String input = "";

		try {
			input = "input=" + URLEncoder.encode(place[0], "utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		// place type to be searched
		String types = "types=geocode";

		// Sensor enabled
		String sensor = "sensor=false";

		// Building the parameters to the web service
		String parameters = input + "&" + types + "&" + sensor + "&" + key;

		// Output format
		String output = "json";

		// Building the url to the web service
		String url = "https://maps.googleapis.com/maps/api/place/autocomplete/"
				+ output + "?" + parameters;

		try {
			// Fetching the data from web service in background
			data = CustomHttpClient.executeHttpGet(url);
		} catch (Exception e) {
			Log.d("Background Task", e.toString());
		}
		return data;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		Log.i("--------------------------------------", result);
		// Creating ParserTask
		parserTask = new ParserTask();

		// Starting Parsing the JSON string returned by Web Service
		parserTask.execute(result);
	}

	class ParserTask extends
			AsyncTask<String, Integer, List<HashMap<String, String>>> {

		JSONObject jObject;

		@Override
		protected List<HashMap<String, String>> doInBackground(
				String... jsonData) {

			List<HashMap<String, String>> places = null;

			PlaceJSONParser placeJsonParser = new PlaceJSONParser();

			try {
				jObject = new JSONObject(jsonData[0]);

				// Getting the parsed data as a List construct
				places = placeJsonParser.parse(jObject);

			} catch (Exception e) {
				Log.d("Exception", e.toString());
			}
			return places;
		}

		@Override
		protected void onPostExecute(List<HashMap<String, String>> result) {

			String[] from = new String[] { "description" };
			int[] to = new int[] { android.R.id.text1 };

			// Creating a SimpleAdapter for the AutoCompleteTextView
			SimpleAdapter adapter = new SimpleAdapter(context, result,
					android.R.layout.simple_list_item_1, from, to);

			// Setting the adapter
			autoCompleteTextView.setAdapter(adapter);
		}
	}

}
