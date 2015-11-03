package madkit.desktop2;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

interface IconAdder{
	public void closeWindow();
	public void addNewIcon(IconInfo iconInfo);
	public void IconChanged(IconInfo iconInfo);
}

public class AddDesktopIcon extends JPanel implements ChangeListener, ActionListener{
	protected String[] iconTypes = {"java","Scheme","Python","Jess","BeanShell","SEditFormalism","SEditFile"};
	protected ImageViewer images;
	protected JTextField imagePath, imageJar, imageDim, iconLabel, iconDesc, iconCode;
	protected JComboBox iconType;
	protected JComboBox iconGUI;
	protected JButton ok, cancel, scroll;
	protected IconAdder iconAdder;
	protected IconInfo iconInfo;
	
	public AddDesktopIcon(IconAdder iconAdder){
		super(new BorderLayout());
		images = new ImageViewer();
		images.addChangeListener(this);
		this.add(images, BorderLayout.CENTER);
		this.iconAdder = iconAdder;
		
		imagePath = new JTextField("", 25);
		imagePath.setEditable(false);
		imageJar = new JTextField("", 18);
		imageJar.setEditable(false);
		imageDim = new JTextField("", 8);
		imageDim.setEditable(false);
		
		iconLabel = new JTextField("", 15);
		iconDesc = new JTextField("", 25);
		iconCode = new JTextField("", 20);
		iconType = new JComboBox(iconTypes);
		iconType.setSelectedIndex(0); // select the Java type by default
		iconGUI = new JComboBox();
		iconGUI.addItem("true");
		iconGUI.addItem("false");

		JPanel pane1 = new JPanel(new GridLayout(0, 1));
		this.add(pane1, BorderLayout.SOUTH);
		
		JPanel pane2 = new JPanel();
		pane2.add(imagePath);
		pane2.add(imageJar);
		pane2.add(imageDim);
		pane1.add(pane2);
		
		pane2 = new JPanel();
		JLabel label = new JLabel("Icon label: ");
		pane2.add(label);
		pane2.add(iconLabel);
		label = new JLabel("Icon description: ");
		pane2.add(label);
		pane2.add(iconDesc);
		pane1.add(pane2);
		
		pane2 = new JPanel();
		label = new JLabel("Icon code: ");
		pane2.add(label);
		pane2.add(iconCode);
		label = new JLabel("Icon type: ");
		pane2.add(label);
		pane2.add(iconType);
		label = new JLabel("Icon GUI: ");
		pane2.add(label);
		pane2.add(iconGUI);
		pane1.add(pane2);
		
		pane2 = new JPanel();
		ok = new JButton("Ok");
		ok.setEnabled(false);
		ok.addActionListener(this);
		pane2.add(ok);
		cancel = new JButton("Cancel");
		cancel.addActionListener(this);
		pane2.add(cancel);
		scroll = new JButton("Scroll to selection");
		scroll.setEnabled(false);
		scroll.addActionListener(this);
		pane2.add(scroll);
		pane1.add(pane2);
	}
	
	public void setIconInfo(IconInfo iconInfo){
		this.iconInfo = iconInfo;
		if (iconInfo != null){
			images.setSelectedIcon(iconInfo.getIcon());
			iconLabel.setText(iconInfo.getLabel());
			iconDesc.setText(iconInfo.getDescription());
			iconCode.setText(iconInfo.getCode());
		//	iconType.setText(iconInfo.getType());
			iconType.setSelectedItem(iconInfo.getType());
			if (iconInfo.getGUI().equalsIgnoreCase("false")) iconGUI.setSelectedItem("false");
		}
	}
	
	public void scrollToCurrentMiniature(){ images.scrollToCurrentMiniature(); }
	
	public void stateChanged(ChangeEvent e){
		Object o = e.getSource();
		if (o == images){
			RessourceInfo res = images.getSelectedRessource();
			if (res != null){
				ok.setEnabled(true);
				scroll.setEnabled(true);
				imagePath.setText(res.getResourceName());
				imageJar.setText(res.getJarName());
				
				Dimension dim = images.getIconDimension();
				if (dim != null){
					imageDim.setText((int)dim.getWidth() + " x " + (int)dim.getHeight());
				}
				else{
					imageDim.setText("");
				}
			}
			else{
				ok.setEnabled(false);
				scroll.setEnabled(false);
				imagePath.setText("");
				imageJar.setText("");
				imageDim.setText("");
			}
		}
	}
	public Dimension getPreferredSize(){ return new Dimension(500, 400); }
	
	public void actionPerformed(ActionEvent e){
		Object o = e.getSource();
		if (o == ok){
			if (iconInfo == null){
				if (images.getSelectedRessource() != null){
					iconInfo = new IconInfo();
					
					String str = imagePath.getText();
					if (!str.startsWith("/")) str = "/" + str;
					iconInfo.setIcon(str);
					
				//	str = iconType.getText();
					str = (String) iconType.getSelectedItem();
					if (str.equals("")) str = "Java";
					iconInfo.setType(str);
					
					iconInfo.setLabel(iconLabel.getText());
					iconInfo.setDescription(iconDesc.getText());
					iconInfo.setCode(iconCode.getText());
					iconInfo.setGUI(iconGUI.getSelectedItem().toString());
					
					iconAdder.addNewIcon(iconInfo);
				}
			}
			else{
				if (images.getSelectedRessource() != null){
					String str = imagePath.getText();
					if (!str.startsWith("/")) str = "/" + str;
					iconInfo.setIcon(str);
					
					//str = iconType.getText();
					str = (String) iconType.getSelectedItem();
					if (str.equals("")) str = "Java";
					iconInfo.setType(str);
					
					iconInfo.setLabel(iconLabel.getText());
					iconInfo.setDescription(iconDesc.getText());
					iconInfo.setCode(iconCode.getText());
					iconInfo.setGUI(iconGUI.getSelectedItem().toString());
					
					iconAdder.IconChanged(iconInfo);
				}
			}
			iconAdder.closeWindow();
		}
		else if(o == cancel){
			iconAdder.closeWindow();
		}
		else if(o == scroll){
			images.scrollToCurrentMiniature();
		}
	}
}