/**
 *
 *  @author Godlewski Mateusz S19282
 *
 */

package zad2;


import org.json.JSONException;
import java.io.IOException;

public class Main
{
  public static void main(String[] args) throws IOException, JSONException {
    Service s = new Service("United States");
    String weatherJson = s.getWeather("Warsaw");
    System.out.println(weatherJson);
    Double rate1 = s.getRateFor("JPY");
    System.out.println(rate1);
    Double rate2 = s.getNBPRate();
    System.out.println(rate2);
    // część uruchamiająca GUI
  }
}
