package xziar.contacts.bean;

import java.io.ByteArrayOutputStream;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import xziar.contacts.util.HanziToPinyin;

public class ContactBean implements ContactInterface
{
	private String name = "";
	private String cel = "";
	private String tel = "";
	private String describe = "";
	private String email = "";
	private byte[] img = null;
	private Bitmap bmp = null;

	private int id = -1;
	private char firstLetter;
	private String pinYin = "";
	private ContactGroup group;

	public ContactBean()
	{
	}

	public ContactBean(String name)
	{
		setName(name);
	}

	public ContactBean(int id)
	{
		setId(id);
	}

	@Override
	public int compareTo(ContactInterface another)
	{
		int ans = getIndexChar() - another.getIndexChar();
		if (ans != 0)
			return ans;
		if (another instanceof ContactBean)
			return pinYin.compareTo(((ContactBean) another).pinYin);
		else
			return pinYin.compareTo(String.valueOf(another.getIndexChar()));
	}

	@Override
	public char getIndexChar()
	{
		return firstLetter;
	}

	@Override
	public boolean isContain(CharSequence s)
	{
		return (name.contains(s) || pinYin.contains(s));
	}

	@Override
	public String getName()
	{
		return name;
	}

	@SuppressLint("DefaultLocale")
	public void setName(String name)
	{
		this.name = name;
		pinYin = HanziToPinyin.getPinYin(name);
		String first = pinYin.substring(0, 1);
		if (first.matches("[A-Za-z]"))
		{
			firstLetter = first.toUpperCase().charAt(0);
		}
		else
		{
			firstLetter = '#';
		}
	}

	public String getCel()
	{
		return cel;
	}

	public void setCel(String cel)
	{
		this.cel = cel;
	}

	public String getTel()
	{
		return tel;
	}

	public void setTel(String tel)
	{
		this.tel = tel;
	}

	public String getDescribe()
	{
		return describe;
	}

	public void setDescribe(String describe)
	{
		this.describe = describe;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public byte[] getImg()
	{
		return img;
	}

	public void setImg(byte[] img)
	{
		this.img = img;
		bmp = BitmapFactory.decodeByteArray(img, 0, img.length);
	}

	@Override
	public Bitmap getHead()
	{
		return bmp;
	}
	
	public void setHead(Bitmap head)
	{
		bmp = head;
		if(bmp == null)
		{
			img = null;
			return;
		}
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.PNG, 100, os);
		img = os.toByteArray();
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	@Override
	public String getGroupName()
	{
		return group.getName();
	}

	public ContactGroup getGroup()
	{
		return group;
	}

	public void setGroup(ContactGroup group)
	{
		this.group = group;
	}
}