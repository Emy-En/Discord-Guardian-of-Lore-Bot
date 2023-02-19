package events;

import calculators.PathfinderWikiScraping;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class PathfinderWikiEventListener extends ListenerAdapter {

    // Method which allows user to ask the bot to go and look for information on the French Pathfinder wiki
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("pfwiki")) {

            event.reply(PathfinderWikiScraping.queryFromCommand(event.getOption("query").getAsString())).queue();
        }
    }
}

