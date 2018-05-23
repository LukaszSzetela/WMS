package pl.szetela.lukasz.WMS.utils;

import java.time.LocalDate;
import java.util.*;

public class StringUtils {

    public static String[] getDate(LocalDate localDate) {
        String[] dates = new String[2];
        int numberOfDay = localDate.getDayOfWeek().getValue();
        switch (numberOfDay) {
            case 1:
                dates[0] = localDate.toString();
                dates[1] = localDate.plusDays(6).toString();
                break;
            case 2:
                dates[0] = localDate.minusDays(1).toString();
                dates[1] = localDate.plusDays(6).toString();
                break;
            case 3:
                dates[0] = localDate.minusDays(2).toString();
                dates[1] = localDate.plusDays(4).toString();
                break;
            case 4:
                dates[0] = localDate.minusDays(3).toString();
                dates[1] = localDate.plusDays(3).toString();
                break;
            case 5:
                dates[0] = localDate.minusDays(4).toString();
                dates[1] = localDate.plusDays(2).toString();
                break;
            case 6:
                dates[0] = localDate.minusDays(5).toString();
                dates[1] = localDate.plusDays(1).toString();
                break;
            case 7:
                dates[0] = localDate.minusDays(6).toString();
                dates[1] = localDate.toString();
                break;
            default:
                break;
        }
        return dates;
    }

    public static List<String> prepareStringArrayToQuery(String value) {
        return new ArrayList<>(Arrays.asList(value.split(",")));
    }

    public static String prepareProperNumberQuestionTagsInBrackets(int number) {
        List<String> questionTags = Collections.nCopies(number, "?");
        StringJoiner stringJoiner = new StringJoiner(", ", "(", ")");
        questionTags.forEach(stringJoiner::add);
        return stringJoiner.toString();
    }

    private StringUtils() {
    }
}
