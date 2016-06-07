package xziar.contacts.activity;

import java.util.ArrayList;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
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
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import xziar.contacts.R;
import xziar.contacts.bean.ContactBean;
import xziar.contacts.bean.ContactGroup;
import xziar.contacts.bean.ContactInterface;
import xziar.contacts.util.ContactAdapter;
import xziar.contacts.util.ContactGroupAdapter;
import xziar.contacts.util.ContactGroupAdapter.OnDeleteItemListener;
import xziar.contacts.util.DBUtil;
import xziar.contacts.widget.SideBar;

public class GroupActivity extends AppCompatActivity
		implements SideBar.OnTouchingLetterChangedListener, TextWatcher,
		OnItemClickListener, OnDeleteItemListener
{
	private final static int REQUESTCODE_INFO = 2;

	private TextView title;
	private FrameLayout groupFL, memberFL;
	private StickyListHeadersListView mListView;
	private ListView cgListView;
	private TextView mFooterView;
	private EditText mSearchInput;

	private ContactGroup objcg;
	private ContactAdapter mAdapter;
	private ContactGroupAdapter cgAdapter;
	private boolean isGroup = true;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle("");
		setSupportActionBar(toolbar);
		ActionBar actbar = getSupportActionBar();
		actbar.setDisplayHomeAsUpEnabled(true);

		initData();
		initWidget();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_group, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.action_add:
			addGroup();
			break;
		case 0x102002c:
			if (isGroup)
				finish();
			else
				showGroup(null);
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

	public void addGroup()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("输入新分组的名称");
		final EditText txt = new EditText(this);
		builder.setView(txt);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				DBUtil.addGroup(new ContactGroup(txt.getText().toString()));
				refreshData();
			}
		});
		builder.show();
	}

	public void refreshData()
	{
		if (isGroup)
		{
			cgAdapter.refresh(DBUtil.groups.values());
			cgAdapter.notifyDataSetChanged();
		}
		else
		{
			mFooterView.setText(objcg.getMembers().size() + "位联系人");
			mAdapter.refresh(objcg.getMembers());
			mAdapter.notifyDataSetChanged();
		}
	}

	public void initData()
	{
		cgAdapter = new ContactGroupAdapter(this);
		cgAdapter.setOnDeleteItemListner(this);
		mAdapter = new ContactAdapter(this, false);
		objcg = DBUtil.groups.get(-1);
	}

	public void initWidget()
	{
		title = (TextView) findViewById(R.id.acttitle);
		groupFL = (FrameLayout) findViewById(R.id.allgroup);
		memberFL = (FrameLayout) findViewById(R.id.pergroup);
		mSearchInput = (EditText) findViewById(R.id.search_input);
		mListView = (StickyListHeadersListView) findViewById(R.id.mainlist);
		cgListView = (ListView) findViewById(R.id.grouplist);
		{
			SideBar mSideBar = (SideBar) findViewById(R.id.sidebar);
			TextView mDialog = (TextView) findViewById(R.id.pinyin_box);

			mSideBar.setTextView(mDialog);
			mSideBar.setOnTouchingLetterChangedListener(this);
			mSearchInput.addTextChangedListener(this);

			// 给listView设置adapter
			mFooterView = (TextView) View.inflate(this,
					R.layout.item_list_contact_count, null);
			refreshData();
			mListView.addFooterView(mFooterView);
			mListView.setAdapter(mAdapter);
			mListView.setOnItemClickListener(this);
		}
		{
			cgListView.setAdapter(cgAdapter);
			cgListView.setOnItemClickListener(this);
		}
		memberFL.setVisibility(View.GONE);
		mSearchInput.setVisibility(View.GONE);
	}

	private void showInfo(ContactBean cb)
	{
		MainActivity.objcb = cb;
		Intent it = new Intent(this, ContactInfoActivity.class);
		startActivityForResult(it, REQUESTCODE_INFO);
	}

	private void showGroup(ContactGroup cg)
	{
		if (cg == null)// back
		{
			memberFL.setVisibility(View.GONE);
			mSearchInput.setVisibility(View.GONE);
			groupFL.setVisibility(View.VISIBLE);
			title.setText("通讯录分组");
			isGroup = true;
		}
		else
		{
			groupFL.setVisibility(View.GONE);
			mSearchInput.setVisibility(View.VISIBLE);
			memberFL.setVisibility(View.VISIBLE);
			objcg = cg;
			title.setText(cg.getName());
			isGroup = false;
		}
		refreshData();
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
		for (ContactBean data : objcg.getMembers())
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
		if (parent.getClass() == cgListView.getClass())
		{
			ContactGroup cg = (ContactGroup) cgAdapter.getItem(position);
			if (cg != null)
				showGroup(cg);
		}
		else
		{
			ContactBean cb = (ContactBean) mAdapter.getItem(position);
			if (cb != null)
				showInfo(cb);
		}
	}

	@Override
	public void onDelete(ContactGroup cg)
	{
		DBUtil.deleteGroup(cg);
		refreshData();
	}

}
