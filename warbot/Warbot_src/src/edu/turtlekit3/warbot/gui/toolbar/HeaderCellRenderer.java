package edu.turtlekit3.warbot.gui.toolbar;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableCellRenderer;

@SuppressWarnings("serial")
public class HeaderCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
 
        setBackground(Color.LIGHT_GRAY);
        setIcon(null);
        setText(value.toString());
        setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        
        return this;
    }
}