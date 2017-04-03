
public class Entry {
	private String symbol;
	private int address;
	public Entry(String s, int add){
		symbol=s;
		address=add;
	}
	public String getSymbol(){
		return symbol;
	}
	public int getAddress(){
		return address;
	}
	
}
