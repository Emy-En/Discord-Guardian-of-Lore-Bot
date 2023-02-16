import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.requests.GatewayIntent;
import Events.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class LoreBot {


    public static void main(String[] args) throws InterruptedException, IOException {

        //Gets token from token.txt file
        BufferedReader br = new BufferedReader(new FileReader("token.txt"));
        final String TOKEN = br.readLine();
        JDABuilder jdaBuilder = JDABuilder.createDefault(TOKEN);

        //Creates connexion
        JDA jda = jdaBuilder
                .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES)
                .addEventListeners(new PointBuyHandler(), new ReadyEventListener())
                .build()
                .awaitReady();

        //Manages slash commands
        jda.upsertCommand("point", "Allows you to calculate eaaaaasily " +
                        "the Pathfinder point buy value of some rolls!")
                .addOption(OptionType.STRING, "rolls", "Put here between 1 and 6 rolls please! Separate them with spaces!", true)
                .setGuildOnly(false)
                .queue();

    }

}