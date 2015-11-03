package enveditor;

import madkit.kernel.*;
public class Car {

    char   c;
    String name="";

    public Car(char c, String name)
    {
	this.c=c;
	this.name=name;
    }

    public void write()
    {
	System.out.println("car. "+c+" = "+name);
    }

}
