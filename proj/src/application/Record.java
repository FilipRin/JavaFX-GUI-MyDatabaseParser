package application;

import java.util.ArrayList;

public class Record {
	private String value,columnName;
	private int groupId, id;
	private static int static_id=0;
	
	public Record(String c, String v, int gId, ArrayList<Integer> gIdIndexes) {
		columnName=c;
		value=v;
		groupId=gId;
		id=++static_id;
		gIdIndexes.add(gId);
	}
	
	public String getColumnName() {return columnName;}
	public String getValue() {return value;}
	public Integer getGId() {return groupId;}
	public Integer getId() {return id;}
	public void changeValue(String val) {value=val;}
	
}
