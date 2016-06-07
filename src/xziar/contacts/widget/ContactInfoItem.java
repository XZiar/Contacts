package xziar.contacts.widget;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import xziar.contacts.R;

public class ContactInfoItem
{
	private LinearLayout layout;
	private TextView content;

	public LinearLayout getLayout()
	{
		return layout;
	}

	public ContactInfoItem(Context context, ViewGroup parent, String name,
			String val)
	{
		this(context, parent, name, val, true, true);
	}

	public ContactInfoItem(final Context context, ViewGroup parent, String name,
			final String val, boolean showBtnCall, boolean showBtnMsg)
	{

		layout = (LinearLayout) LayoutInflater.from(context)
				.inflate(R.layout.contact_info_item, parent, false);
		((TextView) layout.findViewById(R.id.contact_name)).setText(name);
		content = (TextView) layout.findViewById(R.id.contact_val);
		content.setText(val);
		ImageButton call = ((ImageButton) layout
				.findViewById(R.id.contact_call));
		ImageButton msg = ((ImageButton) layout.findViewById(R.id.contact_msg));
		if (showBtnCall)
		{
			call.setOnClickListener(new OnClickListener()
			{
				private Intent phoneIntent = new Intent(Intent.ACTION_CALL,
						Uri.parse("tel:" + val));

				@Override
				public void onClick(View v)
				{
					context.startActivity(phoneIntent);
				}
			});
		}
		else
			call.setVisibility(View.GONE);
		if (showBtnMsg)
		{
			msg.setOnClickListener(new OnClickListener()
			{
				private Intent msgIntent = new Intent(Intent.ACTION_SENDTO,
						Uri.parse("smsto:" + val));

				@Override
				public void onClick(View v)
				{
					context.startActivity(msgIntent);
				}
			});
		}
		else
			msg.setVisibility(View.GONE);
	}

	public void setVal(String val)
	{
		content.setText(val);
	}
	
	public void setOnClickListener(OnClickListener l)
	{
		content.setOnClickListener(l);
	}
}
