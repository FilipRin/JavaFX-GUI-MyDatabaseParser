package application;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.scene.control.TextArea;

public class Insert extends Statement {

	private ArrayList<String> listOfColumns=new ArrayList<String>();
	private ArrayList<String> listOfValues=new ArrayList<String>();
	Boolean hasColumns=false;
	static int idOfRecord=0;
	private String nameOfTable;
	
	private void reset() {
		listOfColumns.clear();
		listOfValues.clear();
		hasColumns=false;
	}
	
	public Insert(ArrayList<String> StatementWords, String stmnt, ArrayList<Table> tables, TextArea txtRes) {
		Pattern pattern1 = Pattern.compile("INSERT INTO ([^\\(]*) \\(([^\\)]*)\\) VALUES \\(([^\\)]*)\\)[^\\n]*");
		Pattern pattern2 = Pattern.compile("INSERT INTO ([^\\(]*) VALUES \\(([^\\)]*)\\)[^\\n]*");
		Matcher matcher1 = pattern1.matcher(stmnt);
		Matcher matcher2 = pattern2.matcher(stmnt);
		String cols,vals;
		
		int i=0;
		//Izdvajanje naziva tabele,vrednosti i kolona
		if(matcher1.find()) {
			cols=matcher1.group(2);
			String[] pomstr=cols.split(",");
			for (String string : pomstr) {
				listOfColumns.add(string);
			}
			
			vals=matcher1.group(3);
			pomstr=vals.split(",");
			for (String string : pomstr) {
				listOfValues.add(string);
			}
			nameOfTable=matcher1.group(1);
			
			if(listOfValues.size()!=listOfColumns.size()) {
				
				txtRes.setText("ERROR: Incorrect statement-> No. of columns: "+listOfColumns.size()+", No. of values: "+listOfValues.size());
				reset();
				//System.out.println("ERROR: neispravan unos-> broj kolona: "+listOfColumns.size()+" a broj vrednosti: "+listOfValues.size());
				return;
			}
			//Unos podataka u tabelu
			for (i=0;i<tables.size();i++) {
				if(tables.get(i).getName().compareTo(nameOfTable)==0) {
					for(int j=0;j<listOfColumns.size();j++) {
						tables.get(i).insertIntoTable(listOfColumns.get(j), listOfValues.get(j), idOfRecord);
						if(j==listOfColumns.size()-1) {
							tables.get(i).incGid();
						}
					}
					break;
				}
			}
			if(i==tables.size()) {
				txtRes.setText("ERROR: Table "+nameOfTable +" does not exist");
				reset();
				System.out.println("ERROR: tabela ne postoji");
				return;
			}
		}
		else if(matcher2.find()) {
			nameOfTable=matcher1.group(1);
			
			vals=matcher1.group(2);
			String[] pomstr=vals.split(",");
			for (String string : pomstr) {
				listOfValues.add(string);
			}
			//Unos podataka u tabelu tako da se match-uje kolona sa vrednosti
			for (i=0;i<tables.size();i++) {
				if(tables.get(i).getName().compareTo(nameOfTable)==0) {
					if(tables.get(i).getNumberOfColumns()==listOfValues.size()) {
						for(int j=0;j<listOfValues.size();j++) {
							tables.get(j).insertIntoTableNoCol(j, listOfValues.get(j), idOfRecord);
							if(j==listOfValues.size()-1) {
								tables.get(j).incGid();
							}
						}
						break;
					}
					else {
						txtRes.setText("ERROR: Incorrect statement-> No. of columns: "+listOfColumns.size()+", No. of values: "+listOfValues.size());
						reset();
						System.out.println("Error: neispravan unos-> broj kolona: "+tables.get(i).getNumberOfColumns()+" a broj prosledjenih vrednosti: "+listOfValues.size());
						return;
					}
				}
			}
			if(i==tables.size()) {
				txtRes.setText("ERROR: Table "+nameOfTable +" does not exist");
				reset();
				System.out.println("ERROR: tabela ne postoji");
				return;
			}
			
		}
		else {
			txtRes.setText("Statement incorrect!");
			return;
		}
		Insert.idOfRecord++;
		txtRes.setText("Data successfully inserted");
		System.out.println("Podatak uspesno unesen!");
		reset();
	}
	
	
	
	
}
