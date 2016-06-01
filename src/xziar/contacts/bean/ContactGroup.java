package xziar.contacts.bean;

import java.util.ArrayList;

public class ContactGroup
{
	private int gid = -1;
	private String name = "";
	private ArrayList<ContactBean> members = new ArrayList<>();;

	public ContactGroup()
	{

	}

	public ContactGroup(String name)
	{
		this.name = name;
	}

	public void addMembers(ContactBean cb)
	{
		members.add(cb);
	}

	public void delMembers(ContactBean cb)
	{
		members.remove(cb);
	}

	public int getGid()
	{
		return gid;
	}

	public void setGid(int gid)
	{
		this.gid = gid;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

}
