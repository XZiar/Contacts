package xziar.contacts.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.database.Cursor;
import android.util.Log;

public class DataInject
{
	private static final Pattern pm = Pattern.compile("set(\\w+)");
	private static HashMap<String, HashMap<String, Method>> ObjectMap = new HashMap<String, HashMap<String, Method>>();

	public static void Load(Class<?> cls)
	{
		HashMap<String, Method> MethodMap = new HashMap<String, Method>();
		Method[] ms = cls.getMethods();
		for (Method m : ms)
		{
			Matcher mth = pm.matcher(m.getName());
			if (mth.matches())
			{
				MethodMap.put(mth.replaceAll("$1"), m);
			}
		}
		String objName = cls.getName();
		Log.v("out", "DataInject Load Class:" + objName);
		ObjectMap.put(objName, MethodMap);
	}

	public static void CursorToObj(Cursor cs, Object obj)
	{
		Class<?> cls = obj.getClass();
		String cname = cls.getName();
		HashMap<String, Method> MethodMap = ObjectMap.get(cname);
		if (MethodMap == null)
		{
			Load(cls);
			MethodMap = ObjectMap.get(cname);
		}
		String[] cols = cs.getColumnNames();
		for (int a = 0; a < cols.length; ++a)
		{
			Method mth = MethodMap.get(cols[a]);
			if (mth == null)
			{
				Log.v("out", "expected:" + cols[a]);
				continue;
			}
			Object val = null;
			switch (cs.getType(a))
			{
			case Cursor.FIELD_TYPE_STRING:
				val = cs.getString(a);
				break;
			case Cursor.FIELD_TYPE_INTEGER:
				val = cs.getInt(a);
				break;
			case Cursor.FIELD_TYPE_NULL:
			case Cursor.FIELD_TYPE_BLOB:
				val = cs.getBlob(a);
				Log.v("obj-blob", "receive the:"+val);
				break;
			case Cursor.FIELD_TYPE_FLOAT:
				val = cs.getDouble(a);
				break;
			}
			try
			{
				mth.invoke(obj, val);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public static <T> ArrayList<T> CursorToObjs(Cursor cs, Class<T> cls)
	{
		ArrayList<T> rets = new ArrayList<>();
		String cname = cls.getName();
		HashMap<String, Method> MethodMap = ObjectMap.get(cname);
		if (MethodMap == null)
		{
			Load(cls);
			MethodMap = ObjectMap.get(cname);
		}
		String[] cols = cs.getColumnNames();
		cs.moveToFirst();
		HashMap<Integer, Integer> types = new HashMap<>();
		HashMap<Integer, Method> mths = new HashMap<>();
		for (int a = 0; a < cols.length; ++a)
		{
			Method mth = MethodMap.get(cols[a]);
			if (mth == null)
			{
				System.out.println("expected:" + cols[a]);
				continue;
			}
			mths.put(a, mth);
			types.put(a, cs.getType(a));
		}
		int cnt = cs.getCount();
		try
		{
			for (int a = 0; a < cnt; a++)
			{
				T obj = cls.newInstance();
				for (Entry<Integer, Integer> e : types.entrySet())
				{
					Object val = null;
					int index = e.getKey();
					switch (e.getValue())
					{
					case Cursor.FIELD_TYPE_STRING:
						val = cs.getString(index);
						break;
					case Cursor.FIELD_TYPE_INTEGER:
						val = cs.getInt(index);
						break;
					case Cursor.FIELD_TYPE_NULL:
					case Cursor.FIELD_TYPE_BLOB:
						val = cs.getBlob(index);
						Log.v("objs-blob", "receive one:"+val);
						break;
					case Cursor.FIELD_TYPE_FLOAT:
						val = cs.getDouble(index);
						break;
					}
					try
					{
						mths.get(index).invoke(obj, val);
					}
					catch (IllegalArgumentException
							| InvocationTargetException e1)
					{
						e1.printStackTrace();
					}
				}
				rets.add(obj);
				cs.moveToNext();
			}
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}
		return rets;
	}
}
