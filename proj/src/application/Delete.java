package application;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.scene.control.TextArea;

public class Delete extends Statement {
	private ArrayList<String> listOfColumns=new ArrayList<String>();
	private ArrayList<String> listOfChecks=new ArrayList<String>();
	private String nameOfTable;
	
	private void reset() {
		listOfColumns.clear();
		listOfChecks.clear();
	}
	
	public Delete(ArrayList<String> StatementWords, String stmnt, ArrayList<Table> tables, TextArea txtRes) {
		Pattern pattern1 = Pattern.compile("DELETE FROM ([^\\ ]*) WHERE ([^\\n]*)");
		Matcher matcher1 = pattern1.matcher(stmnt);
		int i=0;
		String cols,checks;
		//Izdvajanje tabele i uslova
		if(matcher1.find()) {
			
			nameOfTable=matcher1.group(1);
			checks=matcher1.group(2);
			String[] pomstr=checks.split(",");
			for (String string : pomstr) {
				listOfChecks.add(string);
			}
			//Ako tabela postoji
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
			
			//Izdvajanje kolone i vrednosti pa brisanje odgovarajuce linije iz tabele
			String colName=parseTillDelimiter(listOfChecks.get(0), '=');
			String strVal=parseBetweenTwoDelimiters(listOfChecks.get(0), '=', '\0');
			int numDeleted=tables.get(i).deleteDataByGivenColumnAndString(colName,strVal);
			if(numDeleted==0) {
				txtRes.setText("No data was deleted, statement contained wrong columns/values ");
			}
			else {
				txtRes.setText("Number of data successfully deleted: "+numDeleted);
			}
			System.out.println("Number of data successfully deleted: "+numDeleted);
		}
		else {
			txtRes.setText("Statement incorrect!");
		}
	}
	
	
	
}
