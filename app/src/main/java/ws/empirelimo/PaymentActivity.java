package ws.empirelimo;

import com.nabancard.sdkadvanced.CustomizeSDKAdvanced;
import com.nabancard.sdkadvanced.SDKAdvancedCallbacks;

import utils.Constants;
import utils.Global;
import utils.Payment;
import utils.WebServices;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PaymentActivity extends ActionBarActivity {

	private EditText editCardholderName, editCardholderNumber,
			editCardholderMonth, editCardholderYear;
	private Button buttonPayment;
	private ProgressDialog progressDialog;
	private CustomizeSDKAdvanced instance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payment);

		getSupportActionBar().setIcon(
				new ColorDrawable(getResources().getColor(
						android.R.color.transparent)));

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		uiComponent();

		buttonPayment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (Global.connectionDetector.isConnectingToInternet()) {

					if (editCardholderName.length() == 0) {
						Toast.makeText(PaymentActivity.this, "Enter Name..",
								Toast.LENGTH_SHORT).show();
					} else if (editCardholderNumber.length() == 0) {
						Toast.makeText(PaymentActivity.this, "Enter Number..",
								Toast.LENGTH_SHORT).show();
					} else if (editCardholderMonth.length() == 0) {
						Toast.makeText(PaymentActivity.this, "Enter Month..",
								Toast.LENGTH_SHORT).show();
					} else if (editCardholderYear.length() == 0) {
						Toast.makeText(PaymentActivity.this, "Enter Year..",
								Toast.LENGTH_SHORT).show();
					} else {
						progressDialog = new ProgressDialog(
								PaymentActivity.this);
						progressDialog.setMessage("Please wait...");
						progressDialog.setIndeterminate(true);
						progressDialog.setCancelable(false);
						progressDialog.show();

						makePayment();

					}

				} else {
					Toast.makeText(PaymentActivity.this, "Internet required..",
							Toast.LENGTH_SHORT).show();
				}
			}

		});

	}

	public void makePayment() {
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				if (msg.equals(0)) {
					Toast.makeText(PaymentActivity.this,
							"Something went wrong..", Toast.LENGTH_SHORT)
							.show();
				} else {
					Toast.makeText(PaymentActivity.this,
							"Thank you for payment..", Toast.LENGTH_SHORT)
							.show();
					sendData();

				}

			}
		};

		new Thread(new Runnable() {

			@Override
			public void run() {
				Payment payment = new Payment(editCardholderName.getText()
						.toString(), editCardholderNumber.getText().toString(),
						editCardholderMonth.getText().toString(),
						editCardholderYear.getText().toString());
				if (payment.makePayment()) {
					handler.sendEmptyMessage(1);
				} else
					handler.sendEmptyMessage(0);

			}
		}).start();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case android.R.id.home:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void uiComponent() {
		editCardholderName = (EditText) findViewById(R.id.editCardholderName);
		editCardholderNumber = (EditText) findViewById(R.id.editCardholderNumber);

		editCardholderMonth = (EditText) findViewById(R.id.editCardholderMonth);
		editCardholderYear = (EditText) findViewById(R.id.editCardholderYear);
		buttonPayment = (Button) findViewById(R.id.buttonPayment);
	}

	private void sendData() {

		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (msg.what == 1) {
					if (progressDialog != null && progressDialog.isShowing())
						progressDialog.dismiss();

					startActivity(new Intent(PaymentActivity.this,
							ThankYouActivity.class));
					overridePendingTransition(R.anim.slide_out_up,
							R.anim.slide_in_up);
					finish();
				} else {
					if (progressDialog != null && progressDialog.isShowing())
						progressDialog.dismiss();
					Toast.makeText(PaymentActivity.this,
							"something went wrong..", Toast.LENGTH_SHORT)
							.show();
				}
			}
		};

		new Thread(new Runnable() {

			@Override
			public void run() {
				String email = Global.sharedPreferences.getString(
						Constants.prefEmailAdd, "abc@xyz.com");
				String name = Global.sharedPreferences.getString(
						Constants.prefName, "");
				String phoneNo = Global.sharedPreferences.getString(
						Constants.prefPhoneNo, "");

				try {

					String response = WebServices.bookCab(Global.googleMapData
							.getSourcePlace(), Global.googleMapData
							.getDestinationPlace(),
							String.valueOf(Global.googleMapData
									.getCurrentLatLng().latitude), String
									.valueOf(Global.googleMapData
											.getCurrentLatLng().longitude),
							String.valueOf(Global.googleMapData
									.getDestinationLatLng().latitude), String
									.valueOf(Global.googleMapData
											.getDestinationLatLng().longitude),
							Global.googleMapData.getDistance(),
							Global.googleMapData.getTime(),
							Global.googleMapData.getDate(), "123456789",
							"123456", email, name, phoneNo,
							Global.googleMapData.getTotalFare());

					handler.sendEmptyMessage(Integer.parseInt(response));
				} catch (Exception exception) {
					exception.printStackTrace();
					handler.sendEmptyMessage(0);
				}

			}
		}).start();

	}
}
