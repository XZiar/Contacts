package xziar.contacts.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import xziar.contacts.activity.MainActivity;
import xziar.contacts.bean.ContactBean;
import xziar.contacts.bean.ContactGroup;

public class DBUtil
{
	private static final String dbName = "contacts.db";
	private static final String SQL_initGroup = "create table groups("
			+ "Id integer primary key autoincrement," + "Name text not null)";
	private static final String SQL_initMapping = "create table mappings("
			+ "Pid integer primary key," + "Gid integer not null)";
	private static final String SQL_insertGroup = "insert into groups"
			+ "(Name) " + "values(?)";
	private static final String SQL_insertMapping = "insert into mappings"
			+ "(Pid,Gid) " + "values(?,?)";
	private static final String SQL_deleteGroup = "delete from groups where Id=?";
	private static final String SQL_deleteMapping = "delete from mappings where Pid=?";
	private static final String SQL_deleteMappings = "delete from mappings where Gid=?";
	private static final String SQL_selectGroupAll = "select " + "Id,Name "
			+ "from groups ";
	private static final String SQL_selectMappingAll = "select " + "Pid,Gid "
			+ "from mappings ";
	private static SQLiteDatabase db;

	public static ArrayList<ContactBean> people;
	public static HashMap<Integer, ContactGroup> groups = new HashMap<>();

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

	static public void initData()
	{
		people = SystemContactUtil.readAll(MainActivity.getContext());

		Cursor cursor = db.rawQuery(SQL_selectGroupAll, null);
		ArrayList<ContactGroup> cgs = DataInject.CursorToObjs(cursor,
				ContactGroup.class);
		cursor.close();
		ContactGroup ncb = new ContactGroup("Î´·Ö×é");
		cgs.add(ncb);
		groups.clear();
		for (ContactGroup cg : cgs)
		{
			groups.put(cg.getGid(), cg);
		}

		HashMap<Integer, Integer> mapping = new HashMap<>();
		cursor = db.rawQuery(SQL_selectMappingAll, null);
		cursor.moveToFirst();
		while (cursor.moveToNext())
		{
			mapping.put(cursor.getInt(0), cursor.getInt(1));
		}
		cursor.close();

		for (ContactBean cb : people)
		{
			int pid = cb.getId();
			if (mapping.containsKey(pid))
			{
				ContactGroup cg = groups.get(mapping.get(pid));
				cg.addMembers(cb);
				cb.setGroup(cg);
			}
			else
			{
				groups.get(-1).addMembers(cb);
				cb.setGroup(ncb);
			}
		}
		return;
	}

	static public void addGroup(ContactGroup cg)
	{
		SQLiteStatement stmt = db.compileStatement(SQL_insertGroup);
		stmt.bindString(1, cg.getName());
		try
		{
			int ret = (int) stmt.executeInsert();
			cg.setGid(ret);
			groups.put(ret, cg);
		}
		catch (SQLException e)
		{
			Log.e("sql", e.getLocalizedMessage());
		}
	}

	static public void addToGroup(ContactBean cb, ContactGroup cg)
	{
		ContactGroup ocg = cb.getGroup();
		if (ocg != null)
		{
			if (ocg != groups.get(-1))
			{
				SQLiteStatement stmt = db.compileStatement(SQL_deleteMapping);
				stmt.bindLong(1, cb.getId());
				try
				{
					stmt.executeInsert();
				}
				catch (SQLException e)
				{
					Log.e("sql", e.getLocalizedMessage());
				}
			}
			ocg.delMembers(cb);
		}
		if (cg != groups.get(-1))
		{
			SQLiteStatement stmt = db.compileStatement(SQL_insertMapping);
			stmt.bindLong(1, cb.getId());
			stmt.bindLong(2, cg.getGid());
			try
			{
				stmt.executeInsert();
			}
			catch (SQLException e)
			{
				Log.e("sql", e.getLocalizedMessage());
			}
		}
		cb.setGroup(cg);
		cg.addMembers(cb);
	}

	static public void addPeople(ContactBean cb, ContactGroup cg)
	{
		SystemContactUtil.add(MainActivity.getContext(), cb);
		if (cg == null)
			cg = groups.get(-1);
		addToGroup(cb, cg);
		people.add(cb);
	}

	static public void delete()
	{
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

	/*
	 * static public void delete(ContactBean cb) { SQLiteStatement stmt =
	 * db.compileStatement(deleteSQL); stmt.bindLong(1, cb.getId()); try { int
	 * num = stmt.executeUpdateDelete(); Log.v("tester", "finish delete,affect "
	 * + num); } catch (SQLException e) { Log.e("sql", e.getLocalizedMessage());
	 * } }
	 * 
	 * static public ArrayList<ContactBean> query() { Cursor cursor =
	 * db.rawQuery(selectAllSQL, null); ArrayList<ContactBean> cbs =
	 * DataInject.CursorToObjs(cursor, ContactBean.class); cursor.close();
	 * return cbs; }
	 * 
	 * static public ContactBean query(int ID) { String[] arg = new String[] {
	 * "" + ID }; Cursor cursor = db.rawQuery(selectSQL, arg); try { ContactBean
	 * cb = new ContactBean(); if (cursor.moveToFirst())
	 * DataInject.CursorToObj(cursor, cb); else cb = null; return cb; } catch
	 * (SQLException e) { Log.e("sql", e.getLocalizedMessage()); return null; }
	 * finally { cursor.close(); } }
	 */

	static public void onExit()
	{
		if (db != null)
			db.close();
	}
}
