import javax.naming.*;
import java.sql.*;
import javax.sql.*;

public class DbAccess extends CommandImpl
{
    private DataSource dataSource;

    public void init()
    {
        try
        {
            Context init = new InitialContext();
            Context jndiCtx = (Context) init.lookup("java:comp/env");
            String dbName = (String) getParameter("dbName");
            dataSource = (DataSource) jndiCtx.lookup(dbName);
        }
        catch (NamingException exc)
        {
            setStatusCode(1);
        }
    }

    public void execute()
    {
        clearResult();
        setStatusCode(0);
        Connection con = null;
        try
        {
            synchronized(this)
            {
                con = dataSource.getConnection();
            }

            Statement stmt = con.createStatement();
            String cmd =  (String) getParameter("command");

            if (cmd.startsWith("select"))
            {
                ResultSet rs = stmt.executeQuery(cmd);

                ResultSetMetaData rsmd = rs.getMetaData();
                int cols = rsmd.getColumnCount();
                while (rs.next())
                {
                    StringBuilder wynik = new StringBuilder();
                    for (int i=1; i<=cols; i++)
                        wynik.append(rs.getObject(i)).append(" ");
                    addResult(wynik.toString());
                }
                rs.close();
            }
            else if (cmd.startsWith("insert"))
            {
                int upd = stmt.executeUpdate(cmd);
                addResult("Dopisano " + upd + " rekordów");
            }
            else
                setStatusCode(3);
        }
        catch (SQLException exc)
        {
            setStatusCode(2);
            throw new DbAccessException("Błąd w dostępie do bazy lub w SQL", exc);
        }
        finally
        {
            try
            {
                con.close();
            }
            catch (Exception ignored) {}
        }
    }
}