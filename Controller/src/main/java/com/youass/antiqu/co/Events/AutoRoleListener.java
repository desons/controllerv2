package com.youass.antiqu.co.Events;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.youass.antiqu.co.Objects.AutoRole;
import com.youass.antiqu.co.Objects.AutoRoleManager;
import com.youass.antiqu.co.Objects.SelfRole;
import com.youass.antiqu.co.Util.REF;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class AutoRoleListener extends Command {

    private final EventWaiter waiter;

    private static Message mostrecentdelete;
    private static String mostrecentname;


    public AutoRoleListener(EventWaiter waiter) {
        this.waiter = waiter;
        super.name = "autorole";
        super.help = "Create an auto role.";
        super.aliases = new String[]{"autorole-create"};
        super.arguments = "create";
    }

    public static String getMostrecentname() {
        return mostrecentname;
    }

    public static Message getMostrecentdelete() {
        return mostrecentdelete;
    }

    @Override
    protected void execute(CommandEvent evt) {

        String[] args = evt.getMessage().getContentRaw().split(" ");

        Role star = evt.getGuild().getRolesByName("*", true).get(0);

        if(args[1].contains("delete") && evt.getMember().getRoles().contains(star)) {
            Member member = evt.getMember();
            User user = evt.getAuthor();
            Guild guild = evt.getGuild();
            MessageChannel channel = evt.getChannel();
            Message message = evt.getMessage();

            if(args[0].equalsIgnoreCase(REF.prefix+"autorole") && evt.getMember().getRoles().contains(star)) {

                if(args[1].equalsIgnoreCase("delete")) {
                    String name = message.getContentRaw().substring(16);
                    if (AutoRoleManager.isInMap(name))   {

                        channel.sendMessage(createEmbed("", "Are you sure you want to delete the "+name+" autorole?").build()).queue((msg) -> {
                            msg.addReaction("✅").queue();
                            msg.addReaction("❌").queue();
                            mostrecentdelete = msg;
                            mostrecentname = name;
                        });
                  } else {
                        channel.sendMessage(createEmbed("", "That name is not in the map").build()).queue();
                    }
                }



            }

        }


        if(args[1].contains("list") && evt.getMember().getRoles().contains(star) ) {
            Member member = evt.getMember();
            User user = evt.getAuthor();
            Guild guild = evt.getGuild();
            MessageChannel channel = evt.getChannel();
            Message message = evt.getMessage();


            if(AutoRoleManager.getMap().isEmpty()) {
                channel.sendMessage(createEmbed("", "The map is currently empty.").build()).queue();
                return;
            }

            for(AutoRole n : AutoRoleManager.getMap().values()) {

                EmbedBuilder eb = new EmbedBuilder();

                String list = "Name: **";


                list+= n.getName()+"**\n";

                list+= "Detectable(s): ";
                for(Role nn : n.getDectectables()) {
                    list += nn.getAsMention()+" ";
                }

                list+= "Adding Role(s): ";
                for(Role nnn : n.getAddingroles()) {
                    list += nnn.getAsMention()+" ";
                }

                eb.addField("", list, true);
                eb.setColor(Color.RED);
                eb.setTimestamp(Instant.now());
                channel.sendMessage(eb.build()).queue();

            }



        }



        if(args[1].contains("create") && evt.getMember().getRoles().contains(star)) {

            evt.reply(createEmbed("", "What roles should be detected").build());
            waiter.waitForEvent(MessageReceivedEvent.class,
                    e -> e.getAuthor().equals(evt.getAuthor()) && e.getChannel().equals(evt.getChannel()),
                    e -> {


                        ArrayList<Role> detectedroles = new ArrayList<Role>();
                        for (Role n : e.getMessage().getMentionedRoles()) {
                            detectedroles.add(n);
                        }
                        String message = "The detected roles are ";
                        for (Role n : e.getMessage().getMentionedRoles()) {
                            message += "" + n.getAsMention() + "";
                        }
                        message += ".";


                        evt.reply(createEmbed("", "What roles should be applied?").build());


                        waiter.waitForEvent(MessageReceivedEvent.class, ee -> ee.getAuthor().equals(evt.getAuthor()) && ee.getChannel().equals(evt.getChannel()),
                                ee -> {

                                    ArrayList<Role> addingroles = new ArrayList<Role>();
                                    for (Role n : ee.getMessage().getMentionedRoles()) {
                                        addingroles.add(n);
                                    }

                                    String message1 = "The role(s) ";
                                    for (Role n : addingroles) {
                                        message1 += "" + n.getAsMention() + " ";
                                    }
                                    message1 += "will be added if the roles ";

                                    for (Role nn : detectedroles) {
                                        message1 += "" + nn.getAsMention() + " ";
                                    }
                                    message1 += " are added to a user.";
                                    evt.reply(createEmbed(message1, "").build());

                                    /*
                                     End of EventWaiter
                                     */

                                    String namee = evt.getMessage().getContentRaw().substring(16);


                                    AutoRoleManager.addAutoRole(namee, detectedroles, addingroles, evt.getMessage());


                                },
                                1, TimeUnit.MINUTES, () -> evt.reply("This response has timed out"));


                    },
                    1, TimeUnit.MINUTES, () -> evt.reply("This response has timed out"));

        }
    }


    public static EmbedBuilder createEmbed(String text, String title) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.RED);
        eb.addField(title, text, true);
        eb.setTimestamp(Instant.now());
        return eb;
    }

}
