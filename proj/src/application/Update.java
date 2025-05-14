package application;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javafx.scene.control.TextArea;

public class Update extends Statement {
	private ArrayList<String> listOfColumns=new ArrayList<String>();
	private ArrayList<String> listOfValues=new ArrayList<String>();
	private ArrayList<String> listOfChecks=new ArrayList<String>();
	private String nameOfTable;
	
	private void reset() {
		listOfColumns.clear();
		listOfValues.clear();
		listOfChecks.clear();
	}
	
	private ArrayList<Integer> getAllIds(Table table,ArrayList<String> listOfChecks){
		ArrayList<Integer> ids=new ArrayList<Integer>();
		String colName=parseBetweenTwoDelimiters(listOfChecks.get(0), '.', '=');
		ArrayList<Record> recs=table.getDataFromColumnName(colName);
		
		for (Record rec : recs) {
			ids.add(rec.getGId());
		}
		ids=(ArrayList<Integer>) ids.stream().distinct().collect(Collectors.toList());
		return ids;
	}
	
	public Update(ArrayList<String> StatementWords, String stmnt, ArrayList<Table> tables, TextArea txtRes) {
		Pattern pattern1 = Pattern.compile("UPDATE ([^\\ ]*) SET ([^\\ ]*) WHERE ([^\\n]*)");
		Matcher matcher1 = pattern1.matcher(stmnt);
		int i=0;
		String cols,checks;
		//Izdvajanje tabele, kolona i uslova
		if(matcher1.find()) {
			cols=matcher1.group(2);
			String[] pomstr=cols.split(",");
			for (String string : pomstr) {
				listOfColumns.add(string);
			}
			
			checks=matcher1.group(3);
			pomstr=checks.split(",");
			for (String string : pomstr) {
				listOfChecks.add(string);
			}
			nameOfTable=matcher1.group(1);
			
			for (i=0;i<tables.size();i++) {
				if(tables.get(i).getName().compareTo(nameOfTable)==0) {
					break;
				}
			}
			if(i==tables.size()) {
				reset();
				System.out.println("ERROR: this table does not exist!");
				return;
			}
			//Izdvajanje kolona za datau tabelu i vrednosti(novih i starih) pre izmene podataka
			ArrayList<Integer> allIds=getAllIds(tables.get(i), listOfChecks);
			ArrayList<Record> matchingRecords=new ArrayList<Record>();
			String updateTargetColumn = parseBetweenTwoDelimiters(listOfColumns.get(0), '.', '=');
			String newValue = parseBetweenTwoDelimiters(listOfColumns.get(0), '=', '\0');
			String columnName = parseBetweenTwoDelimiters(listOfChecks.get(0), '.', '=');
			String oldValue = parseBetweenTwoDelimiters(listOfChecks.get(0), '=', '\0');
			
			tables.get(i).updateDataByGivenStringAndGid(updateTargetColumn, columnName, oldValue, newValue);
			txtRes.setText("Data in table "+nameOfTable+"("+updateTargetColumn+") changed to "+newValue + " where "+columnName+"="+oldValue);
			reset();
		}
		else {
			txtRes.setText("Statement incorrect!");
		}
	}
}
