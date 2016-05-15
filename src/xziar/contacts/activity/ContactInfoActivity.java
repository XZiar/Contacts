package xziar.contacts.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import xziar.contacts.R;
import xziar.contacts.bean.ContactBean;

public class ContactInfoActivity extends Activity
{
	private ContactBean cb;
	private LinearLayout ll_cont, ll_cel, ll_tel,ll_email, ll_des;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_info);
		((TextView) findViewById(R.id.contact_title)).setText(cb.getName());
		cb = (ContactBean) getIntent().getSerializableExtra("ContactBean");
		ll_cont = (LinearLayout) findViewById(R.id.contact_content);
		ll_cel = (LinearLayout) LayoutInflater.from(this)
				.inflate(R.layout.contact_info_item, ll_cont, false);
		((TextView) ll_cel.findViewById(R.id.contact_name)).setText("手机");
		((TextView) ll_cel.findViewById(R.id.contact_val)).setText(cb.getCel());
		ll_cont.addView(ll_cel);
		ll_tel = (LinearLayout) LayoutInflater.from(this)
				.inflate(R.layout.contact_info_item, ll_cont, false);
		((TextView) ll_tel.findViewById(R.id.contact_name)).setText("座机");
		((TextView) ll_tel.findViewById(R.id.contact_val)).setText(cb.getTel());
		ll_cont.addView(ll_tel);
		ll_email = (LinearLayout) LayoutInflater.from(this)
				.inflate(R.layout.contact_info_item, ll_cont, false);
		((TextView) ll_tel.findViewById(R.id.contact_name)).setText("Email");
		((TextView) ll_tel.findViewById(R.id.contact_val)).setText(cb.getEmail());
		ll_cont.addView(ll_email);
		ll_des = (LinearLayout) LayoutInflater.from(this)
				.inflate(R.layout.contact_info_item, ll_cont, false);
		((TextView) ll_des.findViewById(R.id.contact_name)).setText("备注");
		((TextView) ll_des.findViewById(R.id.contact_val))
				.setText(cb.getDescribe());
		ll_cont.addView(ll_des);
	}
}
