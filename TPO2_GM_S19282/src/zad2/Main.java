/**
 *
 *  @author Godlewski Mateusz S19282
 *
 */

package zad2;


import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class Main
{
  public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
    Service s = new Service("Poland");
    String weatherJson = s.getWeather("Warsaw");
    System.out.println(weatherJson);
    Double rate1 = s.getRateFor("USD");
    System.out.println(rate1);
    Double rate2 = s.getNBPRate();
    //System.out.println(rate2);
    // część uruchamiająca GUI
  }
}
