package xziar.contacts.util;

import java.util.ArrayList;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.*;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;
import xziar.contacts.bean.ContactBean;

public class SystemContactUtil
{
	private static final String[] paramPhoto = new String[] { Photo.PHOTO };
	private static final String[] paramPeople = new String[] { Data.MIMETYPE,
			"data1", "data2" };

	public static int add(Context context, ContactBean cb)
	{
		// Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
		ContentResolver resolver = context.getContentResolver();
		ArrayList<ContentProviderOperation> operations = new ArrayList<>();
		operations
				.add(ContentProviderOperation.newInsert(RawContacts.CONTENT_URI)
						.withValue(RawContacts.ACCOUNT_NAME, null).build());

		// uri = Uri.parse("content://com.android.contacts/data");
		// name
		operations
				.add(ContentProviderOperation.newInsert(Data.CONTENT_URI)
						.withValueBackReference(Data.RAW_CONTACT_ID, 0)
						.withValue(Data.MIMETYPE,
								StructuredName.CONTENT_ITEM_TYPE)
						.withValue("data1", cb.getName()).build());
		// phone
		operations.add(ContentProviderOperation.newInsert(Data.CONTENT_URI)
				.withValueBackReference(Data.RAW_CONTACT_ID, 0)
				.withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
				.withValue("data1", cb.getCel())
				.withValue("data2", Phone.TYPE_MOBILE).build());
		operations.add(ContentProviderOperation.newInsert(Data.CONTENT_URI)
				.withValueBackReference(Data.RAW_CONTACT_ID, 0)
				.withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
				.withValue("data1", cb.getTel())
				.withValue("data2", Phone.TYPE_HOME).build());
		// email
		operations.add(ContentProviderOperation.newInsert(Data.CONTENT_URI)
				.withValueBackReference(Data.RAW_CONTACT_ID, 0)
				.withValue(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE)
				.withValue("data1", cb.getEmail()).build());
		// descibe
		operations.add(ContentProviderOperation.newInsert(Data.CONTENT_URI)
				.withValueBackReference(Data.RAW_CONTACT_ID, 0)
				.withValue(Data.MIMETYPE, Note.CONTENT_ITEM_TYPE)
				.withValue("data1", cb.getDescribe()).build());
		// photo
		operations.add(ContentProviderOperation.newInsert(Data.CONTENT_URI)
				.withValueBackReference(Data.RAW_CONTACT_ID, 0)
				.withValue(Data.MIMETYPE, Photo.CONTENT_ITEM_TYPE)
				.withValue(Photo.PHOTO, cb.getImg()).build());

		try
		{
			resolver.applyBatch(ContactsContract.AUTHORITY, operations);
		}
		catch (RemoteException | OperationApplicationException e)
		{
			e.printStackTrace();
		}
		return 0;
	}

	public static ArrayList<ContactBean> readAll(Context context)
	{
		ArrayList<ContactBean> cbs = new ArrayList<>();
		Uri uri = Uri.parse("content://com.android.contacts/contacts"); // 访问所有联系人
		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver.query(uri, new String[] { "_id" }, null, null,
				null);
		while (cursor.moveToNext())
		{
			int contactsId = cursor.getInt(0);
			uri = Uri.parse("content://com.android.contacts/contacts/"
					+ contactsId + "/data"); // 某个联系人下面的所有数据
			Cursor dataCursor = resolver.query(uri, paramPeople, null, null,
					null);
			ContactBean cb = new ContactBean();
			// load head photo
			Uri photoUri = Uri.withAppendedPath(
					ContentUris.withAppendedId(
							ContactsContract.Contacts.CONTENT_URI, contactsId),
					ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
			Log.v("photo", photoUri.toString());
			Cursor pc = resolver.query(photoUri, paramPhoto, null, null, null);
			if (pc != null && pc.getCount() > 0)
			{
				if (pc.moveToFirst())
				{
					try
					{
						cb.setImg(pc.getBlob(0));
						pc.close();
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
				pc.close();
			}
			// load other imformation
			while (dataCursor.moveToNext())
			{
				String data = dataCursor
						.getString(dataCursor.getColumnIndex("data1"));
				String type = dataCursor
						.getString(dataCursor.getColumnIndex("mimetype"));
				switch (type)
				{
				case Email.CONTENT_ITEM_TYPE:
					cb.setEmail(data);
					break;
				case StructuredName.CONTENT_ITEM_TYPE:
					cb.setName(data);
					break;
				case Phone.CONTENT_ITEM_TYPE:
					int pt = dataCursor
							.getInt(dataCursor.getColumnIndex("data2"));
					if (pt == Phone.TYPE_MOBILE)
						cb.setCel(data);
					else if (pt == Phone.TYPE_HOME)
						cb.setTel(data);
					break;
				case Note.CONTENT_ITEM_TYPE:
					cb.setDescribe(data);
					break;
				}
			}
			cbs.add(cb);
			dataCursor.close();
		}
		cursor.close();
		return cbs;
	}
}
