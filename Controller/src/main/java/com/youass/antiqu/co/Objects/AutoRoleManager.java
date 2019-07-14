package com.youass.antiqu.co.Objects;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.Role;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AutoRoleManager {

    private static HashMap<String,AutoRole> autorolemap;

    public AutoRoleManager() {
        autorolemap = new HashMap<>();
    }

    public static void addAutoRole(String name, List<Role> dectectables, List<Role> addingroles, Message sentmessage) {

        AutoRole autoRole = new AutoRole(name,dectectables,addingroles,sentmessage);
        autorolemap.put(name,autoRole);

    }

    public static void removeAutoRole(String name) {
        AutoRole autoRole = autorolemap.get(name);
        autoRole.delete();
        autorolemap.remove(name);
    }

    public static boolean isInMap(String name) {
        if(autorolemap.containsKey(name)) {
            return true;
        }
        return false;
    }

    public static HashMap<String,AutoRole> getMap() {
        return autorolemap;
    }



}
