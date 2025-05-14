package application;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import application.Main.*;
import javafx.scene.control.TextArea;

public class Database {
	private String name,statement;
	private ArrayList<Table> tables=new ArrayList<Table>();
	private ArrayList<String> StatementWords=new ArrayList<String>();
	
	Select sel=null;
	Update upd = null;
	Insert ins = null;
	Delete del = null;
	
	
	public Database(String txt) {
		this.name=txt;
	}
	
	public String getName() {
		return this.name;
	}
	
	public ArrayList<Table> getTables(){
		return tables;
	}
	
	//Funkcija za prikaz svih tabela SHOW TABLES
	/*
	 * 
		int exportTableInSqlFormat();
		int exportTableInRegularFormat();
		int importDatabase();
	 * */
	
	public int executeStatement(String statement,TextArea txtArea,TextArea txtAreaAllTbls,TextArea txtAreaSelect,Boolean saved) {
		String[] pomstr=statement.split(" ");
		for (String string : pomstr) {
			StatementWords.add(string);
		}
		try {
			if(StatementWords.size()<2) {
				StatementWords.clear();
				return -1;
			}
			String token=statement.substring(0, statement.indexOf(" "));
			//PROVERA DA LI SE RADI O CREATE ILI DROP TABLE
			if(StatementWords.get(1).compareTo("TABLE")==0) {
				String str=StatementWords.get(2);
				if(StatementWords.get(0).compareTo("CREATE")==0) {
					for (Table tbl : tables) {
						if(tbl.getName().compareTo(str)==0) {
							StatementWords.clear();
							txtArea.setText("ERROR: Table "+str+ " already exists!");
							return -2;
						}
					}
					tables.add(new Table(str,statement));
					txtArea.setText("Table "+str+ " created!");
					System.out.println("tabela uspesno kreirana "+str);
					saved=false;
				}
				else if(StatementWords.get(0).compareTo("DROP")==0) {
					int removed=0;
					for (Table tbl : tables) {
						if(tbl.getName().compareTo(str)==0) {
							tables.remove(tbl);
							removed=1;
							StatementWords.clear();
							txtArea.setText("Table "+str+ " dropped!");
							saved=false;
							return 0;
						}
					}
					if(removed==0) {
						StatementWords.clear();
						txtArea.setText("ERROR: Table "+str+ " does not exist!");
						return 0;
					}
				}
			}
			//AKO JE SELECT STATEMENT
			else if(token.compareTo("SELECT")==0) {
				sel = new Select(StatementWords, statement, tables, txtAreaSelect, txtArea);
				sel = null;
			}
			//AKO JE INSERT STATEMENT
			else if(token.compareTo("INSERT")==0) {
				ins = new Insert(StatementWords, statement, tables, txtArea);
				saved=false;
				ins = null;
			}
			//AKO JE DELETE STATEMENT
			else if(token.compareTo("DELETE")==0) {
				del = new Delete(StatementWords, statement, tables, txtArea);
				saved=false;
				del = null;
			}
			//AKO JE UPDATE STATEMENT
			else if(token.compareTo("UPDATE")==0) {
				upd = new Update(StatementWords, statement, tables, txtArea);
				saved=false;
				upd = null;
			}
			//AKO JE SHOW TABLES STATEMENT
			else if(StatementWords.get(0).compareTo("SHOW")==0 && StatementWords.get(1).compareTo("TABLES")==0) {
				StringBuilder str=new StringBuilder();
				for (int i=0;i<tables.size();i++) {
					str.append(tables.get(i).getName());
					if(i<tables.size()-1) {
						str.append(", ");
					}
				}
				txtAreaAllTbls.setText(str.toString());
			}
			else {
				StatementWords.clear();
				txtArea.setText("Statement incorrect!");
				System.out.println("neispravan statement");
				return -1;
			}
			StatementWords.clear();
		}
		catch (Exception e) {
			txtArea.setText("Statement incorrect!");
			System.out.println("Ne izgleda dobro statement");
			System.out.println(e.getLocalizedMessage());
		}
		return 0;
	}
	
