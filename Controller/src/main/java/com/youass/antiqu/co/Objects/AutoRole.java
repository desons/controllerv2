package com.youass.antiqu.co.Objects;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.List;

public class AutoRole extends ListenerAdapter {

    private String name;
    private List<Role> dectectables;
    private List<Role> addingroles;
    private Message sentmessage;

    public AutoRole(String name, List<Role> dectectables, List<Role> addingroles, Message sentmessage) {

        this.name = name;
        this.dectectables = dectectables;
        this.addingroles = addingroles;
        this.sentmessage = sentmessage;

        setUp();

    }

    private void setUp() {
        sentmessage.getJDA().addEventListener(this);
    }


    public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent evt) {

        boolean hasarole = false;

        for (Role n : dectectables) {
            if (evt.getRoles().contains(n)) {
                hasarole = true;
                break;
            }
        }

        int i = addingroles.size();
        int ii = 0;

        for(Role n : addingroles) {
            if(evt.getMember().getRoles().contains(n)) {
                ii++;
            }
            if(i == ii) {
                return;
            }
        }

        if (hasarole) {

            evt.getMember().getUser().openPrivateChannel().queue((chan) -> {

                Role detected = null;

                for (Role n : dectectables) {
                    if (evt.getRoles().contains(n)) {
                        detected = n;
                        evt.getGuild().getController().addRolesToMember(evt.getMember(), addingroles).queue();
                        break;
                    }
                }

                String message = "The role(s) ";
                for (Role n : addingroles) {
                    message += "`" + n.getName() + "` ";
                }
                message += " has been added because you received the role `" + detected.getName()+"`";

                chan.sendMessage(message).queue();

            });

        }
    }




    public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent evt) {

        boolean hasarole = false;

        for (Role n : dectectables) {
            if (evt.getRoles().contains(n)) {
                hasarole = true;
                break;
            }
        }

        if (hasarole) {

            evt.getMember().getUser().openPrivateChannel().queue((chan) -> {

                Role detected = null;

                for(Role n : dectectables) {
                    if(evt.getMember().getRoles().contains(n)) {
                        return;
                    }
                }

                for (Role n : dectectables) {
                    if (evt.getRoles().contains(n)) {
                        detected = n;
                        evt.getGuild().getController().removeRolesFromMember(evt.getMember(), addingroles).queue();
                        break;
                    }
                }


                String message = "The role(s) ";
                for (Role n : addingroles) {
                    message += "`" + n.getName() + "` ";
                }
                message += " has been removed because you removed the role `" + detected.getName()+"`";

                chan.sendMessage(message).queue();

            });

        }
    }


    public void delete() {
        sentmessage.getJDA().removeEventListener(this);
    }

    public String getName() {
        return name;
    }

    public List<Role> getAddingroles() {
        return addingroles;
    }

    public List<Role> getDectectables() {
        return dectectables;
    }

    public Message getSentmessage() {
        return sentmessage;
    }
}
