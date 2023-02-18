import calculators.PointCalculator;
import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

public class PointCalculatorTest {

    //This method tests the 'pointBuyFromDM' method from PointCalculator, but also some functionalities
    //of the PointBuyHandler 'onMessageReceived' command
    @Test
    public void pointBuyFromDMTest(){

        //Stat-rolling message from Dice Maiden
        String str3d6 = """
                Anonymous Request: [6 3d6] Rolls:
                `[6, 6, 2]` Result: `14`
                `[5, 3, 1]` Result: `9`
                `[6, 5, 2]` Result: `13`
                `[4, 2, 1]` Result: `7`
                `[3, 3, 1]` Result: `7`
                `[5, 4, 3]` Result: `12`;
                """;

        //Another stat-rolling message
        String str4d6k3 = """
                Anonymous Request: [6 4d6 k3] Rolls:
                `[5, 2, 1, 1]` Result: `8`
                `[6, 5, 3, 1]` Result: `14`
                `[6, 5, 3, 2]` Result: `14`
                `[4, 2, 2, 2]` Result: `8`
                `[6, 4, 2, 1]` Result: `12`
                `[4, 4, 3, 2]` Result: `11`
                """;

        //A Dice Maiden message not related to stat-rolling
        String strOther =
                """
                Anonymous Request: [3 5d6 kl4] Rolls:
                [6, 4, 3, 1, 1] Result: 9
                [4, 3, 2, 1, 1] Result: 7
                [6, 6, 6, 4, 2] Result: 18
                """;

        //A stat rolling message with out of bounds elements
        String strOutOfBounds = """
                Anonymous Request: [6 4d6 k3] Rolls:
                `[5, 2, 1, 1]` Result: `2`
                `[6, 5, 3, 1]` Result: `4`
                `[6, 5, 3, 2]` Result: `4`
                `[4, 2, 2, 2]` Result: `8`
                `[6, 4, 2, 1]` Result: `2`
                `[4, 4, 3, 2]` Result: `0`
                """;

        //The pattern that allows the stat rolling messages to be recognized (from PointBuyHandler)
        Pattern r = Pattern.compile("6 [34]d6 [k3]?");
        Matcher m1 = r.matcher(str3d6);
        Matcher m2 = r.matcher(str4d6k3);

        //The pattern that captures the values
        Pattern r1 = Pattern.compile(".*Result: `(\\d{1,2})`.*\n"+
                        ".*Result: `(\\d{1,2})`.*\n" +
                        ".*Result: `(\\d{1,2})`.*\n" +
                        ".*Result: `(\\d{1,2})`.*\n" +
                        ".*Result: `(\\d{1,2})`.*\n" +
                        ".*Result: `(\\d{1,2})`");

        //Matching pattern with messages
        Matcher m3 = r1.matcher(str3d6);
        assertTrue(m3.find());

        Matcher m4 = r1.matcher(str4d6k3);
        assertTrue(m4.find());

        Matcher m5 = r1.matcher(strOther);
        assertFalse(m5.find());

        //Test first actual roll
        assertEquals(PointCalculator.pointBuyFromDM(str3d6),"This is worth 1 points!");

        //Test second actual roll
        assertEquals(PointCalculator.pointBuyFromDM(str4d6k3),"This is worth 9 points!");

        //Test third 'not stat-rolling related' roll
        assertEquals(PointCalculator.pointBuyFromDM(strOther),"Error in computing the point buy value");

        //Test Fourth 'out of bounds' roll
        assertEquals(PointCalculator.pointBuyFromDM(strOutOfBounds),"Error in computing the point buy value");
    }

}
