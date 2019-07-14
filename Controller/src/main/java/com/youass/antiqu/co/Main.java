package com.youass.antiqu.co;

import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.youass.antiqu.co.Events.AutoRoleListener;
import com.youass.antiqu.co.Events.SelfRoleListener;
import com.youass.antiqu.co.Objects.AutoRole;
import com.youass.antiqu.co.Objects.AutoRoleManager;
import com.youass.antiqu.co.Objects.SelfRoleManager;
import com.youass.antiqu.co.Util.REF;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class Main extends ListenerAdapter {

    public static void main(String[] args) throws Exception {

        JDA jda = new JDABuilder().setToken(REF.token).setStatus(OnlineStatus.ONLINE).build();

        EventWaiter eventWaiter = new EventWaiter();
        CommandClientBuilder commandClientBuilder = new CommandClientBuilder();

        commandClientBuilder.setPrefix("/");
        commandClientBuilder.setGame(Game.watching("Antiqu Development"));
        commandClientBuilder.setStatus(OnlineStatus.ONLINE);
        commandClientBuilder.setOwnerId("499911364802904074");
        commandClientBuilder.addCommand(new AutoRoleListener(eventWaiter));

        CommandClient client = commandClientBuilder.build();

        jda.addEventListener(new SelfRoleListener());
        jda.addEventListener(eventWaiter);
        jda.addEventListener(client);
        //Setting Management objects
        SelfRoleManager selfRoleManager = new SelfRoleManager();
        AutoRoleManager autoRoleManager = new AutoRoleManager();

    }

}
