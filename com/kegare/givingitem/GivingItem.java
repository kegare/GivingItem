package com.kegare.givingitem;

import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkCheckHandler;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = "kegare.givingitem")
public class GivingItem
{
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(this);
	}

	@NetworkCheckHandler
	public boolean netCheckHandler(Map<String, String> mods, Side side)
	{
		return true;
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onEntityInteract(EntityInteractEvent event)
	{
		if (event.entityPlayer instanceof EntityPlayerMP)
		{
			EntityPlayerMP player = (EntityPlayerMP)event.entityPlayer;
			ItemStack current = player.getCurrentEquippedItem();

			if (current == null || !player.isSneaking() || !event.target.isEntityAlive())
			{
				return;
			}

			if (current.getItem() instanceof ItemBucket)
			{
				return;
			}

			Entity entity = event.target;
			ItemStack itemstack = current.copy();

			if (entity instanceof EntityLivingBase)
			{
				EntityLivingBase target = (EntityLivingBase)entity;

				if (target.getHeldItem() == null)
				{
					target.setCurrentItemOrArmor(0, itemstack);

					if (ItemStack.areItemStacksEqual(target.getHeldItem(), itemstack))
					{
						player.inventory.setInventorySlotContents(player.inventory.currentItem, null);

						player.playSound("random.pop", 1.0F, 1.0F);
						target.playSound("random.pop", 1.0F, 1.0F);

						event.setCanceled(true);

						return;
					}

					player.inventory.setInventorySlotContents(player.inventory.currentItem, itemstack);
				}
			}

			if (entity instanceof EntityPlayerMP)
			{
				EntityPlayerMP target = (EntityPlayerMP)entity;

				if (target.inventory.addItemStackToInventory(current))
				{
					player.playSound("random.pop", 1.0F, 1.0F);
					target.playSound("random.pop", 1.0F, 1.0F);

					event.setCanceled(true);
				}
				else
				{
					current = player.getCurrentEquippedItem();

					if (current == null || current.stackSize <= 0)
					{
						player.inventory.setInventorySlotContents(player.inventory.currentItem, itemstack);
					}
				}
			}
		}
	}
}