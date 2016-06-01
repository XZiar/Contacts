package xziar.contacts.bean;

import android.graphics.Bitmap;

public interface ContactInterface extends Comparable<ContactInterface>
{
	public char getIndexChar();
	public String getName();
	public Bitmap getHead();
	public String getGroupName();
	public boolean isContain(CharSequence s);
}
