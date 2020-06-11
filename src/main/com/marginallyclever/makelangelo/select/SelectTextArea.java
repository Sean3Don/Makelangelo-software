package com.marginallyclever.makelangelo.select;

import java.awt.BorderLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class SelectTextArea extends Select {
	private JLabel label;
	private JTextArea field;
	private JScrollPane pane;
	
	public SelectTextArea(String labelKey,String defaultText) {
		super();
		
		label = new JLabel(labelKey,JLabel.LEADING);
		field = new JTextArea(defaultText,4,20);
		field.setLineWrap(true);
		field.setWrapStyleWord(true);
		field.setBorder(BorderFactory.createLoweredBevelBorder());
		field.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				validate();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				validate();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				validate();
			}
			
			void validate() {
				setChanged();
				notifyObservers();
			}
		});
		
		pane = new JScrollPane(field);
		
		// resize the jscrollpane if the containing panel resizes
		panel.addComponentListener(new ComponentListener() {
			@Override
			public void componentResized(ComponentEvent e) {
				pane.setSize(panel.getSize());
				pane.revalidate();
			}
			@Override
			public void componentMoved(ComponentEvent e) {}
			@Override
			public void componentShown(ComponentEvent e) {}
			@Override
			public void componentHidden(ComponentEvent e) {}
		});
		
		panel.add(label,BorderLayout.PAGE_START);
		panel.add(pane,BorderLayout.CENTER);
	}

	public String getText() {
		return field.getText();
	}
	
	public void setText(String str) {
		field.setText(str);
	}
}