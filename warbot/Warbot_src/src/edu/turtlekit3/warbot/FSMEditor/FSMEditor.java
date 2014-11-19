package edu.turtlekit3.warbot.FSMEditor;

import edu.turtlekit3.warbot.FSMEditor.Modele.Modele;

public class FSMEditor {
	
	public FSMEditor() {
		Modele modele = new Modele();
		
		Frame vu = new Frame(modele);
		
		Controleur controleur = new Controleur(modele, vu);
		
	}

}
