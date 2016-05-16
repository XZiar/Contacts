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
import android.widget.ImageView;
import xziar.contacts.R;

public class AddContactActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_contact);
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
