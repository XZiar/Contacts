package xziar.contacts.bean;

public interface ContactInterface extends Comparable<ContactInterface>
{
	public char getIndexChar();
	public String getName();
	public String getImg();
	public boolean isContain(CharSequence s);
}
