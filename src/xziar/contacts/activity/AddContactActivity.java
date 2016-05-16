package xziar.contacts.activity;

import java.io.FileNotFoundException;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import xziar.contacts.R;
import xziar.contacts.bean.ContactBean;

public class AddContactActivity extends Activity
{
	private ContactBean cb;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_contact);
		cb = (ContactBean) getIntent().getSerializableExtra("ContactBean");
		if (cb != null)
			initData();
	}

	private void initData()
	{
		((TextView) findViewById(R.id.title)).setText("修改联系人");
		((EditText) findViewById(R.id.contact_name)).setText(cb.getName());
		((EditText) findViewById(R.id.contact_cel)).setText(cb.getCel());
		((EditText) findViewById(R.id.contact_tel)).setText(cb.getTel());
		((EditText) findViewById(R.id.contact_email))
				.setText(cb.getEmail());
		((EditText) findViewById(R.id.contact_des))
				.setText(cb.getDescribe());
		if (cb.getImg() != null)
		{
			((ImageView) findViewById(R.id.contact_name)).setImageBitmap(cb.getImg());
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
				Bitmap bitmap = BitmapFactory
						.decodeStream(cr.openInputStream(uri));
				ImageView imageView = (ImageView) findViewById(
						R.id.contact_head);
				/* 将Bitmap设定到ImageView */
				imageView.setImageBitmap(bitmap);
			}
			catch (FileNotFoundException e)
			{
				Log.e("Exception", e.getMessage(), e);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
