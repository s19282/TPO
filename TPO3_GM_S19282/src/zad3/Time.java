/**
 *
 *  @author Godlewski Mateusz S19282
 *
 */

package zad3;

import java.time.*;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Time
{
    public static String passed(String from, String to)
    {
        Pattern withTime = Pattern.compile(".*T.*");
        Matcher mFrom = withTime.matcher(from);
        Matcher mTo = withTime.matcher(to);
        StringBuilder output = new StringBuilder();
        if(mFrom.matches()&&mTo.matches())
        {
            try
            {
                ZonedDateTime dtFrom = ZonedDateTime.of(LocalDateTime.parse(from),ZoneId.of("Europe/Warsaw"));
                ZonedDateTime dtTo = ZonedDateTime.of(LocalDateTime.parse(to),ZoneId.of("Europe/Warsaw"));

                Duration duration = Duration.between(dtFrom,dtTo);
                Period period = Period.between(dtFrom.toLocalDate(),dtTo.toLocalDate());
                output.append("Od ").append(dtFrom.getDayOfMonth()).append(" ").append(dtFrom.getMonth().name()).append(" (")
                        .append(dtFrom.getDayOfWeek().name()).append(") godz. ").append(dtFrom.getHour()==0?"00":dtFrom.getHour())
                        .append(":").append(dtFrom.getMinute()==0?"00":dtFrom.getMinute()).append(" do ").append(dtTo.getDayOfMonth())
                        .append(" ").append(dtTo.getMonth().name()).append(" (").append(dtTo.getDayOfWeek()).append(") godz. ")
                        .append(dtTo.getHour()==0?"00":dtTo.getHour()) .append(":").append(dtTo.getMinute()==0?"00":dtTo.getMinute())
                        .append("\n- mija: ").append(duration.toDays()).append(" dni, tygodni ")
                        .append(duration.toDays()%7==0?duration.toDays()/7:String.format("%.2f",duration.toDays()/7D))
                        .append("\n- godzin: ").append(duration.toHours()).append(", minut " ).append(duration.toMinutes());
                if(ChronoUnit.DAYS.between(dtFrom, dtTo)>=1)
                {
                    output.append("\n- kalendarzowo: ");
                    if(period.getYears()>=1)
                    {
                        output.append(period.getYears());
                        if(period.getYears()>=5)
                            output.append(" lat ");
                        else if(period.getYears()>=2)
                            output.append(" lata ");
                        else
                            output.append(" rok ");
                    }
                    if(period.getMonths()>=1)
                    {
                        output.append(period.getMonths());
                        if(period.getMonths()>=5)
                            output.append(" miesięcy ");
                        else if(period.getMonths()>=2)
                            output.append(" miesiące ");
                        else
                            output.append(" miesiąc ");
                    }
                    if(period.getDays()>=1)
                    {
                        output.append(period.getDays());
                        if(period.getDays()>=2)
                            output.append(" dni");
                        else
                            output.append(" dzień");
                    }
                }
            }
            catch (DateTimeParseException e)
            {
                output.append("*** ").append(e);
            }
        }
        else
        {
            try
            {
                LocalDate dtFrom = LocalDate.parse(from);
                LocalDate dtTo = LocalDate.parse(to);

                long days = ChronoUnit.DAYS.between(dtFrom, dtTo);
                Period period = Period.between(dtFrom,dtTo);
                output.append("Od ").append(dtFrom.getDayOfMonth()).append(" ").append(dtFrom.getMonth().name()).append(" (")
                        .append(dtFrom.getDayOfWeek().name()).append(" do ").append(dtTo.getDayOfMonth())
                        .append(" ").append(dtTo.getMonth().name()).append(" (").append(dtTo.getDayOfWeek())
                        .append(")\n- mija: ").append(days).append(" dni, tygodni ")
                        .append(days%7==0?days/7: String.format("%.2f",days/7D));
                if(ChronoUnit.DAYS.between(dtFrom, dtTo)>=1)
                {
                    output.append("\n- kalendarzowo: ");
                    if(period.getYears()>=1)
                    {
                        output.append(period.getYears());
                        if(period.getYears()>=5)
                            output.append(" lat ");
                        else if(period.getYears()>=2)
                            output.append(" lata ");
                        else
                            output.append(" rok ");
                    }
                    if(period.getMonths()>=1)
                    {
                        output.append(period.getMonths());
                        if(period.getMonths()>=5)
                            output.append(" miesięcy ");
                        else if(period.getMonths()>=2)
                            output.append(" miesiące ");
                        else
                            output.append(" miesiąc ");
                    }
                    if(period.getDays()>=1)
                    {
                        output.append(period.getDays());
                        if(period.getDays()>=2)
                            output.append(" dni");
                        else
                            output.append(" dzień");
                    }
                }
            }
            catch (DateTimeParseException e)
            {
                output.append("*** ").append(e);
            }
        }
        return output.toString();
    }
}
