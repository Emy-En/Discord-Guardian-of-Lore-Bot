package Events;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Calculators.PointCalculator;

public class PointBuyHandler extends ListenerAdapter {

    // Method which allows user to put 6 stats between 3 and 18 to calculate point buy
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("point")) {

            if (event.getOption("rolls") != null) {
                event.reply(PointCalculator.pointBuyFromCommand(event.getOption("rolls").getAsString())).queue();
            }
        }
    }


    // Method which looks for messages related to rolling stats to give the corresponding point buy amount
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        //Identifies Dice Maiden answers
        if (event.getAuthor().getId().equals("572301609305112596")) {

            //Regex to find Dice Maiden messages who are related to stat rolling
            Pattern r = Pattern.compile("6 \\dd6 [k3]{01}");
            Matcher m = r.matcher(event.getMessage().getContentRaw());

            // If the message is stat-rolling-related, then send a message
            if (m.find()) {

                event.getChannel()
                        .sendMessage(PointCalculator.pointBuyFromDM(event.getMessage().getContentRaw()))
                        .setMessageReference(event.getMessageId())
                        .queue();
            }
        }
    }

}

