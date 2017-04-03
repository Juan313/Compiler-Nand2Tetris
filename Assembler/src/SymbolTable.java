import java.io.IOException;
import java.util.Hashtable;


public class SymbolTable {
	Hashtable table;
	public SymbolTable(){
		table =new Hashtable();
		table.put("SP", new Integer(0));
		table.put("LCL", new Integer(1));
		table.put("ARG", new Integer(2));
		table.put("THIS", new Integer(3));
		table.put("THAT", new Integer(4));
		table.put("R0", new Integer(0));
		table.put("R1", new Integer(1));
		table.put("R2", new Integer(2));
		table.put("R3", new Integer(3));
		table.put("R4", new Integer(4));
		table.put("R5", new Integer(5));
		table.put("R6", new Integer(6));
		table.put("R7", new Integer(7));
		table.put("R8", new Integer(8));
		table.put("R9", new Integer(9));
		table.put("R10", new Integer(10));
		table.put("R11", new Integer(11));		
		table.put("R12", new Integer(12));
		table.put("R13", new Integer(13));
		table.put("R14", new Integer(14));
		table.put("R15", new Integer(15));
		table.put("SCREEN", new Integer(16384));
		table.put("KBD", new Integer(24576));
			
	}
	public static void main(String[] args) throws IOException {
		SymbolTable st= new SymbolTable();
		System.out.println(st.table.get("SCREEN").toString());
		
	}
	
}
