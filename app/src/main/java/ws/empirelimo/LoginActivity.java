package ws.empirelimo;

import utils.ConnectionDetector;
import utils.Constants;
import utils.Global;
import utils.Utils;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.support.v7.app.ActionBarActivity;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener {

	private EditText editEmailAdd, editPassword;
	private TextView textForgotPass;
	private Button buttonLogin, buttonCreateAcc;
	private LinearLayout linearLayoutPass;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		Utils.assignGlobalData(getApplicationContext());
		UiComponent();
		this.context = this;
		textForgotPass.setOnClickListener(this);
		buttonCreateAcc.setOnClickListener(this);
		buttonLogin.setOnClickListener(this);

	}

	private void UiComponent() {
		editEmailAdd = (EditText) findViewById(R.id.editEmail);
		editPassword = (EditText) findViewById(R.id.editPassword);
		textForgotPass = (TextView) findViewById(R.id.textForgotPass);
		buttonLogin = (Button) findViewById(R.id.buttonLogin);
		buttonCreateAcc = (Button) findViewById(R.id.buttonCreateAcc);

	}

	private void storeValueToSharedPref() {
		Global.editor.putString(Constants.prefEmailAdd, editEmailAdd.getText()
				.toString());
		// Global.editor.putString(Constants.prefPassword,
		// editPassword.getText()
		// .toString());
		Global.editor.apply();

	}

	@Override
	public void onClick(View view) {
		if (view == buttonLogin) {
			if (editEmailAdd.getText().toString().length() != 0) {
				if (Patterns.EMAIL_ADDRESS.matcher(
						editEmailAdd.getText().toString()).matches()) {
					storeValueToSharedPref();
					startActivity(new Intent(LoginActivity.this,
							BookRideActivity.class));
					overridePendingTransition(R.anim.slide_out_up,
							R.anim.slide_in_up);
					finish();

				} else {
					Toast.makeText(context, "Invalid Email Address..",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(context, "Enter Email Address..",
						Toast.LENGTH_SHORT).show();
			}

		} else if (view == textForgotPass) {
			Toast.makeText(context, "Forgot Password", Toast.LENGTH_SHORT)
					.show();
		} else {
			Toast.makeText(context, "Create Account", Toast.LENGTH_SHORT)
					.show();
		}
	}
}
