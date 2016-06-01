package xziar.contacts.activity;

import java.io.FileNotFoundException;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import xziar.contacts.R;
import xziar.contacts.bean.ContactBean;
import xziar.contacts.util.DBUtil;
import xziar.contacts.util.SystemContactUtil;

public class AddContactActivity extends AppCompatActivity
{
	private ContactBean cb;
	private TextView txt_title;
	private ImageView img_head;
	private Bitmap bmp;
	private EditText txt_name, txt_cel, txt_tel, txt_email, txt_des;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_contact);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle("");
		setSupportActionBar(toolbar);
		ActionBar actbar = getSupportActionBar();
		actbar.setDisplayHomeAsUpEnabled(true);
		{
			txt_title = (TextView) findViewById(R.id.title);
			img_head = (ImageView) findViewById(R.id.contact_head);
			txt_name = (EditText) findViewById(R.id.contact_name);
			txt_cel = (EditText) findViewById(R.id.contact_cel);
			txt_tel = (EditText) findViewById(R.id.contact_tel);
			txt_email = (EditText) findViewById(R.id.contact_email);
			txt_des = (EditText) findViewById(R.id.contact_des);
		}
		bmp = null;
		int ID = getIntent().getIntExtra("ContactBeanID", -1);
		if(ID != -1)
		{
			cb = DBUtil.query(ID);
			initData();
		}
		else
			cb = null;
			
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_add, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case 0x102002c:
			finish();
			break;
		case R.id.action_yes:
			if (txt_name.getText().length() != 0)
			{
				saveData();
				Intent intent = new Intent();
				intent.putExtra("changed", true);
				intent.putExtra("ContactBeanID", cb.getId());
				setResult(RESULT_OK, intent);
				finish();
			}
			break;
		default:
			super.onOptionsItemSelected(item);
		}
		return true;
	}

	private void saveData()
	{
		if (cb != null)
			DBUtil.delete(cb);
		cb = new ContactBean();
		cb.setName(txt_name.getText().toString());
		cb.setCel(txt_cel.getText().toString());
		cb.setTel(txt_tel.getText().toString());
		cb.setEmail(txt_email.getText().toString());
		cb.setDescribe(txt_des.getText().toString());
		cb.setHead(bmp);
		DBUtil.add(cb);
		SystemContactUtil.add(this, cb);
	}

	private void initData()
	{
		txt_title.setText("修改联系人");
		txt_name.setText(cb.getName());
		txt_cel.setText(cb.getCel());
		txt_tel.setText(cb.getTel());
		txt_email.setText(cb.getEmail());
		txt_des.setText(cb.getDescribe());
		bmp = cb.getHead();
		if (bmp != null)
		{
			Log.v("try", "set image");
			img_head.setImageBitmap(bmp);
		}
	}

	public void onClickHead(View view)
	{
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		startActivityForResult(intent, 1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent data)
	{
		if (resultCode == RESULT_OK)
		{
			Uri uri = data.getData();
			Log.e("uri", uri.toString());
			ContentResolver cr = this.getContentResolver();
			try
			{
				bmp = BitmapFactory.decodeStream(cr.openInputStream(uri));
				img_head.setImageBitmap(bmp);
			}
			catch (FileNotFoundException e)
			{
				Log.e("Exception", e.getMessage(), e);
			}
		}
		else if (resultCode == RESULT_CANCELED)
		{
			bmp = null;
			img_head.setImageResource(R.drawable.default_head_rect);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
