/*
* SplashScreen.java - Graphics utilities for MadKit agents
* Copyright (C) 1998-2002  Olivier Gutknecht
*
* This library is free software; you can redistribute it and/or
* modify it under the terms of the GNU Lesser General Public
* License as published by the Free Software Foundation; either
* version 2.1 of the License, or (at your option) any later version.

* This library is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.

* You should have received a copy of the GNU Lesser General Public
* License along with this library; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
*/
package madkit.kernel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.net.URL;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class SplashScreen extends JFrame
{
  String defaultname = "/images/neomadkit_small.jpg";
  JLabel progressLabel;
  JProgressBar progressBar;

  int currentProgressValue ;
  public static int INITIAL_WIDTH = 400;
  public static int INITIAL_HEIGHT = 200;
  public static int MAX_PROGRESS_VALUE = 10;


  public SplashScreen (String filename)
  {
    super("MadKit/Desktop");
    currentProgressValue = 0;

    getContentPane().setLayout(new BoxLayout(getContentPane(),BoxLayout.Y_AXIS));
    getContentPane().setBackground (Color.white);

    JPanel progressPanel = new JPanel() {
      public Insets getInsets() {
	return new Insets(20,20,20,20);
      }
      };


    try
      {
	URL url;
	if (filename != null)
	  url = this.getClass().getResource(filename);
	else
	  url = this.getClass().getResource(defaultname);

	ImageIcon ii = new ImageIcon(url);

	/*	try
	  {
	    MediaTracker tracker = new MediaTracker(this);

	    tracker.addImage(image, 0);
	    tracker.waitForID(0);
	    w = image.getWidth(this);
	    h = image.getHeight(this);
	    setSize(w,h);
	  }
	catch (Exception e) { e.printStackTrace();
	}*/

	while (ii.getImageLoadStatus()!=MediaTracker.COMPLETE);
	JLabel imageLabel = new JLabel(ii);
	imageLabel.setAlignmentX(CENTER_ALIGNMENT);
	getContentPane().add(Box.createVerticalStrut(10));
        getContentPane().add(imageLabel);
      }
    catch (Exception e) { e.printStackTrace(); }


    JLabel authors =
      new JLabel("   (c) 1997-2002, O. Gutknecht, J. Ferber & F. Michel");
    authors.setBackground(Color.white);
    authors.setAlignmentX(CENTER_ALIGNMENT);
    getContentPane().add(Box.createVerticalStrut(10));
    getContentPane().add(authors);

    JLabel version =
      new JLabel("Version " + madkit.kernel.Kernel.VERSION);
    version.setBackground(Color.white);
    version.setAlignmentX(CENTER_ALIGNMENT);
    //getContentPane().add(Box.createVerticalStrut(10));
    getContentPane().add(version);

    progressPanel.setLayout(new BoxLayout(progressPanel,BoxLayout.Y_AXIS));
    progressPanel.setBackground (Color.white);
    //  progressPanel.setBorder(new LineBorder(Color.darkGray, 2));


    //    Dimension d = new Dimension(400, 20);
    progressLabel = new JLabel("          Loading, please wait          ");
    //                                Starting G-Box user interface...
    progressLabel.setAlignmentX(CENTER_ALIGNMENT);
    //progressLabel.setMaximumSize(d);
    //progressLabel.setPreferredSize(d);
    progressPanel.add(progressLabel);
    progressPanel.add(Box.createRigidArea(new Dimension(1,10)));

    progressBar = new JProgressBar();
    progressLabel.setLabelFor(progressBar);
    progressBar.setAlignmentX(CENTER_ALIGNMENT);
    progressBar.setMinimum(0);
    progressBar.setMaximum(MAX_PROGRESS_VALUE);
    progressBar.setValue(0);
    progressPanel.add(progressBar);

    // show the frame
    //setSize(INITIAL_WIDTH, INITIAL_HEIGHT);
     Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    setLocation(screenSize.width/2 - INITIAL_WIDTH/2,
		screenSize.height/2 - INITIAL_HEIGHT/2);

    getContentPane().add(progressPanel);
    pack();
try{
	Thread.sleep(200);
	}
 catch (Exception e) {
   System.err.println("Err"+e);
 }

    show();
    //
    //  setBackground(Color.white);
    /*
     try
      {
	URL url;

	if (filename != null)
	  url = this.getClass().getResource(filename);
	else
	  url = this.getClass().getResource(defaultname);

	legend = new Label("");
	legend.setBackground(Color.white);
	System.err.println(url);

	ImageCanvas ic = new ImageCanvas(getToolkit().getImage(url),
					 "MadKit "+
					 madkit.kernel.Kernel.VERSION+
					 " / G-Box "+BeanBoxFrame.versionID);
	add("Center",ic);
	add("South",legend);

	pack();
	setVisible(true);
	Thread.sleep(2000);

      }
    catch (Exception e)
      {
	e.printStackTrace();
	}*/
  }

  public void setText(String s)
  {
    progressLabel.setText(s);
    progressBar.setValue(++currentProgressValue);
  }

}
