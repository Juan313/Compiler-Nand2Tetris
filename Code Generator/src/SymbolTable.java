import java.util.Hashtable;


public class SymbolTable {
	Hashtable classScopeST = null;
	Hashtable subrountineScopeST = null;
	int indexStatic;
	int indexField;
	int indexArg;
	int indexVar;
	// constructor: create a class scope symbol table 
	public SymbolTable(){
		classScopeST = new Hashtable(); 
		subrountineScopeST = new Hashtable(); 
		indexStatic=0;
		indexField=0;
		indexArg=0;
		indexVar=0;
	}
	
	void startSubroutine(){
		subrountineScopeST = new Hashtable(); 
		indexArg=0;
		indexVar=0;
	}
	
	void Define(String name, String type, String kind){
		if (kind.equals("static")){
			Entry e = new Entry (type,kind,indexStatic);
			classScopeST.put(name, e);
			indexStatic=indexStatic+1;
		} else if (kind.equals("field")){
			Entry e = new Entry (type,kind,indexField);
			classScopeST.put(name, e);
			indexField=indexField+1;
		} else if (kind.equals("argument")){
			Entry e = new Entry (type,kind,indexArg);
			subrountineScopeST.put(name, e);
			indexArg=indexArg+1;
		} else if (kind.equals("var")){
			Entry e = new Entry (type,kind,indexVar);
			subrountineScopeST.put(name, e);
			indexVar=indexVar+1;
		}
	}
	int VarCount(String kind){
		int count=0;
		switch (kind) {
			case "static": count = indexStatic;
			break;
			case "field": count = indexField;
			break;
			case "argument": count = indexArg;
			break;
			case "var": count = indexVar;
			break;
		}
		return count;
		
	}
	String KindOf (String name){
		if (subrountineScopeST.containsKey(name)) {
			Entry e =(Entry) subrountineScopeST.get(name);
			return e.kind;
		} else if (classScopeST.containsKey(name)){
			Entry e =(Entry) classScopeST.get(name);
			return e.kind;
		} else return "NONE";
	}
	String TypeOf(String name){
		
		if (subrountineScopeST.containsKey(name)) {
			Entry e =(Entry) subrountineScopeST.get(name);
			return e.type;
		} else if (classScopeST.containsKey(name)){
			Entry e =(Entry) classScopeST.get(name);
			return e.type;
		} else return null;
	}

	int IndexOf(String name) {

		if (subrountineScopeST.containsKey(name)) {
			Entry e = (Entry) subrountineScopeST.get(name);
			return e.index;
		} else if (classScopeST.containsKey(name)) {
			Entry e = (Entry) classScopeST.get(name);
			return e.index;
		} else
			return -1;
	}
	
	
	class Entry{
		String type;
		String kind;
		int index;
		Entry(String t, String k, int i){
			type=t;
			kind=k;
			index=i;
		}
	}
	
}