	public int importDatabase(String str,TextArea txt1Area,TextArea txt2Area,TextArea txt3Area) {
		
		ArrayList<String> arrayOfStrings=new ArrayList<String>();
		String temp;
		int found=0;
		while(true) {
		if(str.contains(".txt") || str.contains(".sql")) {
			try {
				BufferedReader reader = new BufferedReader(new FileReader(new File("D:\\Users\\cofy\\eclipse-workspace\\proj\\src\\application\\"+str)));
				String line;
				while((line=reader.readLine())!=null) {
					String[] pomstr=line.split(" ");
					for (String string : pomstr) {
						arrayOfStrings.add(string);
					}
					if(pomstr.length==0) {
						System.out.println(line+"\n");
						arrayOfStrings.clear();
						continue;
					}
					if((arrayOfStrings.get(0).compareTo("CREATE")==0) && (arrayOfStrings.get(1).compareTo("TABLE")==0) && (arrayOfStrings.get(2).compareTo("IF")==0) && (arrayOfStrings.get(3).compareTo("NOT")==0) && (arrayOfStrings.get(4).compareTo("EXISTS")==0)) {
						temp=arrayOfStrings.get(5);
						arrayOfStrings.remove(2);
						arrayOfStrings.remove(3);
						arrayOfStrings.remove(4);
						
						for (int i=0;i<tables.size();i++) {
							if(tables.get(i).getName().compareTo(temp)==0) {
								found=1;
							}
						}
						if(found==0) {
							String string="";
							for (int i=0;i<arrayOfStrings.size();i++) {
								string+=arrayOfStrings.get(i);
								if(i<arrayOfStrings.size()-1) {
									string+=" ";
								}
							}
							tables.add(new Table(temp,string));
							System.out.println("Kreirana tabela preko importa: "+temp);
						}
						else {
							System.out.println("ERROR: tabela "+temp+" vec postoji");
						}
						
					}
					else if((arrayOfStrings.get(0).compareTo("CREATE")==0) && (arrayOfStrings.get(1).compareTo("TABLE")==0)) {
						temp=arrayOfStrings.get(2);
						for (int i=0;i<tables.size();i++) {
							if(tables.get(i).getName().compareTo(temp)==0) {
								found=1;
							}
						}
						if(found==0) {
							String string="";
							for (int i=0;i<arrayOfStrings.size();i++) {
								string+=arrayOfStrings.get(i);
								if(i<arrayOfStrings.size()-1) {
									string+=" ";
								}
							}
							tables.add(new Table(temp,string));
							System.out.println("Kreirana tabela preko importa: "+temp);
						}
						else {
							System.out.println("ERROR: tabela "+temp+" vec postoji");
						}
					}
					else if((arrayOfStrings.get(0).compareTo("INSERT")==0) && (arrayOfStrings.get(1).compareTo("INTO")==0)) {
						ins=new Insert(arrayOfStrings,line,tables,txt3Area);
						ins=null;
					}
					else if(arrayOfStrings.get(0).compareTo("UPDATE")==0) {
						upd=new Update(arrayOfStrings,line,tables,txt3Area);
						upd=null;
					}
					else if(arrayOfStrings.get(0).compareTo("DELETE")==0) {
						del=new Delete(arrayOfStrings,line,tables,txt3Area);
						del=null;
					}
					else if(arrayOfStrings.get(0).compareTo("SELECT")==0) {
						sel=new Select(arrayOfStrings,line,tables,txt2Area,txt3Area);
						sel=null;
					}
					System.out.println(line+"\n");
					arrayOfStrings.clear();
					found=0;
				}
				return 0;
			} catch (IOException e) {
				System.out.println(e.getLocalizedMessage());
				return -1;
			}
		}
		else {
			System.out.println("Format fajla nije dobar!");
		}
		}
	}
	
