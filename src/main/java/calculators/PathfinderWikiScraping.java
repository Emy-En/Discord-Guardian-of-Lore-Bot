package calculators;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PathfinderWikiScraping {

    //Returns one link that could match an existing page correctly encoded
    public static String pfWikiUrlGenerator(String query) {
        try {
            BufferedReader br = new BufferedReader(new FileReader("pathfinderWikiUrl.txt"));
            final String pfWiki = br.readLine();

            //The French PF wiki URLs don't change much from the queries but we need to encode it correctly
            return pfWiki + URLEncoder.encode(query, StandardCharsets.UTF_8).replaceAll("\\+","%20") + ".ashx";

        } catch (IOException e) {
            return ("Error when generating URL");
        }
    }


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
        String queryUrl = pfWikiUrlGenerator(query);

        //The link works without this, but a space in the link makes discord not recognizing it

        boolean pageExists = doesPageExist(queryUrl);

        if(pageExists){
            //if the query worked, then return the query for it to be sent

            //Replacement of spaces for it to be recognized as link by discord
            return "Here is a link that matches your request!\n" + queryUrl.replace(" ", "%20");

        }else{
            //Else, sends a link to Google with the query

            //This allows to get the part from the URL that is used for site: Google searches
            Pattern r = Pattern.compile("www\\.(.*?)/.*/");
            Matcher m = r.matcher(queryUrl);

            if(m.find()){
                return "I had trouble looking on the wiki so here is a Google search link:\nhttps://www.google.com/search?q=site+"
                        +m.group(1)+"+"+query.replaceAll(" ","+");
            }else{
                return "Error when handling the request";
            }
        }
    }

}
