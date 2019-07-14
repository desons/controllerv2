package com.youass.antiqu.co.Events;

import com.youass.antiqu.co.Objects.AutoRoleManager;
import com.youass.antiqu.co.Objects.SelfRoleManager;
import com.youass.antiqu.co.Util.REF;
import net.dv8tion.jda.client.events.message.group.react.GroupMessageReactionAddEvent;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class SelfRoleListener extends ListenerAdapter {

    private TextChannel sendtochannel;

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent evt) {

        Member member = evt.getMember();
        User user = evt.getAuthor();
        Guild guild = evt.getGuild();
        TextChannel channel = evt.getChannel();
        Message message = evt.getMessage();
        String[] args = evt.getMessage().getContentRaw().split(" ");

        Role star = evt.getGuild().getRolesByName("*", true).get(0);

        if(args[0].equalsIgnoreCase(REF.prefix+"selfrole") && member.getRoles().contains(star)) {

            if(args[1].equalsIgnoreCase("setchannel")) {
                sendtochannel = message.getMentionedChannels().get(0);
                channel.sendMessage("You have set the `self role message channel` to "+sendtochannel.getAsMention()).queue();
                return;
            }

            if(sendtochannel == null) {
                channel.sendMessage("Please do `/selfrole setchannel #channel`").queue();
                return;
            }

            if(args[1].equalsIgnoreCase("create")) {
                Role role = message.getMentionedRoles().get(0);
                SelfRoleManager.createSelfRoleMessage(role, message, member, sendtochannel, evt);
                return;
            }





            if(args.length < 1) {
                channel.sendMessage("Please do `/selfrole create @role`").queue();
                return;
            }



        }

    }


    //Auto role delete x-emoji


    @Override
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent evt) {

        if (AutoRoleListener.getMostrecentdelete() != null && AutoRoleListener.getMostrecentname() != null) {

            if (evt.getMessageId().equals(AutoRoleListener.getMostrecentdelete().getId())) {

                if (!evt.getUser().equals(evt.getJDA().getSelfUser()) && evt.getReactionEmote().getName().equals("✅")) {

                    evt.getChannel().sendMessage(AutoRoleListener.createEmbed("", "" + AutoRoleListener.getMostrecentname() + " has been deleted.").build()).queue();
                    AutoRoleManager.removeAutoRole(AutoRoleListener.getMostrecentname());

                }

                if(!evt.getUser().equals(evt.getJDA().getSelfUser()) && evt.getReactionEmote().getName().equals("❌")) {
                  evt.getChannel().sendMessage(AutoRoleListener.createEmbed("", "Deletetion **cancled**").build()).queue();
                  return;
                }

            }

        }
    }
}
