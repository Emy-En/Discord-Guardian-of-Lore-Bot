package events;

import calculators.FTWikiScraping;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class FTWikiEventListener extends ListenerAdapter {

    // Method which allows user to ask the bot to go and look for information on the Fandom wiki of our TTRPG server
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("ftwiki")) {
            
            event.reply(FTWikiScraping.queryFromCommand(event.getOption("query").getAsString())).queue();
        }
    }
}

