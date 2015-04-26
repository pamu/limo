package ws.empirelimo;

import java.text.SimpleDateFormat;
import java.util.Date;

import utils.AutoCompleteForPlace;
import utils.Constants;
import utils.DrawerBaseActivity;
import utils.Global;
import utils.GoogleMapData;
import utils.Route;
import utils.Utils;
import utils.WebServices;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class BookRideActivity extends DrawerBaseActivity implements
		OnClickListener, LocationListener {

	private GoogleMap googleMap;
	private SupportMapFragment mMapFragment;
	private Button buttonBookNow, buttonBookLatter, buttonLocate;
	private AutoCompleteTextView editDestination, editCurrent;
	private Context context;
	Time time;
	TimePicker timePicker;
	DatePicker datePicker;
	public static LinearLayout layoutLocate;
	public static LinearLayout layoutBook;
	ProgressDialog progressDialog;

	SimpleDateFormat adf = new SimpleDateFormat("MMM/dd/yy");

	private LocationManager locationManager;

	private static final long MIN_TIME = 400;
	private static final float MIN_DISTANCE = 1000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.activity_bookride);
		this.context = this;

		uiComponent();
		initilizeMap();
		// Toast.makeText(context,
		// Global.sharedPreferences.getString(Constants.prefMobileNo, ""),
		// Toast.LENGTH_SHORT).show();

		buttonBookLatter.setOnClickListener(this);
		buttonBookNow.setOnClickListener(this);
		buttonLocate.setOnClickListener(this);

		editCurrent.setThreshold(1);
		editDestination.setThreshold(1);

		editCurrent.addTextChangedListener(new CustomTextWatcher(this,
				editCurrent));
		editDestination.addTextChangedListener(new CustomTextWatcher(this,
				editDestination));

		editDestination.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				drawRoute();
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(editDestination.getWindowToken(), 0);
			}
		});

	}

	// Layout Visibility

	public static void layoutVisibility(int visibilityForBook,
			int visibilityForLocate) {

		layoutBook.setVisibility(visibilityForBook);
		layoutLocate.setVisibility(visibilityForLocate);
	}

	// Custom class for AutoComplete Text View Listener

	private class CustomTextWatcher implements TextWatcher {
		private AutoCompleteTextView autoCompleteTextView;
		private Context context;

		public CustomTextWatcher(Context context,
				AutoCompleteTextView autoCompleteTextView) {
			this.autoCompleteTextView = autoCompleteTextView;
			this.context = context;
		}

		public void beforeTextChanged(CharSequence charSequence, int start,
				int count, int after) {

			if (layoutLocate.getVisibility() == View.GONE)
				layoutVisibility(View.GONE, View.VISIBLE);
			AutoCompleteForPlace placesTaskForSource = new AutoCompleteForPlace(
					context, autoCompleteTextView);
			placesTaskForSource.execute(charSequence.toString());
		}

		public void onTextChanged(CharSequence charSequence, int start,
				int before, int count) {
		}

		public void afterTextChanged(Editable editable) {
		}
	}

	// Draw Route
	private void drawRoute() {

		googleMap.clear();

		Route route = new Route();
		route.drawRoute(googleMap, context, WebServices.getLatLng(
				BookRideActivity.this, editCurrent.getText().toString()),
				WebServices.getLatLng(BookRideActivity.this, editDestination
						.getText().toString()), "driving", true,
				Route.LANGUAGE_ENGLISH);

		// if (layoutBook.getVisibility() != View.VISIBLE)
		// layoutVisibility(View.VISIBLE, View.GONE);

	}

	// Assign UI-Component
	private void uiComponent() {
		buttonBookNow = (Button) findViewById(R.id.buttonBookNow);
		buttonBookLatter = (Button) findViewById(R.id.buttonBookLatter);
		editDestination = (AutoCompleteTextView) findViewById(R.id.editDestination);
		editCurrent = (AutoCompleteTextView) findViewById(R.id.editCurrent);
		buttonLocate = (Button) findViewById(R.id.buttonLocate);

		layoutLocate = (LinearLayout) findViewById(R.id.layoutLocate);
		layoutBook = (LinearLayout) findViewById(R.id.layoutBook);

		layoutLocate.setVisibility(View.VISIBLE);
		layoutBook.setVisibility(View.GONE);
	}

	// Assign Google Map
	private void initilizeMap() {
		if (googleMap == null) {
			mMapFragment = SupportMapFragment.newInstance();
			FragmentTransaction fragmentTransaction = getSupportFragmentManager()
					.beginTransaction();
			fragmentTransaction.add(R.id.map, mMapFragment);
			fragmentTransaction.commit();

		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mMapFragment != null) {
			googleMap = mMapFragment.getMap();
			googleMap.getUiSettings().setZoomControlsEnabled(false);
			googleMap.getUiSettings().setRotateGesturesEnabled(true);

			locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			locationManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE,
					this);

			// Check GooglMap is null or not

			if (googleMap == null) {
				Toast.makeText(BookRideActivity.this,
						"Sorry! unable to getting map", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	@Override
	public void onClick(View button) {
		if (button == buttonBookNow) {

			if (Global.connectionDetector.isConnectingToInternet()) {

				if (editCurrent.getText().toString().length() != 0
						&& editDestination.getText().toString().length() != 0) {
					try {

						LayoutInflater layoutInflater = (LayoutInflater) context
								.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
						final View customDialogView1 = layoutInflater.inflate(
								R.layout.customdialog1, null);
						final AlertDialog confirmDialog = new AlertDialog.Builder(
								context).create();
						confirmDialog.setView(customDialogView1);
						customDialogView1.findViewById(R.id.buttonBook)
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										confirmDialog.dismiss();

										progressDialog = new ProgressDialog(
												context);
										progressDialog
												.setMessage("Please wait...");
										progressDialog.setIndeterminate(true);
										progressDialog.show();

										final Handler handler = new Handler() {

											@Override
											public void handleMessage(
													Message msg) {
												super.handleMessage(msg);

												if (progressDialog != null
														&& progressDialog
																.isShowing())
													progressDialog.dismiss();

												Intent intent = new Intent(
														context,
														ConfirmationActivity.class);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_out_up,
														R.anim.slide_in_up);

											}

										};

										// Current Time

										time = new Time(Time
												.getCurrentTimezone());
										time.setToNow();

										final String currentTime = time
												.format("%k:%M:%S");
										final String currentDate = adf
												.format(new Date());

										new Thread(new Runnable() {

											@Override
											public void run() {

												// Values Assign in class

												String distance = WebServices
														.GetDistance(
																editCurrent
																		.getText()
																		.toString(),
																editDestination
																		.getText()
																		.toString());

												Global.googleMapData = new GoogleMapData(
														WebServices
																.getLatLng(
																		BookRideActivity.this,
																		editCurrent
																				.getText()
																				.toString()),
														WebServices
																.getLatLng(
																		BookRideActivity.this,
																		editDestination
																				.getText()
																				.toString()),
														distance,
														editCurrent.getText()
																.toString(),
														editDestination
																.getText()
																.toString(),
														currentTime,
														currentDate,
														Double.parseDouble(distance
																.replace(" mi",
																		""))
																* Constants.FarePerMile
																+ "");

												handler.sendEmptyMessage(0);

											}
										}).start();

									}
								});
						customDialogView1.findViewById(R.id.buttonBack)
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										confirmDialog.dismiss();

									}
								});
						confirmDialog.setCancelable(false);
						confirmDialog.show();

					} catch (Exception e) {
						e.printStackTrace();
						Toast.makeText(BookRideActivity.this,
								"Something went wrong..", Toast.LENGTH_SHORT)
								.show();
					}

				} else {
					Toast.makeText(BookRideActivity.this, "Enter Address",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(context, "Internet required..",
						Toast.LENGTH_SHORT).show();
			}

		} else if (button == buttonBookLatter) {

			if (Global.connectionDetector.isConnectingToInternet()) {

				if (editCurrent.getText().toString().length() != 0
						&& editDestination.getText().toString().length() != 0) {
					try {

						LayoutInflater layoutInflater = (LayoutInflater) context
								.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
						final View customDialogView2 = layoutInflater.inflate(
								R.layout.customdialog2, null);
						final AlertDialog dateDialog = new AlertDialog.Builder(
								context).create();

						dateDialog.setView(customDialogView2);
						timePicker = (TimePicker) customDialogView2
								.findViewById(R.id.pickerTime);
						timePicker.setIs24HourView(true);
						datePicker = (DatePicker) customDialogView2
								.findViewById(R.id.pickerDate);

						customDialogView2.findViewById(R.id.buttonBook)
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										dateDialog.dismiss();

										progressDialog = new ProgressDialog(
												context);
										progressDialog
												.setMessage("Please wait...");
										progressDialog.setIndeterminate(true);
										progressDialog.show();

										final Handler handler = new Handler() {

											@Override
											public void handleMessage(
													Message msg) {
												super.handleMessage(msg);

												if (progressDialog != null
														&& progressDialog
																.isShowing())
													progressDialog.dismiss();
												Intent intent = new Intent(
														context,
														ConfirmationActivity.class);
												startActivity(intent);
												overridePendingTransition(
														R.anim.slide_out_up,
														R.anim.slide_in_up);
											}

										};

										new Thread(new Runnable() {

											@Override
											public void run() {

												@SuppressWarnings("deprecation")
												Date yourDate = new Date(
														datePicker.getYear(),
														datePicker.getMonth(),
														datePicker
																.getDayOfMonth());

												Log.d("--Date--",
														adf.format(yourDate)
																+ "");

												String distance = WebServices
														.GetDistance(
																editCurrent
																		.getText()
																		.toString(),
																editDestination
																		.getText()
																		.toString());

												// Values Assign in class
												Global.googleMapData = new GoogleMapData(
														WebServices
																.getLatLng(
																		BookRideActivity.this,
																		editCurrent
																				.getText()
																				.toString()),
														WebServices
																.getLatLng(
																		BookRideActivity.this,
																		editDestination
																				.getText()
																				.toString()),
														distance,
														editCurrent.getText()
																.toString(),
														editDestination
																.getText()
																.toString(),
														timePicker
																.getCurrentHour()
																+ ":"
																+ timePicker
																		.getCurrentMinute()
																+ ":00",

														adf.format(yourDate),

														Double.parseDouble(distance
																.replace(" mi",
																		""))
																* Constants.FarePerMile
																+ "");
												handler.sendEmptyMessage(0);

											}
										}).start();

									}
								});

						customDialogView2.findViewById(R.id.buttonBack)
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										dateDialog.dismiss();
									}
								});

						dateDialog.setCancelable(false);
						dateDialog.show();

					} catch (Exception e) {
						e.printStackTrace();
						Toast.makeText(BookRideActivity.this,
								"Something went wrong..", Toast.LENGTH_SHORT)
								.show();
					}

				} else {
					Toast.makeText(BookRideActivity.this, "Enter Address",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(context, "Internet required..",
						Toast.LENGTH_SHORT).show();
			}

		} else {

			if (Global.connectionDetector.isConnectingToInternet()) {
				if (editCurrent.getText().toString().length() != 0
						&& editDestination.getText().toString().length() != 0) {
					drawRoute();
				} else {
					Toast.makeText(BookRideActivity.this, "Enter Address",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(context, "Internet required..",
						Toast.LENGTH_SHORT).show();
			}
		}

	}

	@Override
	public void onLocationChanged(Location location) {

		LatLng latLng = new LatLng(location.getLatitude(),
				location.getLongitude());
		CameraUpdate cameraUpdate = CameraUpdateFactory
				.newLatLngZoom(latLng, 2);
		googleMap.animateCamera(cameraUpdate);
		locationManager.removeUpdates(this);

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}
}
