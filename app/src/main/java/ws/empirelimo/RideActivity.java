package ws.empirelimo;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.widget.Toast;
import utils.DrawerBaseActivity;
import utils.ViewPageAdapter;

public class RideActivity extends DrawerBaseActivity implements TabListener {

	private ActionBar mActionBar;
	private ViewPager mPager;
	private Tab tab;
	private ViewPageAdapter mAdapter;

	private String[] tabs = { "Upcoming Ride", "Rides" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState, R.layout.activity_ride);

		// Initilization
		mPager = (ViewPager) findViewById(R.id.pager);
		mAdapter = new ViewPageAdapter(getSupportFragmentManager());
		mPager.setAdapter(mAdapter);
		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Adding Tabs
		for (String tab_name : tabs) {
			getSupportActionBar().addTab(
					getSupportActionBar().newTab().setText(tab_name)
							.setTabListener((TabListener) this));
		}

		/**
		 * on swiping the viewpager make respective tab selected
		 * */
		mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				getSupportActionBar().setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// on tab selected
		// show respected fragment view
		mPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}

}