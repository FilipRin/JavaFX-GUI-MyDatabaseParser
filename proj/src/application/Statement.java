package application;

import java.util.ArrayList;

public class Statement {

	private ArrayList<Table> tables=null;
	int res=0;
	public Statement() {}

	
	public String parseTillDelimiter(String str,char delimiter) {
		int pos=str.indexOf(delimiter);
		if(pos!=-1) {
			return str.substring(0, pos);
		}
		else {
			return str;
		}
	}
	
	public String parseBetweenTwoDelimiters(String str,char delimiter1,char delimiter2) {
		int startPos=str.indexOf(delimiter1);
		if(startPos==-1) return "";
		++startPos;
		int endPos=str.indexOf(delimiter2, startPos);
		if(delimiter2=='\0') {
			return str.substring(startPos);
		}
		if(endPos==-1) {
			return "";
		}
		return str.substring(startPos, endPos);
	}
}
