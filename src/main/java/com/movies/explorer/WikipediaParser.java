package com.movies.explorer;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WikipediaParser {

    public static void main(String[] args) throws IOException, InterruptedException {

        Set<MovieData> movies = getMovies(2018);
        movies.forEach(movie -> {
            System.out.println(movie.title());
            System.out.println(movie.companies());
            System.out.println(movie.peopleRoles().actors());
            System.out.println(movie.peopleRoles().directors());
            System.out.println(movie.peopleRoles().screenwriters());
            System.out.println(movie.countries());
            System.out.println(movie.genres());
        });
        System.out.println(movies.size());
    }

    public static Set<MovieData> getMovies(int year) throws IOException, InterruptedException {
        Set<MovieData> movies = new HashSet<>();
        Document doc = Jsoup.connect("https://en.wikipedia.org/wiki/" + year + "_in_film").get();
        Elements wikiTables = doc.select("table.wikitable");
        int numTables = wikiTables.size();
        int movieCount = 0;
        for (int tableIndex = numTables - 1; tableIndex >= numTables - 4; tableIndex--) {
            Element wikiTable = wikiTables.get(tableIndex);
            Elements tableRows = wikiTable.children().select("tr");
            tableRows.remove(0);
            for (Element tableRow : tableRows) {
                Elements cells = tableRow.children().select("td");
                if ((cells.first().select("[rowspan]").size() > 0) || (cells.first().select("[style]").size() > 0)) {
                    cells.remove(cells.first());
                }
                String endpoint = cells.get(0).select("a").attr("href");
                if (endpoint == null || endpoint.isEmpty()) {
                    continue;
                }
                String title = cells.get(0).text().replaceAll("\\p{P}", "");
                System.out.println(title);
                String movieUrl = "https://en.wikipedia.org" + endpoint;
                MovieData movieData = WikipediaMovieParser.parseMovieData(movieUrl, title, year);
                movies.add(movieData);
                movieCount++;
                if (movieCount % 10 == 0) {
                    Thread.sleep(1000);
                }
            }
        }
        return movies;
    }
}
