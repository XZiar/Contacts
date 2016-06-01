package xziar.contacts.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import xziar.contacts.R;
import xziar.contacts.bean.ContactBean;
import xziar.contacts.util.DBUtil;
import xziar.contacts.widget.ContactInfoItem;

public class ContactInfoActivity extends AppCompatActivity
{
	private ContactBean cb;
	private LinearLayout ll_cont;
	private ContactInfoItem cii_cel, cii_tel, cii_email, cii_des;
	private ImageView img_head;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_info);
		int ID = getIntent().getIntExtra("ContactBeanID", -1);
		if (ID != -1)
			cb = DBUtil.query(ID);
		else
			cb = null;

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle("");
		setSupportActionBar(toolbar);
		ActionBar actbar = getSupportActionBar();
		actbar.setDisplayHomeAsUpEnabled(true);

		TextView name = (TextView) findViewById(R.id.contact_title);
		name.setText(cb.getName());
		ll_cont = (LinearLayout) findViewById(R.id.contact_content);
		img_head = (ImageView) findViewById(R.id.contact_head);

		if (cb.getHead() != null)
			img_head.setImageBitmap(cb.getHead());

		cii_cel = new ContactInfoItem(this, ll_cont, "�ֻ�", cb.getCel());
		ll_cont.addView(cii_cel.getLayout());

		cii_tel = new ContactInfoItem(this, ll_cont, "����", cb.getTel(), true,
				false);
		ll_cont.addView(cii_tel.getLayout());

		cii_email = new ContactInfoItem(this, ll_cont, "Email", cb.getEmail(),
				false, false);
		ll_cont.addView(cii_email.getLayout());

		cii_des = new ContactInfoItem(this, ll_cont, "��ע", cb.getDescribe(),
				false, false);
		ll_cont.addView(cii_des.getLayout());

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
}
