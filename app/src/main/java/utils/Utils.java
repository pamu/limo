package utils;

import com.google.android.gms.maps.GoogleMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import ws.empirelimo.BookRideActivity;
import ws.empirelimo.R;
import ws.empirelimo.ConfirmationActivity;

public class Utils {
	public static int[] drawerIcons = { R.drawable.ic_drawer_bookaride,
			R.drawable.ic_drawer_myride, R.drawable.ic_drawer_payment,
			R.drawable.ic_drawer_fare, R.drawable.ic_drawer_profile,
			R.drawable.ic_drawer_contactus };

	public static String[] drawerTitle = { "Book A Ride", "My Rides",
			"Payment Methods", "Fare Chart", "Profile", "Contact Us" };

	static TimePicker timePicker;
	static DatePicker datePicker;

	public static void assignGlobalData(Context context) {

		Global.connectionDetector = new ConnectionDetector(context);
		Global.sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		Global.editor = Global.sharedPreferences.edit();

	}
}
