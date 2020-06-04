import java.util.ListResourceBundle;

public class RegexParamsDef_pl extends ListResourceBundle {
    public Object[][] getContents() {
        return contents;
    }

    static final Object[][] contents = {
            { "charset", "ISO-8859-2" },
            { "header", new String[] { "Testowanie wyrażeń regularnych" } },
            { "param_regex", "Wzorzec:" },
            { "param_input", "Tekst:" },
            { "submit", "Pokaż wyniki wyszukiwania" },
            { "footer", new String[] { } },
            { "resCode", new String[]
                    { "Dopasowano", "Brak danych",
                            "Wadliwy wzorzec", "Nie znaleziono dopasowania" }
            },
            { "resDescr",
                    new String[] { "podłańcuch", "od poz.", "do poz.", "" } },
    };
}