package events;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import bot.LoreBot;


public class ShutDownEventListener extends ListenerAdapter{

    //This method allows a user who is given the password to shut down the bot
    //This will allow the inexperienced users I will share the .jar executable of the project
    //To be able to host the bot on their computers without using a terminal
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        try {
            if (event.getName().equals("shutdown")) {

                BufferedReader brpw = new BufferedReader(new FileReader("password.txt"));
                String password = brpw.readLine();

                if (event.getOption("code").getAsString().equals(password)) {

                    //This part allows to send a message in a specific channel to know when the bot is turned off
                    TextChannel textChannel = LoreBot.getJDA().getTextChannelById("1076598009762156705");
                    if(textChannel.canTalk()) {
                        textChannel.sendMessage("The bot is shutting down!").queue();
                    }

                    event.reply("Good bye!").queue();

                    //The sending of the message takes some time
                    try{
                        Thread.sleep(500);
                    }catch(InterruptedException ignored){}

                    System.exit(1);
                } else {
                    event.reply("Lmao, you thought you could shut me down!").queue();
                }

            }
        } catch (IOException e) {
            //This part allows to send a message in a specific channel to know when the bot is turned off
            TextChannel textChannel = LoreBot.getJDA().getTextChannelById("1076598009762156705");
            if(textChannel.canTalk()) {
                textChannel.sendMessage("The bot is shutting down!").queue();
            }

            event.reply("I have forgotten the password anyway, so bye bye!").queue();

            //The sending of the message takes some time
            try{
                Thread.sleep(500);
            }catch(InterruptedException ignored){}

            System.exit(1);
        }
    }
}
