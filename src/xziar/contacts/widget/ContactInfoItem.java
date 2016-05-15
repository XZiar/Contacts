package xziar.contacts.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import xziar.contacts.R;

public class ContactInfoItem
{
	private LinearLayout layout;

	public LinearLayout getLayout()
	{
		return layout;
	}

	public ContactInfoItem(Context context, ViewGroup parent, String name,
			String val)
	{
		layout = (LinearLayout) LayoutInflater.from(context)
				.inflate(R.layout.contact_info_item, parent, false);
		((TextView) layout.findViewById(R.id.contact_name)).setText(name);
		((TextView) layout.findViewById(R.id.contact_val)).setText(val);
	}

	public ContactInfoItem(Context context, ViewGroup parent, String name,
			String val, boolean showBtn)
	{
		this(context, parent, name, val);
		if (!showBtn)
		{
			ImageButton call = ((ImageButton) layout.findViewById(R.id.contact_call));
			call.setVisibility(View.GONE);
			ImageButton msg = ((ImageButton) layout.findViewById(R.id.contact_msg));
			msg.setVisibility(View.GONE);
		}
	}
}
