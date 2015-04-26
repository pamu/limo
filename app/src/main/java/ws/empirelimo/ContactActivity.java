package ws.empirelimo;

import com.nabancard.sdkadvanced.CustomizeSDKAdvanced;
import com.nabancard.sdkadvanced.SDKAdvancedCallbacks;

import utils.Constants;
import utils.Global;
import utils.PayAnywhere;
import utils.Payment;
import utils.WebServices;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.BadTokenException;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ContactActivity extends ActionBarActivity implements
		SDKAdvancedCallbacks {

	private EditText editName, editPhoneNumber;
	private Button buttonProceed;
	private ProgressDialog progressDialog;
	Bundle bundle;
	boolean isPaymentNow;

	private static boolean showAlert = false;
	private static String alertMessage = "";
	private CustomizeSDKAdvanced sdkAdvanced;

	private PayAnywhere anywhere;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contactinfo);

		if (bundle == null)
			bundle = getIntent().getExtras();

		isPaymentNow = bundle.getBoolean(Constants.isPaymentNow);

		getSupportActionBar().setIcon(
				new ColorDrawable(getResources().getColor(
						android.R.color.transparent)));

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		uiComponent();

		buttonProceed.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (Global.connectionDetector.isConnectingToInternet()) {

					if (editName.length() == 0) {
						Toast.makeText(ContactActivity.this, "Enter Name..",
								Toast.LENGTH_SHORT).show();
					} else if (editPhoneNumber.length() == 0) {
						Toast.makeText(ContactActivity.this, "Enter Number..",
								Toast.LENGTH_SHORT).show();
					} else if (editPhoneNumber.length() <= 9) {
						Toast.makeText(ContactActivity.this,
								"Enter valid Number..", Toast.LENGTH_SHORT)
								.show();
					} else {
						storeValueToSharedPref();
						if (isPaymentNow) {

							anywhere = new PayAnywhere(ContactActivity.this,
									sdkAdvanced, Constants.PA_MerchantId,
									Constants.PA_LoginId,
									Constants.PA_Username,
									Constants.PA_Password);

							anywhere.sdkAdvanced.setAmount(Global.googleMapData
									.getTotalFare());
							anywhere.sdkAdvanced.setInvoice("001");
							anywhere.sdkAdvanced.showChargeScreen();

							// startActivity(new Intent(ContactActivity.this,
							// PaymentActivity.class));
							// overridePendingTransition(R.anim.slide_out_up,
							// R.anim.slide_in_up);

						} else {
							progressDialog = new ProgressDialog(
									ContactActivity.this);
							progressDialog.setMessage("Please wait...");
							progressDialog.setIndeterminate(true);
							progressDialog.setCancelable(false);
							progressDialog.show();
							sendData();

						}
					}
				} else {
					Toast.makeText(ContactActivity.this, "Internet required..",
							Toast.LENGTH_SHORT).show();
				}
			}

		});

	}

	private void sendData() {

		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				Log.d("--Massage--", msg.what + "");

				if (msg.what == 1) {

					if (progressDialog != null && progressDialog.isShowing())
						progressDialog.dismiss();

					startActivity(new Intent(ContactActivity.this,
							ThankYouActivity.class));
					overridePendingTransition(R.anim.slide_out_up,
							R.anim.slide_in_up);
					finish();

				} else {

					if (progressDialog != null && progressDialog.isShowing())
						progressDialog.dismiss();
					Toast.makeText(ContactActivity.this,
							"somthing went wrong..", Toast.LENGTH_SHORT).show();
				}

			}
		};

		new Thread(new Runnable() {

			@Override
			public void run() {

				try {

					String email = Global.sharedPreferences.getString(
							Constants.prefEmailAdd, "abc@xyz.com");
					String name = Global.sharedPreferences.getString(
							Constants.prefName, "");
					String phoneNo = Global.sharedPreferences.getString(
							Constants.prefPhoneNo, "");

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

					Log.d("------------Response--------", response);

					handler.sendEmptyMessage(Integer.parseInt(response));
				} catch (Exception exception) {
					exception.printStackTrace();
					handler.sendEmptyMessage(0);
				}

			}
		}).start();

	}

	private void showAlertDialog() {
		AlertDialog alertDialog = new AlertDialog.Builder(ContactActivity.this)
				.create();
		alertDialog.setTitle(getResources().getString(R.string.app_name));
		alertDialog.setMessage(alertMessage);
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		try {
			alertDialog.show();
		} catch (BadTokenException bte) {
			Log.i("TestApp",
					"exception thrown in testscreen \"showAlertDialog()\""
							+ bte.getMessage());
		} catch (Exception e) {
			Log.i("TestApp", "catch (Exception e) = " + e.getMessage());
		}
	}

	private void storeValueToSharedPref() {
		Global.editor.putString(Constants.prefName, editName.getText()
				.toString());
		Global.editor.putString(Constants.prefPhoneNo, editPhoneNumber
				.getText().toString());
		Global.editor.apply();

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
		editName = (EditText) findViewById(R.id.editName);
		editPhoneNumber = (EditText) findViewById(R.id.editPhone);
		buttonProceed = (Button) findViewById(R.id.buttonProceed);
	}

	@Override
	public void approvedTransaction(String message, String pnr,
			String transaction_type, String invoice_number, String card_type) {
		if (pnr != null) {
			alertMessage = message + "\nPnref: " + pnr + "\nInvoice: "
					+ invoice_number + "\nCard Type: " + card_type;
			showAlert = true;
		} else {
			alertMessage = message;
			showAlert = true;
		}
		Log.i("TestApp", "message1 = " + message);
	}

	@Override
	public void declinedTransaction(String message, String reason) {
		// TODO Auto-generated method stub
		alertMessage = message + "\n" + reason;
		showAlert = true;
		Log.i("TestApp", "message2 = " + message);
	}

	@Override
	public void mostRecentEmailTransaction(String message) {
		// TODO Auto-generated method stub
		alertMessage = message;
		showAlert = true;
		Log.i("TestApp", "message3 = " + message);
	}

	@Override
	public void cancelledTransaction() {
		// TODO Auto-generated method stub
		alertMessage = "Transaction Cancelled";
		showAlert = true;
		Log.i("TestApp", "message4 = Transaction Cancelled");
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (showAlert == true) {
			showAlertDialog();
			showAlert = false;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		anywhere.sdkAdvanced.stopHeadSetService();
	}

}
