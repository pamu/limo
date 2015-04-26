package utils;

import ws.empirelimo.BookRideActivity;
import ws.empirelimo.RideActivity;
import ws.empirelimo.ConfirmationActivity;
import ws.empirelimo.R;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class DrawerBaseActivity extends ActionBarActivity {

	public DrawerLayout navDrawer;
	public ListView navList;
	public ActionBarDrawerToggle mDrawerToggle;
	public CharSequence mDrawerTitle;
	public CharSequence mTitle;
	public String[] navMenuTitles;

	protected void onCreate(Bundle savedInstanceState, int resLayoutID) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setIcon(
				new ColorDrawable(getResources().getColor(
						android.R.color.transparent)));

		setContentView(resLayoutID);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		navMenuTitles = Utils.drawerTitle;
		mTitle = mDrawerTitle = getTitle();

		navDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		navList = (ListView) findViewById(R.id.drawerList);
		DrawerAdapter drawerAdapter = new DrawerAdapter(this);
		navList.setAdapter(drawerAdapter);

		mDrawerToggle = new ActionBarDrawerToggle(this, navDrawer,
				R.drawable.ic_action_navigation_drawer, // nav menu toggle icon
				R.string.app_name, // nav drawer open - description for
									// accessibility
				R.string.app_name // nav drawer close - description for
									// accessibility
		) {
			public void onDrawerClosed(View view) {
				// getSupportActionBar().setTitle(navMenuTitles[0].toUpperCase());
				getSupportActionBar().setTitle(getTitle());
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				// getSupportActionBar().setTitle(
				// mDrawerTitle.toString().toUpperCase());
				getSupportActionBar().setTitle(R.string.app_name);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		navDrawer.setDrawerListener(mDrawerToggle);

		navList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					final int position, long arg3) {
				navDrawer.closeDrawer(navList);
				setTitle(navMenuTitles[position].toUpperCase());
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						setActivityList(position);
					}
				}, 250);

			}
		});
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = navDrawer.isDrawerOpen(navList);
		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getSupportActionBar().setTitle(mTitle);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) {
		case R.id.action_settings:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	private void setActivityList(int position) {

		switch (position) {
		case 0:
			startActivity(new Intent(this, BookRideActivity.class));
			overridePendingTransition(R.anim.slide_out_up, R.anim.slide_in_up);
			break;
		case 1:
			startActivity(new Intent(this, RideActivity.class));
			overridePendingTransition(R.anim.slide_out_up, R.anim.slide_in_up);
			break;
		case 2:
			// startActivity(new Intent(this, ThankYouActivity.class));
			// overridePendingTransition(R.anim.slide_out_up,
			// R.anim.slide_in_up);
			break;
		case 3:
			// startActivity(new Intent(this, ThankYouActivity.class));
			// overridePendingTransition(R.anim.slide_out_up,
			// R.anim.slide_in_up);
			break;
		case 4:
			// startActivity(new Intent(this, ThankYouActivity.class));
			// overridePendingTransition(R.anim.slide_out_up,
			// R.anim.slide_in_up);
			break;

		}

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

}
