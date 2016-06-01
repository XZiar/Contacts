package xziar.contacts.bean;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import xziar.contacts.util.HanziToPinyin;

public class ContactBean implements ContactInterface, Serializable
{
	private static final long serialVersionUID = 7361607606145741754L;
	private String name = "";
	private String cel = "111";
	private String tel = "222";
	private String describe = "bgfgf";
	private String email = "fdvfdb";
	private byte[] img = null;

	private int id = -1;
	private char firstLetter;
	private String pinYin = "";

	public ContactBean()
	{
	}

	public ContactBean(String name)
	{
		setName(name);
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
	}

	@Override
	public Bitmap getHead()
	{
		if (img == null)
			return null;
		else
			return BitmapFactory.decodeByteArray(img, 0, img.length);
	}
	
	public void setHead(Bitmap bmp)
	{
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

}