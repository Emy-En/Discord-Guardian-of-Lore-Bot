package calculators;

import bot.LoreBot;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FTWikiScraping {

    //This class is mostly used to look for characters
    //Returns one link that could match an existing page correctly encoded
    public static String ftWikiUrlGenerator(String query) {

        //The French PF wiki URLs don't change much from the queries but we need to encode it correctly
        String queryEncoded = URLEncoder.encode(query, StandardCharsets.UTF_8)
                .replaceAll("\\+", "_");

        try {
            //This file contains a URL to the wiki, it is accessible in the jar file
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    Objects.requireNonNull(LoreBot.class.getResourceAsStream("/ftWikiUrl.txt"))));

            String ftWiki = br.readLine();

            //These will help knowing if the links built are good
            boolean pageFound = false;
            String answer = "";

            //Tests the link with the query
            answer = ftWiki.replace("query", queryEncoded);
            pageFound = doesPageExist(answer);

            //If it does not work, we have to test if a similar character exists in the list of all characters
            if (pageFound) {
                return answer;
            } else {
                //Here, we will look for a link that could match on pages that are a list of characters

                //We'll look on a few Category pages till either we find a match or we get to the last link to the main page

                String listPage = ftWiki.replace("query", br.readLine());

                while(!pageFound && listPage.contains("Category")){
                    //Opens a page with one of the lists
                    Document doc = Jsoup.parse(new URL(listPage).openStream(),null,listPage);

                    //Select all characters on page
                    Elements characters = doc.select("a.category-page__member-link");

                    //If the character is similar (contains) the query, then the page is considered found
                    for(Element e : characters){
                        if(e.text().toLowerCase().contains(query.toLowerCase())){
                            pageFound = true;
                            answer = ftWiki.replace("query",
                                    URLEncoder.encode(e.text(), StandardCharsets.UTF_8).replaceAll("\\+", "_"));
                            break;
                        }
                    }

                    if(pageFound){
                        return answer;
                    }else{
                        //Start the loop with another category
                        listPage = ftWiki.replace("query", br.readLine());
                    }

                }

                //The last listpage is a link to the home page
                //Pattern that captures the site
                Pattern r = Pattern.compile("//(.*?)/");
                Matcher m = r.matcher(answer);

                if (m.find()) {
                    //Returns link that searches on Google on the pathfinder wiki site
                    return "https://www.google.com/search?q=site%3A" + m.group(1) + "+" + queryEncoded;
                } else {
                    //Error when generating link, returns search with query + keyword wiki
                    return "https://www.google.fr/search?q=%77%69%6B%69%20%66%61%72%20%74%6F%77%6E%20" + queryEncoded;
                }

            }
        }catch(IOException e){
            //Error when generating link, returns search with query + keyword wiki
            return "https://www.google.fr/search?q=%77%69%6B%69%20%66%61%72%20%74%6F%77%6E%20" + queryEncoded;
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
        String queryUrl = ftWikiUrlGenerator(query);

        //The page is considered to exist if Google/Wiki is not in its name (Wiki means home page)
        if (!queryUrl.contains("google.com") && !queryUrl.contains("Wiki")) {
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


