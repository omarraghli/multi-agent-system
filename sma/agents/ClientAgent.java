package sma.agents;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.ControllerException;
import javafx.application.Platform;
import sma.ClientContainer;

public class ClientAgent extends GuiAgent {
	private ClientContainer gui;

	@Override
	protected void setup() {
		gui = (ClientContainer) getArguments()[0];
		gui.setClienAgent(this);
		System.out.println("creation et initialisation de l'agent client : " + this.getAID().getName());
		// ajouter un comportement cyclique
		// reception et affichage du msg entre agents
		addBehaviour(new CyclicBehaviour() {

			@Override
			public void action() {
				//affichage du résultat
				MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
				ACLMessage aclMessage = receive(messageTemplate);
				if (aclMessage != null) {
					// create event from this agent to the ui
					GuiEvent guiEvent = new GuiEvent(this, 1);
					guiEvent.addParameter(aclMessage.getContent());
					Platform.runLater(() -> {
						gui.insertMessage(guiEvent);
					});
				} else {
					block();
				}
			}
		});
	}

	@Override
	protected void beforeMove() {
		try {
			System.out.println("avant migration de l'agent " + this.getAID().getName());
			System.out.println("de " + this.getContainerController().getContainerName());
		} catch (ControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void afterMove() {
		try {
			System.out.println("apres migration de l'agent " + this.getAID().getName());
			System.out.println("vers " + this.getContainerController().getContainerName());
		} catch (ControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void takeDown() {
		System.out.println("l'agent : " + this.getAID().getName() + " va mourir");
	}

	@Override
	public void onGuiEvent(GuiEvent guiEvent) {
		if (guiEvent.getType() == 1) {
			ACLMessage aclMessage = new ACLMessage(ACLMessage.REQUEST);
			String restaurant = guiEvent.getParameter(0).toString();
			aclMessage.setContent(restaurant);
			aclMessage.addReceiver(new AID("Manager1", AID.ISLOCALNAME));
			send(aclMessage);
		}

	}
}
