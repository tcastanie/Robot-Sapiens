package madkit.share;
import java.awt.Graphics;


interface vList 
{

  public void clear();

 public void addItem(String item, String pathitem);

  public int countItems();

    //public void paintComponent(Graphics g);

    public void paint(Graphics g);
}
