/**
 *
 *  @author Godlewski Mateusz S19282
 *
 */

package zad2;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;

public class Service implements Serializable
{
    private static final String apiKey = "59045bb3edb0ac8183aee91bb2c18ee9";
    Locale locale;
    public static HashMap<String,String> countryCode = new HashMap<>();
    public static HashMap<String,String> codeCurrency = new HashMap<>();

    public Service(String country)
    {
        locale = new Locale("",country);
        for(String loc: Locale.getISOCountries())
        {
            Locale tmp = new Locale("",loc);
            try { codeCurrency.put(loc,Currency.getInstance(tmp).getCurrencyCode());}
            catch (NullPointerException ignored){}
            countryCode.put(tmp.getDisplayCountry().toUpperCase(),loc);
        }
    }


    public static String getCurrency(Locale country)
    {
        return codeCurrency.get(countryCode.get(country.getDisplayCountry()));
    }


    public String getURLConnection(String link) throws IOException
    {
        StringBuilder response;
        String lineToRead;
        URL url = new URL(link);
        HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
        response = new StringBuilder();
        while ((lineToRead = bufferedReader.readLine()) != null)
            response.append(lineToRead).append("\n");
        bufferedReader.close();
        return response.toString();
    }


    public String getWeather(String city) {
        city=city.toUpperCase();
        try
        {
            JSONObject object = new JSONObject(getURLConnection("http://api.openweathermap.org/data/2.5/weather?q="+city+","+countryCode.get(locale.getDisplayCountry())+"&appid="+apiKey));
            return object.toString();
        }
        catch (IOException | JSONException e)
        {
            e.printStackTrace();
        }
        return "---";
    }


    public double getRateFor(String currency)
    {
        currency=currency.toUpperCase();
        try
        {
            if(currency.equalsIgnoreCase(getCurrency(locale)))
                return 1D;
            else
            {
                JSONObject object = new JSONObject(getURLConnection("https://api.exchangeratesapi.io/latest?base="+getCurrency(locale)+"&symbols="+currency));
                JSONObject rates = new JSONObject(object.get("rates").toString());
                return rates.getDouble(currency);
            }
        }
        catch (IOException | JSONException e)
        {
            e.printStackTrace();
        }
        return -1D;
    }


    public double getNBPRate()
    {
        try
        {
            if(locale.getDisplayCountry().equalsIgnoreCase("POLAND"))
            {
                return 1D;
            }
            else
            {
                JSONObject xml;
                try
                {
                    xml = new JSONObject(getURLConnection("http://api.nbp.pl/api/exchangerates/rates/a/"+getCurrency(locale)+"/"));
                }
                catch (FileNotFoundException e)
                {
                    try
                    {
                        xml = new JSONObject(getURLConnection("http://api.nbp.pl/api/exchangerates/rates/b/"+getCurrency(locale)+"/"));
                    }
                    catch (FileNotFoundException e1)
                    {
                        System.out.println("Nie znaleziono waluty");
                        return -1D;
                    }
                }
                JSONArray value = xml.getJSONArray("rates");
                return value.getJSONObject(0).getDouble("mid");
            }
        }
        catch (IOException | JSONException e)
        {
            e.printStackTrace();
        }
        return -1D;
    }
}  
