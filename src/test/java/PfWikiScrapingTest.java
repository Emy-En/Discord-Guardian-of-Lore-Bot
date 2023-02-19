import org.junit.jupiter.api.Test;
import calculators.PathfinderWikiScraping;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

public class PfWikiScrapingTest {

    @Test
    public void doesPageExistTest() throws IOException {

        String url = PathfinderWikiScraping.pfWikiUrlGenerator("baguettarme");
        String url2 = PathfinderWikiScraping.pfWikiUrlGenerator("mains brûlantes");
        String url3 = PathfinderWikiScraping.pfWikiUrlGenerator("iomédae");
        String url4 = PathfinderWikiScraping.pfWikiUrlGenerator("maaaaaaaains bruuuulantes777");

        //Test 1 : Everything works fine
        assertEquals("https://www.pathfinder-fr.org/Wiki/Pathfinder-RPG.baguettarme.ashx",url);
        assertTrue(PathfinderWikiScraping.doesPageExist(url));

        //Test 2 : Everything works fine but encoding necessary
        assertEquals("https://www.pathfinder-fr.org/Wiki/Pathfinder-RPG.mains%20br%C3%BBlantes.ashx",url2);
        assertTrue(PathfinderWikiScraping.doesPageExist(url2));

        //Test 3 : There is an error but we still find a "Page Introuvable wiki page
        assertEquals("https://www.pathfinder-fr.org/Wiki/Pathfinder-RPG.iom%C3%A9dae.ashx",url3);
        assertFalse(PathfinderWikiScraping.doesPageExist(url3));

        //Test 4 : Error
        assertEquals("https://www.pathfinder-fr.org/Wiki/Pathfinder-RPG.maaaaaaaains%20bruuuulantes777.ashx",url4);
        assertFalse(PathfinderWikiScraping.doesPageExist(url4));
    }

    @Test
    public void queryFromCommandTest(){
        String url = "https://www.pathfinder-fr.org/Wiki/Pathfinder-RPG.iom%C3%A9dae.ashx";

        Pattern r = Pattern.compile("www\\.(.*?)/");
        Matcher m = r.matcher(url);

        assertTrue(m.find());
        assertEquals("pathfinder-fr.org",m.group(1));

        PathfinderWikiScraping.queryFromCommand("iomédae");
    }


}
