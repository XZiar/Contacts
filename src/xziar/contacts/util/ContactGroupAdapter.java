package xziar.contacts.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import xziar.contacts.R;
import xziar.contacts.bean.ContactGroup;

public class ContactGroupAdapter extends BaseSwipeAdapter
{
	public interface OnDeleteItemListener
	{
		public void onDelete(ContactGroup cg);
	}
	
	class ViewHolder implements OnClickListener
	{
		ContactGroupAdapter cga;
		TextView text;
		ImageView delete;
		ContactGroup cg;

		public ViewHolder(ContactGroupAdapter cga, View view)
		{
			this.cga = cga;
			text = (TextView) (view.findViewById(R.id.contact_title));
			delete = (ImageView) (view.findViewById(R.id.cgdelete));
		}

		public void setData(ContactGroup cg)
		{
			this.cg = cg;
			text.setText(cg.getName());
			delete.setOnClickListener(this);
		}

		@Override
		public void onClick(View v)
		{
			cga.delItemListner.onDelete(cg);
		}
	}

	private ArrayList<ContactGroup> datas = new ArrayList<>();
	private LayoutInflater inflater;
	OnDeleteItemListener delItemListner;

	public ContactGroupAdapter(Context context)
	{
		inflater = LayoutInflater.from(context);
	}

	public ContactGroupAdapter(Context context, ArrayList<ContactGroup> _datas)
	{
		inflater = LayoutInflater.from(context);
		refresh(_datas);
	}

	public void setOnDeleteItemListner(OnDeleteItemListener delItemListner)
	{
		this.delItemListner = delItemListner;
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
	public void registerDataSetObserver(DataSetObserver observer)
	{
		super.registerDataSetObserver(observer);
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer)
	{
		super.unregisterDataSetObserver(observer);
	}

	@Override
	public void fillValues(int position, View convertView)
	{
		ViewHolder holder = (ViewHolder) convertView.getTag();
		if (holder == null)
		{
			holder = new ViewHolder(this, convertView);
			convertView.setTag(holder);
		}
		holder.setData(datas.get(position));
	}

	@Override
	public View generateView(int position, ViewGroup parent)
	{
		return inflater.inflate(R.layout.list_contactgroup_item, parent, false);
	}

	@Override
	public int getSwipeLayoutResourceId(int position)
	{
		return R.id.contactgroup_item;
	}
}
