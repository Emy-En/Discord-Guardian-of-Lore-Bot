package Calculators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PointCalculator {

    //Not exactly equal to actual point buy table because it only goes down to 7 and a 3d6 roll can go as low as 3
    static final int[] CONVERSION = {-16, -12, -9, -6, -4, -2, -1, 0, 1, 2, 3, 5, 7, 10, 13, 17};

    //Method which calculates a point buy amount from a Dice Maiden message
    public static String pointBuyFromDM(String message) {

        //Pattern that captures the dices' results
        Pattern r = Pattern.compile(
                "Result: `(\\d{1,2})`.*\n" +
                        ".*Result: `(\\d{1,2})`.*\n" +
                        ".*Result: `(\\d{1,2})`.*\n" +
                        ".*Result: `(\\d{1,2})`.*\n" +
                        ".*Result: `(\\d{1,2})`.*\n" +
                        ".*Result: `(\\d{1,2})`");

        //Matching pattern with message
        Matcher m = r.matcher(message);

        if (m.find()) {
            //Computing answer
            int answer = 0;

            /*
            Adding to the answer variable the individual point buy values found thanks to the regular expression
            2 errors can happen here : NumberFormatException when parsing and IndexOutOfBoundsException when converting
             */
            try{
                for (int i = 1; i <= 6; i++) {
                    answer += CONVERSION[Integer.parseInt(m.group(i)) - 3];
                }
            }catch(Exception e){
                return ("Error in computing the point buy value");
            }

            return "This is worth " + answer + " points!";

        } else {
            return ("Error in computing the point buy value");
        }
    }

    //Method which calculates a point buy amount from the /point slash command
    public static String pointBuyFromCommand(String message) {

        String[] tableStr = message.split(" ");

        if (tableStr.length > 6){
            return("`"+message+"`  :Too much numbers in there, please keep it between 1 and 6!");
        }
        else {
            int[] tableInt = new int[tableStr.length];
            boolean outofbounds = false;

            for (int i = 0; i < tableStr.length && !outofbounds; i++) {
                tableInt[i] = Integer.parseInt(tableStr[i]);
                outofbounds = (tableInt[i] < 3 || tableInt[i] > 18);
            }

            if (!outofbounds){
                int answer = 0;

                for (int i = 0; i < tableInt.length; i++) {
                    answer += CONVERSION[tableInt[i] - 3];
                }

                return "`"+message+"` is worth " + answer + " points!";
            }
            else{
                return("`"+message+"` : Please keep your numbers between 3 and 18 included");
            }
        }

    }

}





/*
"Definitely Not Umbra Request: `[6 4d6 k3]` Rolls:\n`[6, 3, 2, 2]` Result: `11`\n`[6, 4, 3, 3]` Result: `13`\n`[5, 3, 2, 2]` Result: `10`\n`[6, 5, 1, 1]` Result: `12`\n`[6, 5, 4, 2]` Result: `15`\n`[3, 3, 1, 1]` Result: `7`"

 */