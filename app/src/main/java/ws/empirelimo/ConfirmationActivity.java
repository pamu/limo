package ws.empirelimo;

import utils.Constants;
import utils.DrawerBaseActivity;
import utils.Global;
import utils.GoogleMapData;
import utils.WebServices;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.MailTo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class ConfirmationActivity extends ActionBarActivity implements
		OnClickListener {

	private TextView textClickHere, textFlightNo, textToValue, textFromValue,
			textTime, textDate, textDistanceValue, textFareValue;
	private Button buttonPayment;
	private LinearLayout layoutPlane;
	private ProgressDialog progressDialog;
	private RadioButton checkPaymentNow, checkPaymentLater;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confirmation);

		uiComponent();
		assignValues();

		getSupportActionBar().setIcon(
				new ColorDrawable(getResources().getColor(
						android.R.color.transparent)));

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		textClickHere.setOnClickListener(this);
		buttonPayment.setOnClickListener(this);

	}

	private void assignValues() {

		textToValue.setText(Global.googleMapData.getDestinationPlace());
		textFromValue.setText(Global.googleMapData.getSourcePlace());
		textTime.setText(Global.googleMapData.getTime());
		textDate.setText(Global.googleMapData.getDate());
		textDistanceValue.setText("Your Journey : "
				+ Global.googleMapData.getDistance().replace("mi", "Miles")
				+ " to Go..");

		Log.d("-------------Distance", Global.googleMapData.getDistance());

		textFareValue.setText(Constants.CurrencySymbol
				+ Global.googleMapData.getTotalFare() + " ( "
				+ Constants.CurrencySymbol + Constants.FarePerMile
				+ " per mile )");
	}

	private void uiComponent() {
		textFromValue = (TextView) findViewById(R.id.textFromValue);
		textToValue = (TextView) findViewById(R.id.textToValue);
		textDistanceValue = (TextView) findViewById(R.id.textDistanceValue);
		textTime = (TextView) findViewById(R.id.textTime);
		textDate = (TextView) findViewById(R.id.textDate);
		textFlightNo = (TextView) findViewById(R.id.textFlightNo);
		textFareValue = (TextView) findViewById(R.id.textFareValue);
		textClickHere = (TextView) findViewById(R.id.textClickHere);
		buttonPayment = (Button) findViewById(R.id.buttonPayment);
		layoutPlane = (LinearLayout) findViewById(R.id.layoutPlane);

		checkPaymentNow = (RadioButton) findViewById(R.id.checkPaymentNow);
		checkPaymentLater = (RadioButton) findViewById(R.id.checkPaymentLater);

		//
                                                 
		// textFlightNo.setVisibility(View.GONE);
		layoutPlane.setVisibility(View.GONE);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case android.R.id.home:
			ConfirmationActivity.this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View view) {

		if (view == buttonPayment) {
			if (checkPaymentNow.isChecked()) {
				Intent intent = new Intent(ConfirmationActivity.this,
						ContactActivity.class);
				intent.putExtra(Constants.isPaymentNow, true);
				startActivity(intent);
				overridePendingTransition(R.anim.slide_out_up,
						R.anim.slide_in_up);

			} else {
				if (Global.connectionDetector.isConnectingToInternet()) {
					Intent intent = new Intent(ConfirmationActivity.this,
							ContactActivity.class);
					intent.putExtra(Constants.isPaymentNow, false);
					startActivity(intent);
					overridePendingTransition(R.anim.slide_out_up,
							R.anim.slide_in_up);
				} else {
					Toast.makeText(ConfirmationActivity.this,
							"Internet required..", Toast.LENGTH_SHORT).show();
				}
			}

		} else {
			// Click here

		}

	}
}