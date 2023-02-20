package calculators;

import bot.LoreBot;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PathfinderWikiScraping {

    //Returns one link that could match an existing page correctly encoded
    public static String pfWikiUrlGenerator(String query) {

        //The French PF wiki URLs don't change much from the queries but we need to encode it correctly
        String queryEncoded = URLEncoder.encode(
                query.replaceAll("'", ""),
                StandardCharsets.UTF_8).replaceAll("\\+", "%20");

        try {
            //This file contains multiple URLs that could build working links, it is in the jar file
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    Objects.requireNonNull(LoreBot.class.getResourceAsStream("/pathfinderWikiUrl.txt"))));

            String pfWiki = br.readLine();

            //These will help knowing if the links built are good
            boolean pageExists = false;
            String answer = "";

            //Looks for possible links that could work
            while (pfWiki != null && !pageExists) {
                answer = pfWiki.replace("query", queryEncoded);
                pageExists = doesPageExist(answer);
                pfWiki = br.readLine();
            }

            if (pageExists) {
                return answer;
            } else {
                //Pattern that captures the site
                Pattern r = Pattern.compile("www\\.(.*?)/");
                Matcher m = r.matcher(answer);

                if (m.find()) {
                    //Returns link that searches on Google on the pathfinder wiki site
                    return "https://www.google.com/search?q=site%3A" + m.group(1) + "+" + queryEncoded;
                } else {
                    //Error when generating link, returns search with query + keyword "Pathfinder"
                    return "https://www.google.fr/search?q=pathfinder%20" + queryEncoded;
                }
            }
        }catch(IOException e){
            //Error when generating link, returns search with query + keyword "Pathfinder"
            return "https://www.google.fr/search?q=pathfinder%20" + queryEncoded;
        }
    }

    //Returns true if the page exists and is not a "Page not found" page
    public static boolean doesPageExist(String url) {
        try{
            //If the page doesn't exist, there is an error, and we return false
            Document doc = Jsoup.parse(new URL(url).openStream(),null,url);


            //If the page exists, either it does exist or it is a "We didn't find this page" page
            //The title of these pages is "Page Introuvable"
            boolean answer = doc.select("h1.pagetitlesystem").text().equals("Page Introuvable");

            //If the title is not "Page Introuvable", then true
            //Else, false
            return !answer;

        }catch(IOException e){
            return false;
        }
    }


    //Tests if generated link is ok and sends a link depending on the answer
    public static String queryFromCommand(String query) {

        //Generates a good link to the query, whether on the site or on google (if not found on site)
        String queryUrl = pfWikiUrlGenerator(query);

        //The page is considered to exist if Google is not in its name
        if (!queryUrl.contains("google.com")) {
            //if the query worked correctly, then return the query for it to be sent

            //Replacement of spaces for it to be recognized as link by discord
            return "Here is a link that matches your request!\n" + queryUrl;

        } else {
            //Else, sends a link to Google with the query

            //This allows to get the part from the URL that is used for 'site:' Google searches
            return "I had trouble looking on the wiki so here is a Google search link:\n" + queryUrl;
        }
    }
}


