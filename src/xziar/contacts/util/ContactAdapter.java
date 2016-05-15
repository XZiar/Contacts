/*
 * Copyright (c) 2015 张涛.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package xziar.contacts.util;

import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import xziar.contacts.R;
import xziar.contacts.bean.ContactInterface;

import org.kymjs.kjframe.KJBitmap;
import org.kymjs.kjframe.widget.AdapterHolder;
import org.kymjs.kjframe.widget.KJAdapter;

import java.util.ArrayList;
import java.util.Collections;

/**
 * 列表适配器
 *
 * @author kymjs (http://www.kymjs.com/) on 9/16/15.
 */
public class ContactAdapter extends KJAdapter<ContactInterface> implements SectionIndexer
{

	private KJBitmap kjb = new KJBitmap();
	private ArrayList<ContactInterface> datas;

	public ContactAdapter(AbsListView view, ArrayList<ContactInterface> mDatas)
	{
		super(view, mDatas, R.layout.item_list_contact);
		datas = mDatas;
		if (datas == null)
		{
			datas = new ArrayList<ContactInterface>();
		}
		Collections.sort(datas);
	}

	@Override
	public void convert(AdapterHolder helper, ContactInterface item, boolean isScrolling)
	{
	}

	@Override
	public void convert(AdapterHolder holder, ContactInterface item, boolean isScrolling,
			int position)
	{

		holder.setText(R.id.contact_title, item.getName());
		ImageView headImg = holder.getView(R.id.contact_head);
		if (isScrolling)
		{
			kjb.displayCacheOrDefult(headImg, item.getImg(),
					R.drawable.default_head_rect);
		}
		else
		{
			kjb.displayWithLoadBitmap(headImg, item.getImg(),
					R.drawable.default_head_rect);
		}

		TextView tvLetter = holder.getView(R.id.contact_catalog);
		TextView tvLine = holder.getView(R.id.contact_line);

		// 如果是第0个那么一定显示#号
		if (position == 0)
		{
			tvLetter.setVisibility(View.VISIBLE);
			tvLetter.setText("" + item.getIndexChar());
			tvLine.setVisibility(View.VISIBLE);
		}
		else
		{
			ContactInterface prevData = datas.get(position - 1);
			if (item.getIndexChar() != prevData.getIndexChar())
			{
				tvLetter.setVisibility(View.VISIBLE);
				tvLetter.setText("" + item.getIndexChar());
				tvLine.setVisibility(View.VISIBLE);
			}
			else
			{
				tvLetter.setVisibility(View.GONE);
				tvLine.setVisibility(View.GONE);
			}
		}
	}

	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ascii值
	 */
	public int getSectionForPosition(int position)
	{
		ContactInterface item = datas.get(position);
		return item.getIndexChar();
	}

	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	public int getPositionForSection(int section)
	{
		for (int i = 0; i < getCount(); i++)
		{
			char firstChar = datas.get(i).getIndexChar();
			if (firstChar == section)
			{
				return i;
			}
		}
		return -1;
	}

	@Override
	public Object[] getSections()
	{
		return null;
	}

}
