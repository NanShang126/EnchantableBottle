package org.nanshang.enchantableBottle;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class PlayerInteractEventListener implements Listener {
    private static final Random random = new Random();

    // 检查物品是否为玻璃瓶
    private boolean isBottle(ItemStack item) {
        return item != null && item.getType().equals(Material.GLASS_BOTTLE);
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        // 空值检查并确保物品是玻璃瓶且方块是附魔台
        if (event.getItem() == null || !isBottle(event.getItem()) || event.getClickedBlock() == null || !event.getClickedBlock().getType().equals(Material.ENCHANTING_TABLE))
            return;

        // 取消事件以防止打开附魔界面
        event.setCancelled(true);

        // 确保玩家有足够的等级，最低为5级
        if (event.getPlayer().getTotalExperience() < 6)
            return;

        // 从玩家的背包中移除玻璃瓶
        event.getPlayer().getInventory().removeItem(new ItemStack(Material.GLASS_BOTTLE));

        // 在附魔台上生成一个经验瓶
        event.getPlayer().getWorld().dropItem(event.getClickedBlock().getLocation().add(0.5, 1, 0.5), new ItemStack(Material.EXPERIENCE_BOTTLE));

        // 扣除经验值（3到12之间）
        int expToGive = random.nextInt(15) + 5;
        event.getPlayer().giveExp(-expToGive);

        // 播放经验值球拾取的声音效果
        event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, random.nextFloat() * 0.5F + 0.5F);
    }
}
