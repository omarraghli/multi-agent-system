package sma;

import java.util.Scanner;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.gui.GuiEvent;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import sma.agents.RestaurantAgent;

public class RestaurantContainer extends Application {
	private ObservableList<String> listRestaurant;
	private RestaurantAgent restaurantAgent;
	private AgentContainer agentContainer;

	public static void main(String[] args) {
		launch(RestaurantContainer.class);
	}

	public void startConatiner() {
		try {
			Runtime runtime = Runtime.instance();
			Profile profile = new ProfileImpl(false);
			profile.setParameter(Profile.MAIN_HOST, "localhost");
			agentContainer = runtime.createAgentContainer(profile);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
//.setStyle("-fx-background-color: #a61414;"); 
//.setTextFill(Color.web("#ffffff"));
	@Override
	public void start(Stage primaryStage) throws Exception {
		startConatiner();
		primaryStage.setTitle("Restaurant");
		BorderPane borderPane = new BorderPane();
		listRestaurant = FXCollections.observableArrayList();
		HBox hBox = new HBox();
		hBox.setStyle("-fx-background-color: #453935;");
		hBox.setPadding(new Insets(10));
		hBox.setSpacing(10);
		
		Label Restaurant = new Label("Nom restaurant");
		Restaurant.setTextFill(Color.web("#ffffff"));
		
		TextField textFieldRestaurant = new TextField();
		Button btnRestaurant = new Button("Deployer agent");
		hBox.getChildren().add(Restaurant);
		hBox.getChildren().add(textFieldRestaurant);
		hBox.getChildren().add(btnRestaurant);
		borderPane.setTop(hBox);
		borderPane.setStyle("-fx-background-color: #575451;");
		VBox vBox = new VBox();
		GridPane gridPane = new GridPane();
		ListView<String> listView = new ListView<>(listRestaurant);
		listView.setMinSize(450, 600);
		gridPane.add(listView, 0, 0);
		vBox.getChildren().add(gridPane);
		borderPane.setCenter(vBox);		
		
		vBox.setPadding(new Insets(10));
		vBox.setSpacing(10);
		Scene scene = new Scene(borderPane,500, 700);
		primaryStage.setScene(scene);
		primaryStage.show();
		btnRestaurant.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String agentName = textFieldRestaurant.getText();
				AgentController agentController;
				try {
					agentController = agentContainer.createNewAgent(agentName, "sma.agents.RestaurantAgent",
							new Object[] { RestaurantContainer.this });
					agentController.start();
				} catch (StaleProxyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

	public void insertMessage(GuiEvent guiEvent) {
		String message = guiEvent.getParameter(0).toString();
		System.out.println("insert message" + message);
		listRestaurant.add(message);
	}

	public RestaurantAgent getRestaurantAgent() {
		return restaurantAgent;
	}

	public void setRestaurantAgent(RestaurantAgent restaurantAgent) {
		this.restaurantAgent = restaurantAgent;
	}

}
