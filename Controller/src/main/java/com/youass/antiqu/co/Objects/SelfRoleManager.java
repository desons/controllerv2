package com.youass.antiqu.co.Objects;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;

public class SelfRoleManager {

    private static ArrayList<SelfRole> selfRoles;

    public SelfRoleManager() {
        selfRoles = new ArrayList<SelfRole>();
    }

    public static void createSelfRoleMessage(Role role, Message sendingmessage, Member member, TextChannel sendtochannel, GuildMessageReceivedEvent evt) {
        if(roleIsInArray(role)) {
            sendingmessage.getTextChannel().sendMessage("This Role already has a Self Role").queue();
            return;
        }

        SelfRole selfRole = new SelfRole(role,sendingmessage,member,sendtochannel);
        selfRoles.add(selfRole);

    }

    public static boolean roleIsInArray(Role role) {
        for(SelfRole n : selfRoles) {
            if(n.getRole() == role) {
                return true;
            }
        }
        return false;
    }

    public static int getNumberInArrayFromRole(Role role) {
        int number = 0;
        for(int i = 0; i < selfRoles.size(); i++) {
            if(selfRoles.get(i).getRole() == role) {
                number = i;
            }
        }
        return number;
    }

}
