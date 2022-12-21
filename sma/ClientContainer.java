package sma;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.gui.GuiEvent;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sma.agents.ClientAgent;

public class ClientContainer extends Application {
	private ObservableList<String> listLivre;
	private ClientAgent clientAgent;

	public static void main(String[] args) {
		launch(ClientContainer.class);
	}

	public void startConatiner() {
		try {
			Runtime runtime = Runtime.instance();
			Profile profile = new ProfileImpl(false);
			profile.setParameter(Profile.MAIN_HOST, "localhost");
			AgentContainer agentContainer = runtime.createAgentContainer(profile);
			AgentController agentController = agentContainer.createNewAgent("Client2", "sma.agents.ClientAgent",
					new Object[] { this });
			agentController.start();

		} catch (ControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//.setStyle("-fx-background-color: #46c414;");

	@Override
	public void start(Stage primaryStage) throws Exception {
		startConatiner();
		primaryStage.setTitle("Client");
		BorderPane borderPane = new BorderPane();		
		
		listLivre = FXCollections.observableArrayList();
		HBox hBox = new HBox();
		
		hBox.setStyle("-fx-background-color: #453935;");
		
		hBox.setPadding(new Insets(10));
		hBox.setSpacing(10);
		Label livre = new Label("chercher");
		
		livre.setTextFill(Color.web("#ffffff"));
		
		TextField textFieldLivre = new TextField();
		Button acheter = new Button("search");
		hBox.getChildren().add(livre);
		hBox.getChildren().add(textFieldLivre);
		hBox.getChildren().add(acheter);
		borderPane.setTop(hBox);
		VBox vBox = new VBox();
		GridPane gridPane = new GridPane();
		ListView<String> listView = new ListView<>(listLivre);
		listView.setMinSize(450, 600);
		gridPane.add(listView, 0, 0);
		vBox.getChildren().add(gridPane);
		borderPane.setCenter(vBox);
		borderPane.setStyle("-fx-background-color: #575451;");
		vBox.setPadding(new Insets(10));
		vBox.setSpacing(10);
		Scene scene = new Scene(borderPane,500, 700);
		primaryStage.setScene(scene);
		primaryStage.show();
		acheter.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				String livre = textFieldLivre.getText();
				GuiEvent guiEvent = new GuiEvent(this, 1);
				guiEvent.addParameter(livre);
				clientAgent.onGuiEvent(guiEvent);

			}
		});

	}

	public void insertMessage(GuiEvent guiEvent) {
		String livre = guiEvent.getParameter(0).toString();
		System.out.println("livre" + livre);
		listLivre.add(livre);
	}

	public ClientAgent getClienAgent() {
		return clientAgent;
	}

	public void setClienAgent(ClientAgent clientAgent) {
		this.clientAgent = clientAgent;
	}

}
