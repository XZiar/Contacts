package xziar.contacts.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import xziar.contacts.bean.ContactBean;
import xziar.contacts.bean.ContactGroup;

public class DBUtil
{
	private static final String dbName = "contacts.db";
	private static final String SQL_initGroup = "create table groups("
			+ "Id integer primary key," + "Name text not null)";
	private static final String SQL_initMapping = "create table mappings("
			+ "Pid integer primary key," + "Gid integer not null)";
	private static final String SQL_insertGroup = "insert into groups"
			+ "(Id,Name) " + "values(?,?)";
	private static final String SQL_insertMapping = "insert into mappings"
			+ "(Id,Name) " + "values(?,?)";
	private static final String SQL_deleteGroup = "delete from groups where Id=?";
	private static final String SQL_deleteMapping = "delete from mappings where Pid=?";
	private static final String SQL_deleteMappings = "delete from mappings where Gid=?";
	private static final String SQL_selectGroupAll = "select " + "Id,Name "
			+ "from groups ";
	private static final String SQL_selectMappingAll = "select " + "Pid,Gid "
			+ "from mappings ";
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
		Log.v("database", dir.getAbsolutePath());
		db = SQLiteDatabase.openOrCreateDatabase(dir.getAbsolutePath() + dbName,
				null);
		try
		{
			db.execSQL(SQL_initGroup);
			db.execSQL(SQL_initMapping);
		}
		catch (SQLException e)
		{
			Log.e("sql", e.getLocalizedMessage());
			delete();
			db.execSQL(SQL_initGroup);
			db.execSQL(SQL_initMapping);
		}
	}

	static public HashMap<Integer, ContactGroup> readGroup(
			ArrayList<ContactBean> cbs)
	{
		Cursor cursor = db.rawQuery(SQL_selectGroupAll, null);
		ArrayList<ContactGroup> groups = DataInject.CursorToObjs(cursor,
				ContactGroup.class);
		cursor.close();
		ContactGroup ncb = new ContactGroup("Î´·Ö×é");
		groups.add(ncb);
		HashMap<Integer, ContactGroup> cgs = new HashMap<>();
		for (ContactGroup cg : groups)
		{
			cgs.put(cg.getGid(), cg);
		}
		HashMap<Integer, Integer> mapping = new HashMap<>();

		cursor = db.rawQuery(SQL_selectMappingAll, null);
		cursor.moveToFirst();
		while (cursor.moveToNext())
		{
			mapping.put(cursor.getInt(0), cursor.getInt(1));
		}
		cursor.close();
		for (ContactBean cb : cbs)
		{
			int pid = cb.getId();
			if (mapping.containsKey(pid))
			{
				ContactGroup cg = cgs.get(mapping.get(pid));
				cg.addMembers(cb);
				cb.setGroup(cg);
			}
			else
			{
				cgs.get(-1).addMembers(cb);
				cb.setGroup(ncb);
			}
		}
		return cgs;
	}

	static public void add(ContactBean cb)
	{
		SQLiteStatement stmt = db.compileStatement(insertSQL);
		stmt.bindString(1, cb.getName());
		stmt.bindString(2, cb.getTel());
		stmt.bindString(3, cb.getCel());
		stmt.bindString(4, cb.getEmail());
		stmt.bindString(5, cb.getDescribe());
		byte[] img = cb.getImg();
		if (img != null)
		{
			stmt.bindBlob(6, img);
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
		// db.execSQL("drop table contacts");
		db.execSQL("drop table groups");
		db.execSQL("drop table mappings");
	}

	static public void deleteGroup(ContactGroup cg)
	{
		SQLiteStatement stmt1 = db.compileStatement(SQL_deleteMappings);
		SQLiteStatement stmt2 = db.compileStatement(SQL_deleteGroup);
		stmt1.bindLong(1, cg.getGid());
		stmt2.bindLong(1, cg.getGid());
		try
		{
			int num = stmt1.executeUpdateDelete();
			stmt2.executeUpdateDelete();
			Log.v("database", "finish delete,affect " + num);
		}
		catch (SQLException e)
		{
			Log.e("sql", e.getLocalizedMessage());
		}
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
		ArrayList<ContactBean> cbs = DataInject.CursorToObjs(cursor,
				ContactBean.class);
		cursor.close();
		return cbs;
	}

	static public ContactBean query(int ID)
	{
		String[] arg = new String[] { "" + ID };
		Cursor cursor = db.rawQuery(selectSQL, arg);
		try
		{
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
		finally
		{
			cursor.close();
		}
	}

	static public void onExit()
	{
		if (db != null)
			db.close();
	}
}
