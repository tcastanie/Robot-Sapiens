/*
 * LoggerConfigurator.javaCreated on Dec 5, 2003
 *
 * Copyright (C) 2003 Sebastian Rodriguez
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.

 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 *Last Update $Date: 2003/12/17 16:33:14 $

 */
package madkit.netcomm;



import java.util.Properties;

//import org.apache.log4j.PropertyConfigurator;

/**Configures the netcomm logger. By default the logger's level is set to INFO
 * for all the the netcomm agents.<br>
 * If you want to define your own loggers configuration, you can create your configuration
file and set the path by defining the madkit.netcomm.log4jconfig property. <br>
 * @author Sebastian Rodriguez - sebastian.rodriguez@utbm.fr
 *	@version $Revision: 1.1 $
 */
class LoggerConfigurator {

	/**
	 * Configures the netcomm logger.
	 */
	static	public	void configure() {
		String config=System.getProperty("madkit.netcomm.log4jconfig");
		if(config==null || config.equalsIgnoreCase("")){
			Properties prop=new Properties();
			//Set root logger level to DEBUG and its only appender to A1.
			prop.setProperty("log4j.rootLogger","INFO, A1");

			//A1 is set to be a ConsoleAppender.
			prop.setProperty("log4j.appender.A1","org.apache.log4j.ConsoleAppender");

			// A1 uses PatternLayout.
			prop.setProperty("log4j.appender.A1.layout","org.apache.log4j.PatternLayout");
			prop.setProperty("log4j.appender.A1.layout.ConversionPattern","%d [%t] %-5p %c - %m%n");
			prop.setProperty("log4j.logger.madkit.netcomm","INFO");
			
		//	PropertyConfigurator.configure(prop);
		}else{
		//	PropertyConfigurator.configure(config);
		}
			
	}
}
