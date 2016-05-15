package xziar.contacts.activity;

import java.util.ArrayList;

import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.ui.BindView;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import xziar.contacts.R;
import xziar.contacts.bean.ContactBean;
import xziar.contacts.bean.ContactInterface;
import xziar.contacts.util.ContactAdapter;
import xziar.contacts.util.DBUtil;
import xziar.contacts.widget.SideBar;

public class MainActivity extends KJActivity
		implements SideBar.OnTouchingLetterChangedListener, TextWatcher, OnItemClickListener
{

	@BindView(id = R.id.school_friend_member)
	private ListView mListView;
	private TextView mFooterView;

	private ArrayList<ContactBean> datas = new ArrayList<>();
	private ContactAdapter mAdapter;

	@Override
	public void setRootView()
	{
		setContentView(R.layout.activity_main);
	}

	@Override
	public void initData()
	{
		super.initData();
		DBUtil.onInit(getFilesDir());
		ContactBean cb = new ContactBean("Hello");
		DBUtil.add(cb);
		cb = new ContactBean("There");
		DBUtil.add(cb);
		cb = new ContactBean("辣鸡");
		DBUtil.add(cb);
		datas = DBUtil.query();
	}

	@Override
	public void initWidget()
	{
		super.initWidget();
		SideBar mSideBar = (SideBar) findViewById(R.id.school_friend_sidrbar);
		TextView mDialog = (TextView) findViewById(R.id.school_friend_dialog);
		EditText mSearchInput = (EditText) findViewById(
				R.id.school_friend_member_search_input);

		mSideBar.setTextView(mDialog);
		mSideBar.setOnTouchingLetterChangedListener(this);
		mSearchInput.addTextChangedListener(this);

		// 给listView设置adapter
		mFooterView = (TextView) View.inflate(aty,
				R.layout.item_list_contact_count, null);
		mListView.addFooterView(mFooterView);

		mFooterView.setText(datas.size() + "位联系人");
		ArrayList<ContactInterface> conts = new ArrayList<ContactInterface>(datas);
		mAdapter = new ContactAdapter(mListView, conts);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
	}

	private void showInfo()
	{
		Intent it = new Intent();
		it.setClass(this, ContactInfoActivity.class);
		startActivityForResult(it, 1);
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
		for (ContactInterface data : datas)
		{
			if(data.isContain(s))
				temp.add(data);
		}
		if (mAdapter != null)
		{
			mAdapter.refresh(temp);
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
		Toast.makeText(this, "pos:"+position, Toast.LENGTH_SHORT).show();
		
	}
}
