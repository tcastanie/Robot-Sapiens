
/*
* Monitor.java - Graphics utilities for MadKit agents
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
package madkit.system;

import java.awt.Color;import java.awt.Dimension;import java.awt.Font;import java.awt.FontMetrics;import java.awt.Graphics;import java.awt.Graphics2D;import java.awt.Rectangle;import java.awt.geom.Line2D;import java.awt.geom.Rectangle2D;import java.awt.image.BufferedImage;import javax.swing.JPanel;

public class Monitor extends JPanel {

    private int w1;
    private int h1;
    private BufferedImage bufferedImage;
    private Graphics2D graphics;
    private Font font;
    private int i3;
    private float values[];
    private int i4;
    private int i5;
    private int i6;
    private float total=1F;
    private float value=0F;
    private String topString="K alloc";
    private String bottomString="K used";
    private float f2;
    private Rectangle rec1;
    private Rectangle2D rect2D1;
    private Rectangle2D rect2D2;
    private Line2D line2D;
    private Color color1;
    private Color color2;
    String warning="on";
    int warnSize=500;

    public Monitor() {
        font = new Font("Times New Roman", 0, 11);
        rec1 = new Rectangle();
        rect2D1 = new java.awt.geom.Rectangle2D.Float();
        rect2D2 = new java.awt.geom.Rectangle2D.Float();
        line2D = new java.awt.geom.Line2D.Float();
        color1 = new Color(46, 139, 87);
        color2 = new Color(0, 100, 0);
        //color2 = Color.cyan;
        setBackground(Color.black);
    }

    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    public Dimension getMaximumSize() {
        return getPreferredSize();
    }

    public Dimension getPreferredSize() {
        return new Dimension(140, 80);
    }

    public void setValues(float max, float val){
        total=max;
        value=val;
        redrawScreen();
    }

    public void setTopString(String s){topString = s;}
    public void setBottomString(String s){bottomString = s;}


    public void redrawScreen(){
        Dimension dimension = getSize();
        if(dimension.width != w1 || dimension.height != h1) {
            w1 = dimension.width;
            h1 = dimension.height;
            bufferedImage = (BufferedImage)createImage(w1, h1);
            graphics = bufferedImage.createGraphics();
            graphics.setFont(font);
            FontMetrics fontmetrics = graphics.getFontMetrics(font);
            i5 = fontmetrics.getAscent();
            i6 = fontmetrics.getDescent();
        }
        repaint();
    }

    public void paint(Graphics g) {
        if(graphics == null)
            return;
        graphics.setBackground(getBackground());
        graphics.clearRect(0, 0, w1, h1);
        graphics.setColor(Color.green);
        graphics.drawString(String.valueOf((int)Math.round(total))+topString, 4F, (float)i5 + 0.5F);
        graphics.drawString(String.valueOf((int)Math.round(value))+bottomString, 4, h1 - i6);
        graphics.setColor(Color.red);

        float f2 = i5 + i6;
        float f3 = (float)h1 - f2 * (float)2 - 0.5F;
        float f4 = f3 / (float)10;
        float f5 = 20F;
        float f6 = (float)w1 - f5 - (float)10;
        graphics.setColor(color2);
        int v = (int)(((total-value) / total) * (float)10);
        int j;
        for(j = 0; j < v; j++) {
            rect2D1.setRect(5D, f2 + (float)j * f4, f5, f4 - (float)1);
            graphics.fill(rect2D1);
        }

        graphics.setColor(Color.green);
        for(; j < 10; j++) {
            rect2D2.setRect(5D, f2 + (float)j * f4, f5, f4 - (float)1);
            graphics.fill(rect2D2);
        }

        graphics.setColor(color1);
        int k = 30;
        int l = (int)f2;
        int i1 = w1 - k - 5;
        int j1 = (int)f3;
        rec1.setRect(k, l, i1, j1);
        graphics.draw(rec1);
        int k1 = j1 / 10;
        for(int l1 = l; l1 <= j1 + l; l1 += k1) {
            line2D.setLine(k, l1, k + i1, l1);
            graphics.draw(line2D);
        }

        int i2 = i1 / 15;
        if(i3 == 0)
            i3 = i2;
        for(int j2 = k + i3; j2 < i1 + k; j2 += i2) {
            line2D.setLine(j2, l, j2, l + j1);
            graphics.draw(line2D);
        }

        i3--;
        if(values == null) {
            values = new float[i1];
            i4 = 0;
        } else
        if(values.length != i1) {
            float ai[] = null;
            if(i4 < i1) {
                ai = new float[i4];
                System.arraycopy(values, 0, ai, 0, ai.length);
            } else {
                ai = new float[i1];
                System.arraycopy(values, values.length - ai.length, ai, 0, ai.length);
                i4 = ai.length - 2;
            }
            values = new float[i1];
            System.arraycopy(ai, 0, values, 0, ai.length);
        } else {
            graphics.setColor(Color.magenta);
            values[i4] = (total-value)/ total;
            int index = (k + i1) - i4;
            for(int m = 0; m < i4;) {
                if(m != 0){
                    int vdispCurrent=(int)((float)l + (float)j1 * values[m]);
                    int vdispPrevious=(int)((float)l + (float)j1 * values[m-1]);
                    if(values[m] != values[m - 1])
                        graphics.drawLine(index - 1, vdispPrevious, index, vdispCurrent);
                    else
                        graphics.fillRect(index, vdispCurrent, 1, 1);
                }
                m++;
                index++;
            }

            if(i4 + 2 == values.length) {
                for(int m = 1; m < i4; m++)
                    values[m - 1] = values[m];

                i4--;
            } else {
                i4++;
            }
        }
        g.drawImage(bufferedImage, 0, 0, this);
    }
}
