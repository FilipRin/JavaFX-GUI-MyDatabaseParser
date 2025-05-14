package application;


import java.util.Optional;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.ColorInput;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
 
public class Main extends Application {
    
    private Database d=null;
    private static Stage GUIstage;
    private int created=0;
    private Boolean saved=false;
    
    @Override
    public void start(Stage primaryStage) {
    	GUIstage=primaryStage;
    	//POCETNA SCENA, PRAVIMO NOVU BAZU ILI IMPORTUJEMO POSTOJECU
        Label confirmation=new Label();
        TextField tf1=new TextField();
        TextField tf2=new TextField();
        Button submit = new Button("Submit");
        tf1.setDisable(true);
        tf2.setDisable(true);
        ToggleGroup group = new ToggleGroup();  
        RadioButton button1 = new RadioButton("New database");  
        RadioButton button2 = new RadioButton("Existing database");
        button1.setToggleGroup(group);
        button2.setToggleGroup(group);
        
        StackPane root = new StackPane();
        final GridPane inputGridPane = new GridPane();
        Scene scene = new Scene(root, 600, 500);
        
        TextArea txt1Area=new TextArea();
        TextArea txt2Area=new TextArea();
        TextArea txt3Area=new TextArea();
        
        //NEIZABRANE OPCIJE IMAJU TEXT POLJA DISABLED
        button1.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
				if(button1.isSelected()) {
					tf1.setDisable(false);
				}
				else{
					tf1.setDisable(true);
				}
			}
        });
        
        button2.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
				if(button2.isSelected()) {
					tf2.setDisable(false);
				}
				else{
					tf2.setDisable(true);
				}	
			}
        });
        
        //POTVRDA UNOSA NAZIVA BAZE/IMENA FAJLA I UKOLIKO JE SVE U REDU IDEMO NA SCENU GLAVNOG PROGRAMA
        submit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if(tf1.isDisabled()) {
					System.out.println("Otvaranje postojeceg fajla");
					d=new Database(tf2.getText());
					changeSceneToMainProgram(txt1Area,txt2Area,txt3Area);
					d.importDatabase(tf2.getText(),txt1Area,txt2Area,txt3Area);
					created=1;
				}
				else {
					d = new Database(tf1.getText());
					confirmation.setText(tf1.getText() + " database is created!");
					tf1.setDisable(true);
					button1.setDisable(true);
					tf2.setDisable(true);
					button2.setDisable(true);
					changeSceneToMainProgram(txt1Area,txt2Area,txt3Area);
					created=1;
				}
			}
        });
        
        //AKO BAZA NIJE KREIRANA ILI UCITANA MI SMO NA POCETNOJ SCENI
        if(created==0) {
        inputGridPane.setAlignment(Pos.CENTER);
        GridPane.setConstraints(button1, 0, 0);
        GridPane.setConstraints(button2, 0, 10);
        GridPane.setConstraints(tf1, 10, 0);
        GridPane.setConstraints(tf2, 10, 10);
        GridPane.setConstraints(submit, 5, 20);
        GridPane.setConstraints(confirmation, 5, 30);
        inputGridPane.setHgap(3);
        inputGridPane.setVgap(3);
        inputGridPane.getChildren().addAll(button1,button2,tf1,tf2,submit,confirmation);
        
        root.getChildren().addAll(inputGridPane);
        root.setPadding(new Insets(12, 12, 12, 12));
        
        GUIstage.setTitle("Database program");
        GUIstage.setScene(scene);
        GUIstage.show();
        }
        
        Main.getStage().setOnCloseRequest(event -> {
    		event.consume();
    		quit(getStage());
    	}); 
    }
    
    public static Stage getStage() {
    	return GUIstage;
    }
    
    //FUNKCIJA ZA PROMENU I KREIRANJE NOVE SCENE KOJA PREDSTAVLJA GLAVNI PROGRAM
    public void changeSceneToMainProgram(TextArea txt1Area,TextArea txt2Area,TextArea txt3Area) {
    	Text txt1=new Text("All tables: ");
    	
    	txt1Area.setMaxHeight(70);
    	txt1Area.setMaxWidth(300);
    	txt1Area.setEditable(false);
    	txt1Area.setFont(new Font(12).font("Verdana", FontWeight.BOLD, 12));
    	Text txt2=new Text("Results of currently selected table: ");
    	
    	txt2Area.setMaxHeight(200);
    	txt2Area.setMaxWidth(350);
    	txt2Area.setEditable(false);
    	txt2Area.setStyle("-fx-font-family: monospace");
    	Text txt3=new Text("Enter a statement: ");
    	TextField stmnt=new TextField();
    	stmnt.setMaxWidth(300);
    	Button stmntClick= new Button("->");
    	
    	txt3Area.setMaxWidth(300);
    	txt3Area.setMaxHeight(20);
    	txt1Area.setEditable(false);
    	txt3Area.setFont(new Font(12).font("Verdana", FontWeight.BOLD, 12));
    	txt3Area.setText("Database "+d.getName()+" created!");
    	
    	Button expUser= new Button("Export in user format");
    	expUser.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				d.exportTableInRegularFormat();
				saved=true;
			}  
    	});
    	
    	Button expSql= new Button("Export in SQL format");
    	expSql.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				d.exportTableInSQLFormat();
				saved=true;
			}
    	});
    	
    	//izvrsavanje statementa na pritisak dugmeta ->
    	stmntClick.setOnAction(new EventHandler<ActionEvent>() {
    		int x=0;
			@Override
			public void handle(ActionEvent arg0) {
				x=d.executeStatement(stmnt.getText(),txt3Area,txt1Area,txt2Area,saved);
			}
		});
    	
    	GridPane grid=new GridPane();
    	grid.setMaxSize(900, 500);
    	grid.setPadding(new Insets(10, 10, 10, 10));
    	grid.setVgap(20); 
        grid.setHgap(5);
        grid.setAlignment(Pos.TOP_CENTER);
        grid.add(txt1, 0, 0);
        grid.add(txt1Area, 1, 0);
        grid.add(txt2, 0, 1);
        grid.add(txt2Area, 1, 1);
        grid.add(txt3, 0, 2);
        grid.add(stmnt, 1, 2);
        grid.add(stmntClick, 2, 2);
        grid.add(txt3Area, 3, 2);
        
        VBox box= new VBox(10,grid,expUser,expSql);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(10, 10, 10, 10));
        
    	Scene newScene=new Scene(box,1200,600);
    	newScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
    	Main.getStage().setTitle("Database "+d.getName());
    	Main.getStage().setScene(newScene);
    	Main.getStage().show();
    	
    	Main.getStage().setOnCloseRequest(event -> {
    		event.consume();
    		quit(getStage(),txt3Area);
    	});    	
    }
    
    ButtonType yes= new ButtonType("YES", ButtonBar.ButtonData.OK_DONE);
    ButtonType no= new ButtonType("NO", ButtonBar.ButtonData.CANCEL_CLOSE);
    public void quit(Stage stage,TextArea txtArea) {
    	Alert alert = new Alert(AlertType.CONFIRMATION,"Are you sure u want to exit?",yes,no);
		alert.setTitle("Confirmation Dialog");
		alert.setContentText("Are you sure u want to exit?");
		if(alert.showAndWait().get() == yes) {
			if(saved) {
				stage.close();
			}
			else {
				//alert koji pita da li zelimo sacuvati fajl
				//ako da onda se poziva npr userformat export i zatim se gasi
				//ako ne onda se gasi odmah
				Alert alert2 = new Alert(AlertType.CONFIRMATION,"Are you sure u want to exit?",yes,no);
				alert2.setTitle("Confirmation Dialog");
				alert2.setContentText("File not saved before quitting! Are you sure that you dont want to save this file?");
				if(alert2.showAndWait().get() == yes) {
					stage.close();
				}
				else {
					txtArea.setText("Save file before quitting!");
				}
			}
		}
    }
    
    public void quit(Stage stage) {
    	Alert alert = new Alert(AlertType.CONFIRMATION,"Are you sure u want to exit?",yes,no);
		alert.setTitle("Confirmation Dialog");
		alert.setContentText("Are you sure u want to exit?");
		if(alert.showAndWait().get() == yes) {
			stage.close();
		}
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
}