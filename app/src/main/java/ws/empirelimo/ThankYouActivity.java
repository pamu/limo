package ws.empirelimo;

import utils.Constants;
import utils.Global;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class ThankYouActivity extends ActionBarActivity {

	private TextView textEmail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_thankyou);

		getSupportActionBar().setIcon(
				new ColorDrawable(getResources().getColor(
						android.R.color.transparent)));

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		textEmail = (TextView) findViewById(R.id.textEmail);
		textEmail.setText("Check Your email "
				+ Global.sharedPreferences.getString(Constants.prefEmailAdd,
						"abc@xyz.com") + " for your journey information ");
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			startActivity(new Intent(ThankYouActivity.this,
					BookRideActivity.class));
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
