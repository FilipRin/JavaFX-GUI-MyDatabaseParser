package application;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javafx.scene.control.TextArea;

public class Table {
	
	private String name;
	private ArrayList<Record> records=new ArrayList<Record>();
	private ArrayList<String> columns=new ArrayList<String>();
	private ArrayList<Integer> gIdIndexes=new ArrayList<Integer>();
	private int gId;
	
	/**
	 * Table constructor
	 * @param _name  name of table
	 * @param statement  string which will be parsed using regex for creating tables in SQL
	 */
	public Table(String _name,String statement) {
		name=_name;
		Pattern pattern = Pattern.compile("CREATE TABLE \\w+\\s*\\(\\s*(.*?)\\s*\\)[^\\n]*");
		Matcher matcher = pattern.matcher(statement);
		String x;
		//Izdvajanje kolona za novu tabelu i dodavanje predefinisanog grupnog ID-a (gId)
		if(matcher.find()) {
			x=matcher.group(1);
			x=x+"!";
			int i=0;
			String pom="";
			while(x.charAt(i)!='!') {
				if(x.charAt(i)==',') {
					columns.add(pom);
					pom="";
					i++;
				}
				else {
					pom+=x.charAt(i);
					i++;
				}
			}
			columns.add(pom);
			pom="";
		}
		gId=0;
	}
	
	/**
	 * @return  returns name of table
	 */
	public String getName() {return name;}
	/**
	 * @param n  number of a column
	 * @return  name of that column
	 */
	public String getColumnName(int n) {return columns.get(n);}
	/**
	 * @return  number of columns
	 */
	public Integer getNumberOfColumns() {return columns.size();}
	/**
	 * @return  size/number of data in table
	 */
	public Integer getSize() {return records.size();}
	/**
	 * @return  all data from this table
	 */
	public ArrayList<Record> getAllData(){ return records; }
	
	/**
	 * @param _name  name of wanted column
	 * @return  data of that column
	 */
	public ArrayList<Record> getDataFromColumnName(String _name){
		ArrayList<Record> data = new ArrayList<Record>();
		for (Record rec : records) {
			if(rec.getColumnName().compareTo(_name)==0) {
				data.add(rec);
			}
		}
		return data;
	}
	
	/**
	 * @param i  wanted group ID
	 * @return  data that belong to that group ID
	 */
	public ArrayList<Record> getDataFromGId(int i){
		ArrayList<Record> data = new ArrayList<Record>();
		for (Record rec : records) {
			if(rec.getGId()==i) {
				data.add(rec);
			}
		}
		return data;
	}
	
	/**
	 * Inserting data into table
	 * @param col  columns in which we will insert data
	 * @param val  values which will be inserted in given columns
	 * @param gId  group ID that will be given to all inserted data
	 * @return  0 if insert was successful
	 */
	public int insertIntoTable(String col,String val,int gId) {
		records.add(new Record(col,val,this.gId,gIdIndexes));
		return 0;
	}
	
	/**
	 * increments group ID
	 */
	public void incGid() {gId++;}
	
	/**
	 * Inserting data into table, but giving index of columns instead of names
	 * @param col  index of column
	 * @param val  values which will be inserted in given columns
	 * @param gId  group ID that will be given to all inserted data
	 * @return  0 if insert was successful
	 */
	public int insertIntoTableNoCol(int col,String val,int gId) {
		if(col>=columns.size()) {
			return -1;
		}
		records.add(new Record(columns.get(col),val,this.gId,gIdIndexes));
		return 0;
	}
	
	/**
	 * @param targetColumnName  
	 * @param columnName
	 * @param oldVal
	 * @param newVal
	 * @return
	 */
	public int updateDataByGivenStringAndGid(String targetColumnName,String columnName,String oldVal,String newVal) {
		ArrayList<Integer> gIdTmps = new ArrayList<Integer>();
		for (Record rec : records) {
			if(rec.getValue().compareTo(oldVal)==0 && rec.getColumnName().compareTo(columnName)==0) {
				gIdTmps.add(rec.getGId());
			}
		}
		for (int i = 0; i < gIdTmps.size(); i++) {
			for (Record rec : records) {
				if(rec.getGId()==gIdTmps.get(i) && rec.getColumnName().compareTo(targetColumnName)==0) {
					rec.changeValue(newVal);
				}
			}
		}
		return 0;
	}
	
