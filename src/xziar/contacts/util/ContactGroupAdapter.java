package xziar.contacts.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import xziar.contacts.R;
import xziar.contacts.bean.ContactGroup;

public class ContactGroupAdapter extends BaseAdapter
{
	class ViewHolder
	{
		ImageView img;
		TextView text;
		TextView group;

		public ViewHolder(View view)
		{
			img = (ImageView) (view.findViewById(R.id.contact_head));
			text = (TextView) (view.findViewById(R.id.contact_title));
			group = (TextView) (view.findViewById(R.id.contact_group));
		}

		public void setData(ContactGroup cg)
		{
			text.setText(cg.getName());
			group.setText("");
			img.setImageResource(R.drawable.icon_group);
		}
	}

	private ArrayList<ContactGroup> datas = new ArrayList<>();
	private LayoutInflater inflater;

	public ContactGroupAdapter(Context context)
	{
		inflater = LayoutInflater.from(context);
	}

	public ContactGroupAdapter(Context context, ArrayList<ContactGroup> _datas)
	{
		inflater = LayoutInflater.from(context);
		refresh(_datas);
	}

	public <T> void refresh(Collection<ContactGroup> _datas)
	{
		datas.clear();
		datas.addAll(_datas);
		Collections.sort(datas);
		notifyDataSetChanged();
	}

	@Override
	public boolean areAllItemsEnabled()
	{
		return false;
	}

	@Override
	public boolean isEnabled(int position)
	{
		return true;
	}

	@Override
	public int getCount()
	{
		return datas.size();
	}

	@Override
	public Object getItem(int position)
	{
		if (position < datas.size())
			return datas.get(position);
		else
			return null;
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public boolean hasStableIds()
	{
		return true;
	}

	@Override
	public int getItemViewType(int position)
	{
		return 0;
	}

	@Override
	public int getViewTypeCount()
	{
		return 1;
	}

	@Override
	public boolean isEmpty()
	{
		return datas.size() == 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder;

		if (convertView == null)
		{
			convertView = inflater.inflate(R.layout.list_contact_item, parent,
					false);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.setData(datas.get(position));

		return convertView;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer)
	{
		super.registerDataSetObserver(observer);
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer)
	{
		super.unregisterDataSetObserver(observer);
	}
}
