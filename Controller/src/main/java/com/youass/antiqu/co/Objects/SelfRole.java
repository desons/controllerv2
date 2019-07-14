package com.youass.antiqu.co.Objects;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.awt.*;
import java.time.Instant;

public class SelfRole extends ListenerAdapter {

    private Role role;
    private Message message;
    private Message sendingmessage;
    private Member member; //Member who set the role
    private TextChannel sendtochannel;


    public SelfRole(Role role, Message sendingmessage, Member member, TextChannel sendtochannel) {

        this.member = member;
        this.role = role;
        this.sendingmessage = sendingmessage;
        this.sendtochannel = sendtochannel;

        setUp();

    }

    private void setUp() {

        EmbedBuilder eba = new EmbedBuilder();

        eba.addField("**"+role.getName()+"**", "Click the :white_check_mark: to receive the "+role.getAsMention()+" role.", true);
        eba.setColor(Color.RED);
        eba.setTimestamp(Instant.now());

        sendtochannel.sendMessage(eba.build()).queue( (msg) -> {
            setMessage(msg);
            msg.addReaction("✅").queue();
        });

        sendtochannel.getJDA().addEventListener(this);

    }

    @Override
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent evt) {
        String evtmessageid = evt.getMessageId();

        if(evtmessageid.equalsIgnoreCase(message.getId())) {

            if(evt.getMember().getRoles().contains(role)) {
                evt.getMember().getUser().openPrivateChannel().queue((chan) -> {
                    chan.sendMessage("You already have the `"+role.getName()+"` role.").queue();
                });
                return;
            }

            if(!evt.getUser().equals(evt.getJDA().getSelfUser()) && evt.getReactionEmote().getName().equals("✅")) {

                evt.getGuild().getController().addRolesToMember(evt.getMember(),role).queue();

                evt.getMember().getUser().openPrivateChannel().queue((chan) -> {
                    chan.sendMessage("You have received the `"+role.getName()+"` role.").queue();
                });

                return;
            }

        }
    }

    @Override
    public void onGuildMessageReactionRemove(GuildMessageReactionRemoveEvent evt) {
        String evtmessageid = evt.getMessageId();

        if(evtmessageid.equalsIgnoreCase(message.getId())) {

            if(!evt.getMember().getRoles().contains(role)) {
                evt.getMember().getUser().openPrivateChannel().queue((chan) -> {
                    chan.sendMessage("You do not have the `"+role.getName()+"` role.").queue();
                });
                return;
            }

            if(!evt.getUser().equals(evt.getJDA().getSelfUser()) && evt.getReactionEmote().getName().equals("✅")) {

                evt.getGuild().getController().removeRolesFromMember(evt.getMember(),role).queue();

                evt.getMember().getUser().openPrivateChannel().queue((chan) -> {
                    chan.sendMessage("You have removed the `"+role.getName()+"` role.").queue();
                });

                return;
            }

        }
    }



    private void setMessage(Message message) {
        this.message = message;
    }

    public Member getMember() {
        return member;
    }

    public Message getMessage() {
        return message;
    }

    public Message getSendingmessage() {
        return sendingmessage;
    }

    public Role getRole() {
        return role;
    }

    public TextChannel getSendtochannel() {
        return sendtochannel;
    }


}
