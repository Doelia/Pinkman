package edu.turtlekit3.warbot.FSMEditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import edu.turtlekit3.warbot.FSMEditor.Modele.Modele;
import edu.turtlekit3.warbot.FSMEditor.Modele.ModeleCondition;
import edu.turtlekit3.warbot.FSMEditor.Modele.ModeleState;
import edu.turtlekit3.warbot.FSMEditor.Panel.PanelCondition;
import edu.turtlekit3.warbot.FSMEditor.Panel.PanelState;
import edu.turtlekit3.warbot.FSMEditor.dialogues.DialogueCondSetting;
import edu.turtlekit3.warbot.FSMEditor.dialogues.DialogueStateSetting;

public class Controleur {

	public Modele modele;
	public Frame view;
	
	public Controleur(Modele modele, Frame vu) {
		this.modele = modele;
		this.view = vu;
		
		init();
	}
	
	private void init(){
		
		MouseListenerPanelCenter mouseListener = new MouseListenerPanelCenter(this);
		view.getPanelCenter().addMouseListener(mouseListener);
		view.getPanelCenter().addMouseMotionListener(mouseListener);
		
		view.getButtonAddSate().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				addSate();
			}
		});
		
		view.getButtonAddCond().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				addCond();
			}
		});
		
		view.getButtonDelState().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				delState();
			}
		});
		
		view.getButtonEditCond().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				editCond();
			}
		});
		
		view.getListeCondition().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				listeConditionEdition(e);
			}
		});
		
		//Pour debuger
		addSate();addSate();addCond();
	}
	
	private void listeConditionEdition(ListSelectionEvent e){
		//Deselctionne tous les elements
		for (PanelCondition p : this.view.getPanelCenter().getPanelcondition()) {
			p.isSelected = false;
		}
		
		String stringCond = view.getListeCondition().getSelectedValue();
		PanelCondition panelCond = this.getPanelConditionWithName(stringCond);
		
		panelCond.isSelected = true;
		
		this.view.getPanelCenter().repaint();
	}
	
	private void addSate(){
		DialogueStateSetting d = new DialogueStateSetting(this.view); 
		
		//creation du modele
		ModeleState s = new ModeleState(d);
		this.modele.addState(s);
		
		//Ajoute du panel
		PanelState panel = new PanelState(s);
		this.view.getPanelCenter().addState(panel);
		
		view.getPanelCenter().repaint();
	}
	
	private void addCond(){
		
		if(this.view.getPanelCenter().isTwoStatesSelected()){
			
			DialogueCondSetting d = new DialogueCondSetting(this.view);
			
			PanelState panelSource;
			PanelState panelDest;
			ModeleState modeleSource;
			ModeleState modeleDest;
			
			panelSource = this.view.getPanelCenter().getFirstSelectedState();
			panelDest = this.view.getPanelCenter().getSecondeSelectedState();
			modeleSource = panelSource.getModele();
			modeleDest = panelDest.getModele();
			
			//Cr�e un nouveau modele condition et le donne au modele
			ModeleCondition mc = new ModeleCondition(d);
			this.modele.addCondition(mc);
			
			//Cr�e un nouveau panel condition et le donne au panel
			PanelCondition pc = new PanelCondition(mc);
			this.view.getPanelCenter().addCondition(pc);
			
			//Donne au modele source le modele condition
			modeleSource.addCondition(mc);
			//Donne au modele condition source le modele destination
			mc.setDestination(modeleDest);
			
			//Donne au panel condition le panel state source et destination
			pc.setPanelSourceAndDestination(panelSource, panelDest);
			
			//Met a jour la liste de conditions dans la vu
			this.view.getListeModeleConditions().addElement(mc.getNom());
			
		}else{
			System.out.println("Pour ajouter une condition deux etats doivent etre selectionn�s");
		}
		
		view.getPanelCenter().repaint();
	}
	
	private void delState(){
		if(this.view.getPanelCenter().isOneStateSelected()){
			
			PanelState panelToDelet = this.view.getPanelCenter().getFirstSelectedState();
			
			//ATTENTION : supprimer le modele avant le panel puisque on utilise le panel pour acceder au modele
			this.modele.removeState(panelToDelet.getModele());
			this.view.getPanelCenter().removePanelState(panelToDelet);
			
			this.view.getPanelCenter().setNoItemSelected();
			
			view.getPanelCenter().repaint();
		}
		
	}

	private void editCond(){
		String condSelec = this.view.getListeConditions().getSelectedValue();
		
		if(condSelec != null){
			
			ModeleCondition modeleCond;
			
			for (ModeleCondition modeleC : this.modele.getConditions()) {
				if(modeleC.getNom().equals(condSelec)){
					modeleCond = modeleC;
					break;
				}
			}
			
			if(condSelec.equals(Configuration.WarConditionActionTerminate)){
				
				
				
			}
		}
	}
	
	private PanelCondition getPanelConditionWithName(String s){
		for (PanelCondition p : this.view.getPanelCenter().getPanelcondition()) {
			if(p.getModele().getNom().equals(s))
				return p;
		}
		return null;
	}
	

}
