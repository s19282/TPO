package zad2;

public class Pozycja
{
    private String kurs_sredni;

    private String kod_waluty;

    private String nazwa_waluty;

    private String przelicznik;

    public String getKurs_sredni ()
    {
        return kurs_sredni;
    }

    public void setKurs_sredni (String kurs_sredni)
    {
        this.kurs_sredni = kurs_sredni;
    }

    public String getKod_waluty ()
    {
        return kod_waluty;
    }

    public void setKod_waluty (String kod_waluty)
    {
        this.kod_waluty = kod_waluty;
    }

    public String getNazwa_waluty ()
    {
        return nazwa_waluty;
    }

    public void setNazwa_waluty (String nazwa_waluty)
    {
        this.nazwa_waluty = nazwa_waluty;
    }

    public String getPrzelicznik ()
    {
        return przelicznik;
    }

    public void setPrzelicznik (String przelicznik)
    {
        this.przelicznik = przelicznik;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [kurs_sredni = "+kurs_sredni+", kod_waluty = "+kod_waluty+", nazwa_waluty = "+nazwa_waluty+", przelicznik = "+przelicznik+"]";
    }
}
