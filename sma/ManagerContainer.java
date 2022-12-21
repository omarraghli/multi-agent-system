package sma;

import java.util.Iterator;

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
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sma.agents.ManagerAgent;

public class ManagerContainer extends Application {
	private ObservableList<String> liste;
	private ManagerAgent ManagerAgent;

	public static void main(String[] args) {
		launch(ManagerContainer.class);
	}

	public void startConatiner() {
		try {
			Runtime runtime = Runtime.instance();
			Profile profile = new ProfileImpl(false);
			profile.setParameter(Profile.MAIN_HOST, "localhost");
			AgentContainer agentContainer = runtime.createAgentContainer(profile);
			AgentController agentController = agentContainer.createNewAgent("Manager1", "sma.agents.ManagerAgent",
					new Object[] { this });
			agentController.start();

		} catch (ControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		startConatiner();
		primaryStage.setTitle("Manager");
		BorderPane borderPane = new BorderPane();
		liste = FXCollections.observableArrayList();

		VBox vBox = new VBox();
		GridPane gridPane = new GridPane();
		ListView<String> listView = new ListView<>(liste);
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

	}

	public void insertMessage(GuiEvent guiEvent) {
		Iterator<String> it = guiEvent.getAllParameter();
		while (it.hasNext()) {

			String msg = it.next();
			liste.add(msg);
		}
	}

	public ManagerAgent getManagerAgent() {
		return ManagerAgent;
	}

	public void setManagerAgent(ManagerAgent ManagerAgent) {
		this.ManagerAgent = ManagerAgent;
	}

}
