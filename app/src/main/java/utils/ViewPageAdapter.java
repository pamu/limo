package utils;

import ws.empirelimo.CompletedFragment;
import ws.empirelimo.UpcomingFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPageAdapter extends FragmentPagerAdapter {

	// Declare the number of ViewPager pages
	final int PAGE_COUNT = 2;

	public ViewPageAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int arg0) {
		switch (arg0) {

		// Open UpcomingFragment.java
		case 0:
			return new UpcomingFragment();

			// Open CompletedFragment.java
		case 1:
			return new CompletedFragment();

		}
		return null;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return PAGE_COUNT;
	}

}