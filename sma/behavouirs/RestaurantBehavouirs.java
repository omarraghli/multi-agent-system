package sma.behavouirs;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import javafx.application.Platform;
import sma.RestaurantContainer;
import sma.agents.RestaurantAgent;

public class RestaurantBehavouirs extends CyclicBehaviour {
	private String conversationID;
	RestaurantAgent restaurantAgent;
	RestaurantContainer gui;

	public RestaurantBehavouirs(Agent agent, String conversationID, RestaurantContainer gui) {
		super(agent);
		restaurantAgent = (RestaurantAgent) agent;
		this.conversationID = conversationID;
		this.gui = gui;
	}

	@Override
	public void action() {
		try {

			MessageTemplate messageTemplate = MessageTemplate.and(MessageTemplate.MatchConversationId(conversationID),
					MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL));
			ACLMessage aclMessage = myAgent.receive(messageTemplate);

			if (aclMessage != null) {
				restaurantAgent.setNbplacesDispo(restaurantAgent.getNbplacesDispo() - 1);
				System.out.println("--------------------------------");
				System.out.println("Conversation ID:" + aclMessage.getConversationId());
				System.out.println("Validation de la transaction .....");
				ACLMessage reply2 = aclMessage.createReply();
				reply2.setPerformative(ACLMessage.CONFIRM);
				reply2.setContent(String.valueOf(restaurantAgent.getNbplacesDispo()));
				GuiEvent guiEvent = new GuiEvent(this, 1);
				guiEvent.addParameter("##############Confirmation#############");
				Platform.runLater(() -> {
					gui.insertMessage(guiEvent);
				});
				// gui.insertMessage(guiEvent);
				System.out.println("...... En cours");
				Thread.sleep(5000);
				myAgent.send(reply2);

				System.out.println(restaurantAgent.getNbplacesDispo());

			}

			else {
				block();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
