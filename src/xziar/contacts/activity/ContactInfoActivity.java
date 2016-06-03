package xziar.contacts.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import xziar.contacts.R;
import xziar.contacts.bean.ContactBean;
import xziar.contacts.bean.ContactGroup;
import xziar.contacts.util.DBUtil;
import xziar.contacts.widget.ContactInfoItem;

public class ContactInfoActivity extends AppCompatActivity
		implements OnClickListener
{
	private ContactBean cb;
	private String[] strs;
	private ContactGroup[] cgs;
	private LinearLayout ll_cont;
	private TextView txt_gname;
	private NumberPicker np_group;
	private ContactInfoItem cii_cel, cii_tel, cii_email, cii_des;
	private ImageView img_head;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_info);
		cb = MainActivity.objcb;

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle("");
		setSupportActionBar(toolbar);
		ActionBar actbar = getSupportActionBar();
		actbar.setDisplayHomeAsUpEnabled(true);

		txt_gname = (TextView) findViewById(R.id.contact_gname);
		txt_gname.setText(cb.getGroupName());
		txt_gname.setOnClickListener(this);
		TextView name = (TextView) findViewById(R.id.contact_title);
		name.setText(cb.getName());
		ll_cont = (LinearLayout) findViewById(R.id.contact_content);
		img_head = (ImageView) findViewById(R.id.contact_head);

		if (cb.getHead() != null)
			img_head.setImageBitmap(cb.getHead());

		cii_cel = new ContactInfoItem(this, ll_cont, "手机", cb.getCel());
		ll_cont.addView(cii_cel.getLayout());

		cii_tel = new ContactInfoItem(this, ll_cont, "座机", cb.getTel(), true,
				false);
		ll_cont.addView(cii_tel.getLayout());

		cii_email = new ContactInfoItem(this, ll_cont, "Email", cb.getEmail(),
				false, false);
		ll_cont.addView(cii_email.getLayout());

		cii_des = new ContactInfoItem(this, ll_cont, "备注", cb.getDescribe(),
				false, false);
		ll_cont.addView(cii_des.getLayout());

		cgs = DBUtil.groups.values().toArray(new ContactGroup[0]);
		strs = new String[cgs.length];
		int objidx = 0;
		for (int a = 0; a < cgs.length; a++)
		{
			ContactGroup cg = cgs[a];
			if (cb.getGroup() == cg)
				objidx = a;
			strs[a] = cg.getName();
		}
		np_group = new NumberPicker(this);
		np_group.setDisplayedValues(strs);
		np_group.setMinValue(0);
		np_group.setMaxValue(strs.length - 1);
		np_group.setValue(objidx);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case 0x102002c:
			finish();
			break;
		default:
			super.onOptionsItemSelected(item);
		}
		return true;
	}

	@Override
	public void onClick(View v)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("更改 " + cb.getName() + " 的分组");
		builder.setView(np_group);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				DBUtil.addToGroup(cb, cgs[np_group.getValue()]);
			}
		});
		builder.show();
	}
}
