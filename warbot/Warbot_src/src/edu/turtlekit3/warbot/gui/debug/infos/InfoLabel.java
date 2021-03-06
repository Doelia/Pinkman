package edu.turtlekit3.warbot.gui.debug.infos;

import javax.swing.JLabel;

@SuppressWarnings("serial")
public class InfoLabel extends JLabel {

	private String _label;
	private String _value;
	
	public InfoLabel(String label) {
		super();
		_label = label;
		setText(_label + " : ");
	}
	
	public void setValue(String value) {
		_value = value;
		setText(_label + " : " + _value);
	}
}
