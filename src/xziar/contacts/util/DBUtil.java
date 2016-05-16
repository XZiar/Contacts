package xziar.contacts.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.util.Log;
import xziar.contacts.bean.ContactBean;

public class DBUtil
{
	private static final String dbName = "contacts.db";
	private static final String initSQL = "create table contacts("
			+ "Id integer primary key autoincrement," + "Name text not null,"
			+ "Tel text not null," + "Cel text not null,"
			+ "Email text not null," + "Describe text not null," + "Img BLOB"
			+ ");";
	private static final String insertSQL = "insert into contacts"
			+ "(Name,Tel,Cel,Email,Describe,Img) " + "values(?,?,?,?,?,?)";
	private static final String deleteSQL = "delete from contacts where Id=?";
	private static final String selectAllSQL = "select "
			+ "Id,Name,Tel,Cel,Email,Describe,Img " + "from contacts";
	private static final String selectSQL = "select "
			+ "Id,Name,Tel,Cel,Email,Describe,Img "
			+ "from contacts where Id=?";
	private static SQLiteDatabase db;

	static public void onInit(File dir)
	{
		Log.v("tester", dir.getAbsolutePath());
		db = SQLiteDatabase.openOrCreateDatabase(dir.getAbsolutePath() + dbName,
				null);
		try
		{
			db.execSQL(initSQL);
		}
		catch (SQLException e)
		{
			Log.e("sql", e.getLocalizedMessage());
			delete();
			db.execSQL(initSQL);
		}
	}

	static public void add(ContactBean cb)
	{
		SQLiteStatement stmt = db.compileStatement(insertSQL);
		stmt.bindString(1, cb.getName());
		stmt.bindString(2, cb.getTel());
		stmt.bindString(3, cb.getCel());
		stmt.bindString(4, cb.getEmail());
		stmt.bindString(5, cb.getDescribe());
		Bitmap bmp = cb.getImg();
		if (bmp != null)
		{
			final ByteArrayOutputStream os = new ByteArrayOutputStream();
			bmp.compress(Bitmap.CompressFormat.PNG, 100, os);
			stmt.bindBlob(6, os.toByteArray());
		}
		else
			stmt.bindNull(6);
		try
		{
			long ret = stmt.executeInsert();
			cb.setId((int) ret);
		}
		catch (SQLException e)
		{
			Log.e("sql", e.getLocalizedMessage());
		}
	}

	static public void delete()
	{
		db.execSQL("drop table contacts");
	}

	static public void delete(ContactBean cb)
	{
		SQLiteStatement stmt = db.compileStatement(deleteSQL);
		stmt.bindLong(1, cb.getId());
		try
		{
			int num = stmt.executeUpdateDelete();
			Log.v("tester", "finish delete,affect " + num);
		}
		catch (SQLException e)
		{
			Log.e("sql", e.getLocalizedMessage());
		}
	}

	static public ArrayList<ContactBean> query()
	{
		Cursor cursor = db.rawQuery(selectAllSQL, null);
		return DataInject.CursorToObjs(cursor, ContactBean.class);
	}

	static public ContactBean query(int ID)
	{
		String[] arg = new String[1];
		arg[0] = "" + ID;
		try
		{
			Cursor cursor = db.rawQuery(selectSQL, arg);
			ContactBean cb = new ContactBean();
			if (cursor.moveToFirst())
				DataInject.CursorToObj(cursor, cb);
			else
				cb = null;
			return cb;
		}
		catch (SQLException e)
		{
			Log.e("sql", e.getLocalizedMessage());
			return null;
		}

	}

	static public void onExit()
	{
		if (db != null)
			db.close();
	}
}
