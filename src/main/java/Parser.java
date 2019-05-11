/**
 * Показує погоду з сайту https://sinoptik.ua
 */

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    private static Document getPage() throws IOException {
        String city = "київ"; // бровари ніжин київ одеса
        String url = "https://ua.sinoptik.ua/" + toASCII("погода-" + city + "/") + toDay();
        Document page = Jsoup.parse(new URL(url), 2000);
        return page;
    }

    // Конвертація назви міста в ASCII-формат. Напр.: %D0%BA%D0%B8%D1%97%D0%B2 (київ)
    public static String toASCII(String textToConvert){
        URI uri = null;
        try {
            uri = new URI(textToConvert);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            System.out.println("Помилка в конвертації назви міста");
        }
        return uri.toASCIIString();
    }

    // Повертає сьогодняшню дату
    public static String toDay() {
        Calendar c = Calendar.getInstance();
        //String curentTime = String.format("%tT", c); //format hh:mm:ss
        //String currentDate = String.format("%ta %td.%tm.%ty", c, c, c, c);  //format Fr dd.MM.yyyy
        String currentDate = String.format("%tY-%tm-%td", c, c, c);  //format yyyy-MM-dd
        return currentDate;
    }

    // Пошук інформації про погодні явища
    public static String phenomenon(String s){

        Pattern pattern = Pattern.compile("class=\"weather.+title=\"(?<weather>.*?)\">");
        Matcher matcher = pattern.matcher(s);

        while (matcher.find()) {
            return matcher.group("weather");
        }

        return "data not found";

    }


    public static void main(String[] args) throws IOException {

        Document page = getPage();
        Element tableWth = page.select("div[class=tabs]").first();

        for(int i = 1; i<=7; i++){

            Elements firstDay = tableWth.select("div[id=bd"+i+"]");
            String hepen = firstDay.select("div").first().text();
            System.out.println(hepen + " " + phenomenon(String.valueOf(firstDay)));

        }
    }
}
