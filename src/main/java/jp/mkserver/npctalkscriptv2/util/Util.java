package jp.mkserver.npctalkscriptv2.util;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Util {

    public static String getItemName(ItemStack item){
        if(!item.hasItemMeta()||!item.getItemMeta().hasDisplayName()){
            return item.getType().name();
        }
        return item.getItemMeta().getDisplayName();
    }

    public static ItemStack createItem(String name, String[] lore, Material item, int dura, boolean enchant,boolean unbreakable){
        ItemStack items = new ItemStack(item,1);
        ItemMeta meta = items.getItemMeta();
        if (meta instanceof Damageable){
            ((Damageable) meta).setDamage(dura);
        }
        meta.setLore(Arrays.asList(lore));
        meta.setDisplayName(name);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        if(enchant){
            meta.addEnchant(Enchantment.ARROW_FIRE,1,true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if(unbreakable){
            meta.setUnbreakable(true);
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        }
        items.setItemMeta(meta);
        return items;
    }

    public static ItemStack createItem(String name, String[] lore, Material item, int dura, boolean enchant,boolean unbreakable,int customModel){
        ItemStack items = new ItemStack(item,1);
        ItemMeta meta = items.getItemMeta();
        if (meta instanceof Damageable){
            ((Damageable) meta).setDamage(dura);
        }
        meta.setCustomModelData(customModel);
        meta.setLore(Arrays.asList(lore));
        meta.setDisplayName(name);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        if(enchant){
            meta.addEnchant(Enchantment.ARROW_FIRE,1,true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if(unbreakable){
            meta.setUnbreakable(true);
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        }
        items.setItemMeta(meta);
        return items;
    }

    public static ItemStack overhaulItem(ItemStack item,String name, String[] lore){
        ItemMeta meta = item.getItemMeta();
        if(lore!=null){
            meta.setLore(Arrays.asList(lore));
        }
        if(name!=null){
            meta.setDisplayName(name);
        }
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createBannerItem(Material banner_mt,List<Pattern> patterns){
        ItemStack banner = new ItemStack(banner_mt);
        if(banner.getItemMeta() instanceof BannerMeta){
            BannerMeta meta = (BannerMeta)banner.getItemMeta();
            for(Pattern pat : patterns){
                meta.addPattern(pat);
            }
            meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            banner.setItemMeta(meta);
            return banner;
        }
        return null;
    }


    public static ItemStack createSkullitem(String name, String[] lore, UUID playeruuid, boolean enchant){
        ItemStack items = new ItemStack(Material.PLAYER_HEAD,1);
        SkullMeta meta = (SkullMeta) items.getItemMeta();
        meta.setLore(Arrays.asList(lore));
        meta.setDisplayName(name);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        if(enchant){
            meta.addEnchant(Enchantment.ARROW_FIRE,1,true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        meta.setUnbreakable(true);
        meta.setOwningPlayer(Bukkit.getPlayer(playeruuid));
        items.setItemMeta(meta);
        return items;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    //  マインクラフトチャットに、ホバーテキストや、クリックコマンドを設定する関数
    // [例1] sendHoverText(player,"ここをクリック",null,"/say おはまん");
    // [例2] sendHoverText(player,"カーソルをあわせて","ヘルプメッセージとか",null);
    // [例3] sendHoverText(player,"カーソルをあわせてクリック","ヘルプメッセージとか","/say おはまん");
    public static void sendHoverText(Player p, String text, String hoverText, String command){
        //////////////////////////////////////////
        //      ホバーテキストとイベントを作成する
        HoverEvent hoverEvent = null;
        if(hoverText != null){
            BaseComponent[] hover = new ComponentBuilder(hoverText).create();
            hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, hover);
        }

        //////////////////////////////////////////
        //   クリックイベントを作成する
        ClickEvent clickEvent = null;
        if(command != null){
            clickEvent = new ClickEvent(ClickEvent.Action.RUN_COMMAND,command);
        }

        BaseComponent[] message = new ComponentBuilder(text).event(hoverEvent).event(clickEvent). create();
        p.spigot().sendMessage(message);
    }

    //  マインクラフトチャットに、ホバーテキストや、クリックコマンドサジェストを設定する
    public static void sendSuggestCommand(Player p, String text, String hoverText, String command){

        //////////////////////////////////////////
        //      ホバーテキストとイベントを作成する
        HoverEvent hoverEvent = null;
        if(hoverText != null){
            BaseComponent[] hover = new ComponentBuilder(hoverText).create();
            hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, hover);
        }

        //////////////////////////////////////////
        //   クリックイベントを作成する
        ClickEvent clickEvent = null;
        if(command != null){
            clickEvent = new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND ,command);
        }

        BaseComponent[] message = new ComponentBuilder(text). event(hoverEvent).event(clickEvent). create();
        p.spigot().sendMessage(message);
    }
}
