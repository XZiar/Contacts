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
import android.widget.SectionIndexer;
import android.widget.TextView;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import xziar.contacts.R;
import xziar.contacts.bean.ContactInterface;

public class ContactAdapter extends BaseAdapter
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

	private ArrayList<ContactInterface> datas = new ArrayList<>();
	private LayoutInflater inflater;

	public ContactAdapter(Context context)
	{
		inflater = LayoutInflater.from(context);
	}

	public ContactAdapter(Context context, ArrayList<ContactInterface> _datas)
	{
		inflater = LayoutInflater.from(context);
		refresh(_datas);
	}

	public <T> void refresh(Collection<? extends ContactInterface> _datas)
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
		ContactInterface ci = datas.get(position);
		holder.text.setText(ci.getName());
		if (ci.getImg() != null)
			holder.img.setImageBitmap(ci.getImg());
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
		((TextView) convertView)
				.setText(String.valueOf(datas.get(position).getIndexChar()));
		return convertView;
	}

	@Override
	public long getHeaderId(int position)
	{
		return datas.get(position).getIndexChar();
	}

	@Override
	public Object[] getSections()
	{
		return null;
	}

	@Override
	public int getPositionForSection(int sectionIndex)
	{
		for (int i = 0; i < datas.size(); i++)
		{
			if (sectionIndex == datas.get(i).getIndexChar())
			{
				return i;
			}
		}
		return -1;
	}

	@Override
	public int getSectionForPosition(int position)
	{
		return datas.get(position).getIndexChar();
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
