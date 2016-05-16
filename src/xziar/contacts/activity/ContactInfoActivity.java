package xziar.contacts.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_info);
		int ID = getIntent().getIntExtra("ContactBeanID", 0);
		cb = DBUtil.query(ID);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle("");
		setSupportActionBar(toolbar);
		ActionBar actbar = getSupportActionBar();
		actbar.setDisplayHomeAsUpEnabled(true);

		TextView name = (TextView) findViewById(R.id.contact_title);
		name.setText(cb.getName());
		ll_cont = (LinearLayout) findViewById(R.id.contact_content);

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
}
