package sma.agents;

import java.util.ArrayList;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import javafx.application.Platform;
import sma.RestaurantContainer;

import sma.behavouirs.RestaurantBehavouirs;

public class RestaurantAgent extends GuiAgent {

	private String libelle;
	private int capacite;
	private int nbplacesDispo;

	private ParallelBehaviour parallelBehaviour;
	private RestaurantContainer gui;

	public int getNbplacesDispo() {
		return nbplacesDispo;
	}

	public void setNbplacesDispo(int nbplacesDispo) {
		this.nbplacesDispo = nbplacesDispo;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public int getCapacite() {
		return capacite;
	}

	public void setCapacite(int capacite) {
		this.capacite = capacite;
	}

	@Override
	protected void setup() {
		// get the input
		gui = (RestaurantContainer) getArguments()[0];
		gui.setRestaurantAgent(this);
		capacite = (int) (10 + Math.random() * 60);
		System.out.println("random generation" + capacite);
		nbplacesDispo = capacite;

		System.out.println("....... Restaurant " + this.getAID().getName());
		System.out.println("....... capacité " + capacite);
		GuiEvent guiEvent = new GuiEvent(this, 1);
		guiEvent.addParameter("....... capacité " + capacite);
		Platform.runLater(() -> {
			gui.insertMessage(guiEvent);
		});
		// gui.insertMessage(guiEvent);
		System.out.println("--------------");
		System.out.println("Publication du service dans Directory Facilitator...");
		DFAgentDescription agentDescription = new DFAgentDescription();
		agentDescription.setName(this.getAID());
		ServiceDescription serviceDescription = new ServiceDescription();
		serviceDescription.setType("offre-restaurant");
		serviceDescription.setName("offre-diner");
		agentDescription.addServices(serviceDescription);
		try {
			DFService.register(this, agentDescription);
		} catch (FIPAException e1) {
			e1.printStackTrace();
		}
		parallelBehaviour = new ParallelBehaviour();
		addBehaviour(parallelBehaviour);
		parallelBehaviour.addSubBehaviour(new CyclicBehaviour() {

			@Override
			public void action() {

				try {

					MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(ACLMessage.CFP);
					ACLMessage aclMessage = receive(messageTemplate);
					if (aclMessage != null) {
						if (nbplacesDispo != 0) {
							System.out.println("Conversation ID:" + aclMessage.getConversationId());
							String message = aclMessage.getContent();

							ACLMessage reply = aclMessage.createReply();
							reply.setPerformative(ACLMessage.PROPOSE);
							reply.setContent(String.valueOf(capacite));

							GuiEvent guiEvent = new GuiEvent(this, 1);
							guiEvent.addParameter("demande de :" + aclMessage.getSender().getLocalName()
									+ " a propos : " + message + " nb de place disp :" + nbplacesDispo);
							Platform.runLater(() -> {
								gui.insertMessage(guiEvent);
							});
							//gui.insertMessage(guiEvent);

							System.out.println("...... En cours");
							Thread.sleep(5000);
							send(reply);

							parallelBehaviour.addSubBehaviour(
									new RestaurantBehavouirs(myAgent, aclMessage.getConversationId(), gui));
						} else
							takeDown();
					} else {
						block();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

	}

	@Override
	public void beforeMove() {
		// TODO Auto-generated method stub
		super.beforeMove();
	}

	@Override
	public void afterMove() {
		// TODO Auto-generated method stub
		super.afterMove();
	}

	@Override
	public void takeDown() {
		GuiEvent guiEvent = new GuiEvent(this, 1);
		guiEvent.addParameter("le restaurant : " + this.getAID().getLocalName() + " est saturé");
		Platform.runLater(() -> {
			gui.insertMessage(guiEvent);
		});
		//gui.insertMessage(guiEvent);

		try {
			DFService.deregister(this);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
	}

	public RestaurantContainer getGui() {
		return gui;
	}

	public void setGui(RestaurantContainer gui) {
		this.gui = gui;
	}

	@Override
	protected void onGuiEvent(GuiEvent guiEvent) {

	}

}
