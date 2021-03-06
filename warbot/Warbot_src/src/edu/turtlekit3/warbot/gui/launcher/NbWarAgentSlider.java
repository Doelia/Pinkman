package edu.turtlekit3.warbot.gui.launcher;

import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class NbWarAgentSlider extends JPanel {

	private JSpinner _nbUnitSpinner;
	private JSlider _nbUnitSlider;
	
	public NbWarAgentSlider(String title, int valMin, int valMax, int valInit, int step, int displayStep) {
		super();
		
		setLayout(new GridLayout(2, 1));
		
		JPanel pnlSup = new JPanel(new FlowLayout(FlowLayout.LEFT));
		pnlSup.add(new JLabel(title));
		SpinnerNumberModel spinnerModel = new SpinnerNumberModel(valInit, valMin, valMax, step);
		_nbUnitSpinner = new JSpinner(spinnerModel);
		JComponent editor = _nbUnitSpinner.getEditor();
		if (editor instanceof JSpinner.DefaultEditor)
			((JSpinner.DefaultEditor) editor).getTextField().setEditable(false);
		_nbUnitSpinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				_nbUnitSlider.setValue((int) _nbUnitSpinner.getValue());
			}
		});
		pnlSup.add(_nbUnitSpinner);
		add(pnlSup);
		
		_nbUnitSlider = new JSlider(JSlider.HORIZONTAL, valMin, valMax, valInit);
		_nbUnitSlider.setPaintLabels(true);
		_nbUnitSlider.setMajorTickSpacing(displayStep);
		_nbUnitSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				_nbUnitSpinner.setValue(_nbUnitSlider.getValue());
			}
		});
		add(_nbUnitSlider);
	}
	
	public int getSelectedValue() {
		return _nbUnitSlider.getValue();
	}
}
