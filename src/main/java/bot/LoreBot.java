package bot;

import events.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.io.*;
import java.util.Objects;

public class LoreBot {

    //Making the jda an attribute allows to access it from other classes
    private static JDA jda;

    public static JDA getJDA() {
        return jda;
    }

    public static void main(String[] args) throws InterruptedException, IOException {

        //Gets token from token.txt file
        try{

            //The token file is a resource accessible in the jar file
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    Objects.requireNonNull(LoreBot.class.getResourceAsStream("/token.txt"))));

            final String TOKEN = br.readLine();
            JDABuilder jdaBuilder = JDABuilder.createDefault(TOKEN);

            //Creates connexion
            jda = jdaBuilder
                    .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES)
                    .addEventListeners(
                            new PointBuyHandler(),
                            new ReadyEventListener(),
                            new ShutDownEventListener(),
                            new PathfinderWikiEventListener(),
                            new GithubEventListener(),
                            new FTWikiEventListener())
                    .build()
                    .awaitReady();

            //Manages slash commands
            jda.upsertCommand("point", "Allows you to calculate eaaaaasily " +
                            "the Pathfinder point buy value of some rolls!")
                    .addOption(OptionType.STRING, "rolls", "Put here between 1 and 6 rolls please! Separate them with spaces!", true)
                    .setGuildOnly(false)
                    .queue();

            jda.upsertCommand("shutdown", "Allows you to shut down the bot, please do not use unless necessary!")
                    .addOption(OptionType.STRING, "code", "Code to shutdown bot", true)
                    .setGuildOnly(false)
                    .queue();

            jda.upsertCommand("pfwiki", "Allows you to quickly have a look at spells and everything!")
                    .addOption(OptionType.STRING, "query", "What are you looking for?", true)
                    .setGuildOnly(false)
                    .queue();

            jda.upsertCommand("git", "Here is the link to my Guthub project!")
                    .setGuildOnly(false)
                    .queue();

            jda.upsertCommand("ftwiki", "Allows you to quickly have a look at our wiki! Typing 'w' gives the link to the homepage!")
                    .addOption(OptionType.STRING, "query", "What are you looking for?", true)
                    .setGuildOnly(false)
                    .queue();

            //This part allows to send a message in a specific channel to know when the bot is turned on
            TextChannel textChannel = jda.getTextChannelById("1076598009762156705");
            if(textChannel.canTalk()) {
                textChannel.sendMessage("The bot has been turned on!").queue();
            }

        }catch(IOException | NullPointerException f){
            System.out.println("Error : Token file not found");
        }
    }

}