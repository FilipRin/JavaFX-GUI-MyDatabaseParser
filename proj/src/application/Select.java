package application;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.scene.control.TextArea;

public class Select extends Statement {

	private ArrayList<String> columns=new ArrayList<String>(), listOfChecks=new ArrayList<String>(), selTables=new ArrayList<String>(), selColumns=new ArrayList<String>();
	private Boolean condition=false;
	
	public Select(ArrayList<String> StatementWords,String stmnt,ArrayList<Table> tables, TextArea txt, TextArea txtRes) {
		Pattern pattern1 = Pattern.compile("SELECT ([^F]*) FROM ([^W]*) WHERE ([^\\n]*)");
		Pattern pattern2 = Pattern.compile("SELECT ([^F]*) FROM ([^\\n]*)");
		Matcher matcher1 = pattern1.matcher(stmnt);
		Matcher matcher2 = pattern2.matcher(stmnt);
		String cols,selTbls,LstOfChecks;
		//Izdvajanje kolona,tabela,kriterijuma
		if(matcher1.find()) {
			cols=matcher1.group(1);
			String[] pomstr=cols.split(",");
			for (String string : pomstr) {
				columns.add(string);
			}
			
			selTbls=matcher1.group(2);
			pomstr=selTbls.split(",");
			for (String string : pomstr) {
				selTables.add(string);
			}
			
			LstOfChecks=matcher1.group(3);
			pomstr=LstOfChecks.split(",");
			for (String string : pomstr) {
				listOfChecks.add(string);
			}
		}
		else if(matcher2.find()) {
			cols=matcher2.group(1);
			String[] pomstr=cols.split(",");
			for (String string : pomstr) {
				columns.add(string);
			}
			
			selTbls=matcher2.group(2);
			pomstr=selTbls.split(",");
			for (String string : pomstr) {
				selTables.add(string);
			}
		}
		else {
			txtRes.setText("Statement incorrect!");
			return;
		}
		//Uzimamo tabele koje su selektovane
		ArrayList<Table> selectedTables=new ArrayList<Table>();
		for (String strTable : selTables) {
			for (int i = 0; i < tables.size(); i++) {
				if(tables.get(i).getName().compareTo(strTable)==0) {
					selectedTables.add(tables.get(i));
				}
			}
		}
		//Ako se prikazuju sve informacije
		if(columns.get(0).compareTo("*")==0) {
			ArrayList<Record> allRecords = new ArrayList<Record>();
			ArrayList<String> distColumnNames=new ArrayList<String>();
			for (Table table : selectedTables) {
				allRecords.addAll(table.getAllData());
			}
			for (Record record : allRecords) {
				if(!distColumnNames.contains(record.getColumnName())) {
					distColumnNames.add(record.getColumnName());
				}
			}
			//PRINT TABLE
			int num=0;
			
			if(tables.size()>1) {
				String format = "|%1$-10s";
				StringBuilder str=new StringBuilder();
				str.append(String.format(format,"ID"));
				str.append(String.format(format,"Name"));
				str.append(String.format(format,"Value"));
				str.append("\n");
				for (Table table : selectedTables) {
					table.printTable(txt,txtRes,str,format);
				}
			}
			else {
				for (Table table : selectedTables) {
					table.printTable(txt,txtRes);
				}
			}
			num+=allRecords.size()/columns.size();
			txtRes.setText("Number of data selected: "+num);
			
		}
		//Ako se prikazuju specificne informacije
		else {
			ArrayList<Record> allRecords=new ArrayList<Record>(),tmp = new ArrayList<Record>();
			ArrayList<Integer> allIds=new ArrayList<Integer>();
			for (Table tbl : selectedTables) {
				for(int i=0;i<columns.size();i++) {
					System.out.println(tbl.getAllColumnNames().toString());
					if(tbl.getAllColumnNames().contains(columns.get(i))) {
						allRecords.addAll(tbl.getDataFromColumnName(columns.get(i)));
						allIds.addAll(tbl.getAllGIds());
					}
					//tmp.add(tbl.getDataFromColumnName(col))
				}
			}
			ArrayList<String> distColumnNames=new ArrayList<String>();
			for (Record record : allRecords) {
				if(!distColumnNames.contains(record.getColumnName())) {
					distColumnNames.add(record.getColumnName());
				}
			}
			Collections.sort(allRecords, new GIdComparator());
			int num=0;
			
			if(tables.size()>1) {
				String format = "|%1$-10s";
				StringBuilder str=new StringBuilder();
				str.append(String.format(format,"ID"));
				str.append(String.format(format,"Name"));
				str.append(String.format(format,"Value"));
				str.append("\n");
				for (Table table : selectedTables) {
					table.printTableWithGivenRecords(allRecords,txt,txtRes,str,format);
					break;
				}
			}
			else {
				for (Table table : selectedTables) {
					table.printTableWithGivenRecords(allRecords,txt,txtRes);
				}
			}
			num+=allRecords.size();
			txtRes.setText("Number of data selected: "+num);
		}
	}
	
	
}

class GIdComparator implements Comparator<Record>{
	@Override
	public int compare(Record o1, Record o2) {
		return o1.getGId().compareTo(o2.getGId());
	}
}
