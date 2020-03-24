/**
 *
 *  @author Godlewski Mateusz S19282
 *
 */

package zad2;

import jdk.internal.org.xml.sax.XMLReader;
import org.json.JSONObject;
import org.json.JSONArray;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLReaderFactory;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.beans.XMLDecoder;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;

public class Service implements Serializable
{
    private static final String apiKey = "59045bb3edb0ac8183aee91bb2c18ee9";
    Locale locale;

    HashMap<String,String> countryCurrency = new HashMap<>();

    public Service(String country)
    {
        locale = new Locale("",country);
        for(String loc: Locale.getISOCountries())
        {
            try
            {
                countryCurrency.put(new Locale("",loc).getDisplayCountry().toUpperCase(),Currency.getInstance(new Locale("",loc)).getCurrencyCode());
            }
            catch (NullPointerException ignored)
            {}
        }
    }

    public String getURLConnection(String link) throws IOException
    {
        //int responseCode;
        StringBuilder response = null;
        String lineToRead;
        URL url = new URL(link);
        HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
        //responseCode = httpConnection.getResponseCode();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
        response = new StringBuilder();
        while ((lineToRead = bufferedReader.readLine()) != null)
            response.append(lineToRead).append("\n");
        bufferedReader.close();
        return response.toString();
    }

    public String getWeather(String city) throws IOException
    {
        JSONObject object = new JSONObject(getURLConnection("http://api.openweathermap.org/data/2.5/weather?q="+city+"&appid="+apiKey));
        return object.toString();
    }

    public double getRateFor(String currency) throws IOException
    {
        if(countryCurrency.get(locale.getDisplayCountry()).equals(currency))
            return 1D;
        else
        {
            JSONObject object = new JSONObject(getURLConnection("https://api.exchangeratesapi.io/latest?symbols=PLN,"+currency));
            JSONObject rates = new JSONObject(object.get("rates").toString());

            return Double.parseDouble(rates.get(countryCurrency.get(locale.getDisplayCountry())).toString());
        }
    }

    public double getNBPRate() throws IOException {
       XMLDecoder test = new XMLDecoder(getURLConnection("http://www.nbp.pl/kursy/xml/a058z200324.xml").);
       Tabela_kursow tk = (Tabela_kursow)test.readObject();
        System.out.println();
        return 1d;
    }


}  
