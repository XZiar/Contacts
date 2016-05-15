package xziar.contacts.util;

import java.io.File;
import java.util.ArrayList;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import xziar.contacts.bean.ContactBean;

public class DBUtil
{
	private static final String dbName = "contacts.db";
	private static final String initSQL = "create table contacts("
			+ "ID integer primary key autoincrement," 
			+ "Name text not null,"
			+ "Tel text not null," 
			+ "Cel text not null,"
			+ "Email text not null,"
			+ "Describe text not null," 
			+ "Img BLOB" 
			+ ");";
	private static final String insertSQL = "insert into contacts"
			+ "(Name,Tel,Cel,Email,Describe) "
			+ "values(?,?,?,?,?)";
	private static final String deleteSQL = "delete from contacts where ID=?";
	private static final String selectAllSQL = "select "
			+ "ID,Name,Tel,Cel,Email,Describe,Img "
			+ "from contacts";
	private static SQLiteDatabase db;

	static public void onInit(File dir)
	{
		Log.v("tester", dir.getAbsolutePath());
		db = SQLiteDatabase.openOrCreateDatabase(dir.getAbsolutePath() + dbName, null);
		try
		{
			db.execSQL(initSQL);
		}
		catch(SQLException e)
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

		try
		{
			long ret = stmt.executeInsert();
			cb.setId((int) ret);
		}
		catch(SQLException e)
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
			Log.v("tester", "finish delete,affect "+num);
		}
		catch(SQLException e)
		{
			Log.e("sql", e.getLocalizedMessage());
		}
	}
	
	static public ArrayList<ContactBean> query()
	{
		Cursor cursor = db.rawQuery(selectAllSQL, null);
		return DataInject.CursorToObjs(cursor, ContactBean.class);
	}
	
	static public void onExit()
	{
		if (db != null)
			db.close();
	}
}