	public int exportTableInRegularFormat() {
		String str = getName()+".txt";
		File file=new File(str);
		String pom="";
		int success=0;
		try {
			if(file.createNewFile()) {
				success=1;
			}
			else {
				while(true) {
					pom+="new"+str;
					file=new File(pom);
					if(file.createNewFile()) {
						success=1;
						break;
					}
					System.out.println("File with this name exists, we are adding new to the name of file!");
				}
			}
			if(success==1) {
				ArrayList<Record> tmpRecords=new ArrayList<Record>();
				ArrayList<String> columns=new ArrayList<String>();
				ArrayList<Integer> gIds=new ArrayList<Integer>();
				
				FileWriter writer=new FileWriter(file);
				StringBuilder sb=new StringBuilder();
				sb.append("--Database name "+getName()+"\n");
				for(int i=0;i<tables.size();i++) {
					columns=tables.get(i).getAllColumnNames();
					sb.append("CREATE TABLE "+tables.get(i).getName()+" (");
					for(int j=0;j<columns.size();j++) {
						sb.append(columns.get(j));
						if(j<columns.size()-1) {
							sb.append(",");
						}
					}
					sb.append(")\n");
				}
				System.out.println(sb.toString());
				for(int i=0;i<tables.size();i++) {
					gIds=tables.get(i).getAllGIds();
					columns=tables.get(i).getAllColumnNames();
					for(int j=0;j<gIds.size();j++) {
						int gIdNum=gIds.get(j);
						tmpRecords=tables.get(i).getDataFromGId(gIdNum);
						sb.append("INSERT INTO "+tables.get(i).getName()+" (");
						for(int k=0;k<tmpRecords.size();k++) {
							if(tmpRecords.get(k).getGId()==gIdNum) {
								sb.append(tmpRecords.get(k).getColumnName());
							}
							if(k<tmpRecords.size()-1) {
								sb.append(",");
							}
						}
						sb.append(") VALUES (");
						for(int k=0;k<tmpRecords.size();k++) {
							if(tmpRecords.get(k).getGId()==gIdNum) {
								sb.append(tmpRecords.get(k).getValue());
							}
							if(k<tmpRecords.size()-1) {
								sb.append(",");
							}
						}
						sb.append(")\n");
					}
				}
				writer.write(sb.toString());
				writer.close();
			}
		} catch (IOException e) {
			System.out.println(e.getLocalizedMessage());
		}
		
		return 0;
	}
	
	public int exportTableInSQLFormat() {
		String str = getName()+".sql";
		File file=new File(str);
		String pom="";
		int success=0;
		try {
			if(file.createNewFile()) {
				success=1;
			}
			else {
				while(true) {
					pom+="new"+str;
					file=new File(pom);
					if(file.createNewFile()) {
						success=1;
						break;
					}
					System.out.println("File with this name exists, we are adding new to the name of file!");
				}
			}
			if(success==1) {
				ArrayList<Record> tmpRecords=new ArrayList<Record>();
				ArrayList<String> columns=new ArrayList<String>();
				ArrayList<Integer> gIds=new ArrayList<Integer>();
				
				FileWriter writer=new FileWriter(file);
				StringBuilder sb=new StringBuilder();
				sb.append("--Database name "+getName()+"\n");
				for(int i=0;i<tables.size();i++) {
					columns=tables.get(i).getAllColumnNames();
					sb.append("CREATE TABLE IF NOT EXISTS "+tables.get(i).getName()+" (");
					for(int j=0;j<columns.size();j++) {
						sb.append(columns.get(j)+" CHAR[50],\n");
					}
					sb.append("ID INTEGER PRIMARY KEY AUTOINCREMENT\n);\n\n");
				}
				System.out.println(sb.toString());
				for(int i=0;i<tables.size();i++) {
					gIds=tables.get(i).getAllGIds();
					columns=tables.get(i).getAllColumnNames();
					for(int j=0;j<gIds.size();j++) {
						int gIdNum=gIds.get(j);
						tmpRecords=tables.get(i).getDataFromGId(gIdNum);
						sb.append("INSERT INTO "+tables.get(i).getName()+" (");
						for(int k=0;k<tmpRecords.size();k++) {
							if(tmpRecords.get(k).getGId()==gIdNum) {
								sb.append(tmpRecords.get(k).getColumnName());
							}
							if(k<tmpRecords.size()-1) {
								sb.append(", ");
							}
						}
						sb.append(") VALUES (");
						for(int k=0;k<tmpRecords.size();k++) {
							if(tmpRecords.get(k).getGId()==gIdNum) {
								sb.append("'"+tmpRecords.get(k).getValue()+"'");
							}
							if(k<tmpRecords.size()-1) {
								sb.append(", ");
							}
						}
						sb.append(");\n");
					}
				}
				writer.write(sb.toString());
				writer.close();
			}
		} catch (IOException e) {
			System.out.println(e.getLocalizedMessage());
		}
		return 0;
	}
}
