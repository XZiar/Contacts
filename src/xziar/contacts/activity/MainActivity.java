package xziar.contacts.activity;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.TextView;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import xziar.contacts.R;
import xziar.contacts.bean.ContactBean;
import xziar.contacts.bean.ContactInterface;
import xziar.contacts.util.ContactAdapter;
import xziar.contacts.util.DBUtil;
import xziar.contacts.util.SystemContactUtil;
import xziar.contacts.widget.SideBar;

public class MainActivity extends AppCompatActivity
		implements SideBar.OnTouchingLetterChangedListener, TextWatcher,
		OnItemClickListener
{
	private final static int REQUESTCODE_ADD = 1;
	private final static int REQUESTCODE_INFO = 2;

	private static Context context = null;
	private StickyListHeadersListView mListView;
	private TextView mFooterView;

	private ArrayList<ContactBean> datas = new ArrayList<>();
	private ContactAdapter mAdapter;
	public ContactBean objcb;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		context = getApplicationContext();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle("");
		setSupportActionBar(toolbar);

		initData();
		mListView = (StickyListHeadersListView) findViewById(R.id.mainlist);
		initWidget();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.action_add:
			Intent it = new Intent(this, AddContactActivity.class);
			startActivityForResult(it, REQUESTCODE_ADD);
			break;
		case R.id.action_import:
			ArrayList<ContactBean> cbs = SystemContactUtil.readAll(context);
			for(ContactBean cb : cbs)
			{
				DBUtil.add(cb);
			}
			refreshData();
			break;
		default:
			super.onOptionsItemSelected(item);
		}
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent data)
	{
		if (data != null)
		{
			if (data.getBooleanExtra("changed", false))
			{
				refreshData();
				Log.v("act res", "changed");
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void refreshData()
	{
		datas = DBUtil.query();
		mAdapter.refresh(datas);
		mAdapter.notifyDataSetChanged();
	}
	
	public void initData()
	{
		DBUtil.onInit(getFilesDir());
		ContactBean cb = new ContactBean("Hello");
		DBUtil.add(cb);
		cb = new ContactBean("There");
		DBUtil.add(cb);
		cb = new ContactBean("Here");
		DBUtil.add(cb);
		{
			cb = new ContactBean("P1");
			DBUtil.add(cb);
			cb = new ContactBean("P3");
			DBUtil.add(cb);
			cb = new ContactBean("P5");
			DBUtil.add(cb);
			cb = new ContactBean("QQ");
			DBUtil.add(cb);
			cb = new ContactBean("122");
			DBUtil.add(cb);
			cb = new ContactBean("sca");
			DBUtil.add(cb);
			cb = new ContactBean("下啊房产税");
			DBUtil.add(cb);
			cb = new ContactBean("道非道vf");
			DBUtil.add(cb);
			cb = new ContactBean("俄方vesd");
			DBUtil.add(cb);
			cb = new ContactBean("额few是");
			DBUtil.add(cb);
			cb = new ContactBean("额我few如果");
			DBUtil.add(cb);
			cb = new ContactBean("语句一般");
			DBUtil.add(cb);
		}
		cb = new ContactBean("辣鸡");
		DBUtil.add(cb);
	}

	public void initWidget()
	{
		SideBar mSideBar = (SideBar) findViewById(R.id.sidebar);
		TextView mDialog = (TextView) findViewById(R.id.pinyin_box);
		EditText mSearchInput = (EditText) findViewById(R.id.search_input);

		mSideBar.setTextView(mDialog);
		mSideBar.setOnTouchingLetterChangedListener(this);
		mSearchInput.addTextChangedListener(this);

		// 给listView设置adapter
		mFooterView = (TextView) View.inflate(this,
				R.layout.item_list_contact_count, null);
		mListView.addFooterView(mFooterView);

		mFooterView.setText(datas.size() + "位联系人");
		mAdapter = new ContactAdapter(this);
		refreshData();
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
	}

	private void showInfo(ContactBean cb)
	{
		objcb = cb;
		Intent it = new Intent();
		it.setClass(this, ContactInfoActivity.class);
		it.putExtra("ContactBeanID", cb.getId());
		startActivityForResult(it, REQUESTCODE_INFO);
	}

	@Override
	public void onTouchingLetterChanged(char c)
	{
		int position = 0;
		// 该字母首次出现的位置
		if (mAdapter != null)
		{
			position = mAdapter.getPositionForSection(c);
		}
		if (position != -1)
		{
			mListView.setSelection(position);
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after)
	{

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count)
	{
		ArrayList<ContactInterface> temp = new ArrayList<>();
		for (ContactBean data : datas)
		{
			if (data.isContain(s))
				temp.add(data);
		}
		if (mAdapter != null)
		{
			mAdapter.refresh(temp);
			mAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void afterTextChanged(Editable s)
	{
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id)
	{
		ContactBean cb = (ContactBean) mAdapter.getItem(position);
		if (cb != null)
			showInfo(cb);
	}

	public static Context getContext()
	{
		return context;
	}
}
