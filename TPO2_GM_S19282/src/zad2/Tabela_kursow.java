package zad2;

public class Tabela_kursow
{
    private String uid;

    private String data_publikacji;

    private String numer_tabeli;

    private String typ;

    private Pozycja[] pozycja;

    public String getUid ()
    {
        return uid;
    }

    public void setUid (String uid)
    {
        this.uid = uid;
    }

    public String getData_publikacji ()
    {
        return data_publikacji;
    }

    public void setData_publikacji (String data_publikacji)
    {
        this.data_publikacji = data_publikacji;
    }

    public String getNumer_tabeli ()
    {
        return numer_tabeli;
    }

    public void setNumer_tabeli (String numer_tabeli)
    {
        this.numer_tabeli = numer_tabeli;
    }

    public String getTyp ()
    {
        return typ;
    }

    public void setTyp (String typ)
    {
        this.typ = typ;
    }

    public Pozycja[] getPozycja ()
    {
        return pozycja;
    }

    public void setPozycja (Pozycja[] pozycja)
    {
        this.pozycja = pozycja;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [uid = "+uid+", data_publikacji = "+data_publikacji+", numer_tabeli = "+numer_tabeli+", typ = "+typ+", pozycja = "+pozycja+"]";
    }
}