	/**
	 * @param col  column/s to be deleted
	 * @param val  value of data which will be deleted
	 * @return  number of deleted data
	 */
	public int deleteDataByGivenColumnAndString(String col,String val) {
		ArrayList<Integer> gIdList=new ArrayList<Integer>();
		int num=0;
		for (Record rec : records) {
			if(rec.getValue().compareTo(val)==0 && rec.getColumnName().compareTo(col)==0) {
				gIdList.add(rec.getGId());
			}
		}
		gIdList=(ArrayList<Integer>) gIdList.stream().distinct().collect(Collectors.toList());
		for (Integer gId : gIdList) {
			int ig=gIdIndexes.indexOf(gIdIndexes.getFirst());
			int i=records.indexOf(records.getFirst());
			while(i<=records.indexOf(records.getLast())) {
				if(i==-1) break;
				if(records.get(i).getGId()==gId) {
					records.remove(i);
					gIdIndexes.remove(ig);
					num++;
					//i=records.indexOf(records.getFirst());
					//ig=gIdIndexes.indexOf(gIdIndexes.getFirst());
					if(records.size()==0) break;
					continue;
				}
				i++;
				ig++;
			}
		}
		return num;
	}
	
	/**
	 * @return  all group IDs
	 */
	public ArrayList<Integer> getAllGIds(){
		ArrayList<Integer> ids=new ArrayList<Integer>();
		for (int i = 0; i < records.size(); i++) {
			ids.add(records.get(i).getGId());
		}
		Collections.sort(ids);
		ids=(ArrayList<Integer>) ids.stream().distinct().collect(Collectors.toList());
		return ids;
	}
	
	/**
	 * @return  all column names
	 */
	public ArrayList<String> getAllColumnNames(){
		ArrayList<String> names=new ArrayList<String>();
		for (int i = 0; i < records.size(); i++) {
			names.add(records.get(i).getColumnName());
		}
		Collections.sort(names);
		names=(ArrayList<String>) names.stream().distinct().collect(Collectors.toList());
		return names;
	}
	
	/**
	 * @param txt  Area where the text will be printed on
	 * @param txtRes  Area where the result or other info will be printed on
	 */
	public void printTable(TextArea txt,TextArea txtRes) {
		int index=0;
		String format = "|%1$-10s";
		StringBuilder str=new StringBuilder();
		str.append(String.format(format,"ID"));
		str.append(String.format(format,"Name"));
		str.append(String.format(format,"Value"));
		str.append("\n");
		for(Record rec : records) {
			str.append(String.format(format,rec.getGId()));
			str.append(String.format(format,rec.getColumnName()));
			str.append(String.format(format,rec.getValue()));
			str.append("\n");
			index++;
		}
		System.out.println(str.toString());		
		txt.setText(str.toString());
		//txtRes.setText("Number of data selected: "+index);
	}
	
	/**
	 * @param txt  Area where the text will be printed on
	 * @param txtRes  Area where the result or other info will be printed on
	 * @param str  String builder that join data from more tables
	 * @param format  Format for printed text
	 */
	public void printTable(TextArea txt,TextArea txtRes,StringBuilder str,String format) {
		int index=0;
		
		for(Record rec : records) {
			str.append(String.format(format,rec.getGId()));
			str.append(String.format(format,rec.getColumnName()));
			str.append(String.format(format,rec.getValue()));
			str.append("\n");
			index++;
		}
		System.out.println(str.toString());		
		txt.setText(str.toString());
		//txtRes.setText("Number of data selected: "+index);
	}
	
	/**
	 * @param recs  List of records/data that we want printed
	 * @param txt  Area where the text will be printed on
	 * @param txtRes  Area where the result or other info will be printed on
	 */
	public void printTableWithGivenRecords(ArrayList<Record> recs,TextArea txt,TextArea txtRes) {
		//Print specific info
		int index=0;
		String format = "|%1$-10s";
		StringBuilder str=new StringBuilder();
		str.append(String.format(format,"ID"));
		str.append(String.format(format,"Name"));
		str.append(String.format(format,"Value"));
		str.append("\n");
		for (Record rec : recs) {
			//str.append(gIdIndexes.get(index));
			str.append(String.format(format,rec.getGId()));
			str.append(String.format(format,rec.getColumnName()));
			str.append(String.format(format,rec.getValue()));
			str.append("\n");
			index++;
		}
		System.out.println(str.toString());
		txt.setText(str.toString());
		//txtRes.setText("Number of data selected: "+index);
	}
	
	/**
	 * @param recs  List of records/data that we want printedv
	 * @param txt  Area where the text will be printed on
	 * @param txtRes  Area where the result or other info will be printed on
	 * @param str  String builder that join data from more tables
	 * @param format   Format for printed text
	 */
	public void printTableWithGivenRecords(ArrayList<Record> recs,TextArea txt,TextArea txtRes,StringBuilder str,String format) {
		//Print specific info
		int index=0;
		
		for (Record rec : recs) {
			//str.append(gIdIndexes.get(index));
			str.append(String.format(format,rec.getGId()));
			str.append(String.format(format,rec.getColumnName()));
			str.append(String.format(format,rec.getValue()));
			str.append("\n");
			index++;
		}
		System.out.println(str.toString());
		txt.setText(str.toString());
		//txtRes.setText("Number of data selected: "+index);
	}
}
