package utils;

import ws.empirelimo.R;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DrawerAdapter extends BaseAdapter {

	public Context context;

	public DrawerAdapter(Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		return Utils.drawerIcons.length;
	}

	@Override
	public Object getItem(int position) {
		return Utils.drawerIcons[position];
	}

	@Override
	public long getItemId(int position) {
		return Utils.drawerIcons[position];
	}

	private class ViewHolder {
		ImageView imageView;
		TextView textView;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.drawerlistelement, null);
			holder = new ViewHolder();
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.drawerIcon);
			holder.textView = (TextView) convertView
					.findViewById(R.id.drawerTitle);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.imageView.setImageResource(Utils.drawerIcons[position]);
		holder.textView.setText(Utils.drawerTitle[position].toUpperCase());
		return convertView;
	}

}
