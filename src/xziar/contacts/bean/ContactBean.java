package xziar.contacts.bean;

import xziar.contacts.util.HanziToPinyin;

public class ContactBean implements ContactInterface
{
	private String name = "";
	private String cel = "";
	private String tel = "";
	private String describe = "";
	private String email = "";

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

	@Override
	public String getImg()
	{
		return null;
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
