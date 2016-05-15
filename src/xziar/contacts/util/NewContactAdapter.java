package xziar.contacts.util;

import java.util.ArrayList;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import xziar.contacts.R;
import xziar.contacts.bean.ContactInterface;

public class NewContactAdapter<T> extends BaseAdapter
		implements StickyListHeadersAdapter, SectionIndexer
{
	class ViewHolder
	{
		ImageView img;
		TextView text;

		public ViewHolder(View view)
		{
			text = (TextView) (view.findViewById(R.id.contact_title));
			img = (ImageView) (view.findViewById(R.id.contact_head));
		}
	}

	private ArrayList<T> datas;
	private LayoutInflater inflater;

	public NewContactAdapter(Context context, ArrayList<T> _datas)
	{
		inflater = LayoutInflater.from(context);
		datas = _datas;
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
		return datas.get(position);
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
		ContactInterface ci = (ContactInterface) datas.get(position);
		holder.text.setText(ci.getName());
		// Log.v("err", "null tag:" + convertView.getClass().getName());
		return convertView;
	}

	@Override
	public View getHeaderView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = inflater.inflate(R.layout.list_contact_header, parent,
					false);
		}
		ContactInterface ci = (ContactInterface) datas.get(position);
		((TextView) convertView).setText(String.valueOf(ci.getIndexChar()));
		return convertView;
	}

	@Override
	public long getHeaderId(int position)
	{
		ContactInterface ci = (ContactInterface) datas.get(position);
		return ci.getIndexChar();
	}

	@Override
	public Object[] getSections()
	{
		return null;
	}

	@Override
	public int getPositionForSection(int sectionIndex)
	{
		for (int i = 0; i < getCount(); i++)
		{
			ContactInterface ci = (ContactInterface) datas.get(i);
			if (sectionIndex == ci.getIndexChar())
			{
				return i;
			}
		}
		return 0;
	}

	@Override
	public int getSectionForPosition(int position)
	{
		ContactInterface ci = (ContactInterface) datas.get(position);
		return ci.getIndexChar();
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer)
	{
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer)
	{
	}
}
