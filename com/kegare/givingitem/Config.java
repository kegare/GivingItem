package com.kegare.givingitem;

import java.io.File;
import java.util.List;

import net.minecraft.util.StatCollector;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Loader;

import org.apache.logging.log4j.Level;

import com.google.common.collect.Lists;

public class Config
{
	public static Configuration config;

	public static boolean giveToPlayer;
	public static boolean giveToMob;
	public static boolean swapWithPlayer;
	public static boolean swapWithMob;

	public static void syncConfig()
	{
		if (config == null)
		{
			File file = new File(Loader.instance().getConfigDir(), "GivingItem.cfg");
			config = new Configuration(file);

			try
			{
				config.load();
			}
			catch (Exception e)
			{
				File dest = new File(file.getParentFile(), file.getName() + ".bak");

				if (dest.exists())
				{
					dest.delete();
				}

				file.renameTo(dest);

				FMLLog.log(Level.ERROR, e, "A critical error occured reading the " + file.getName() + " file, defaults will be used - the invalid file is backed up at " + dest.getName());
			}
		}

		String category = Configuration.CATEGORY_GENERAL;
		Property prop;
		List<String> propOrder = Lists.newArrayList();

		prop = config.get(category, "giveToPlayer", true);
		prop.setLanguageKey("givingitem.config." + category + "." + prop.getName());
		prop.comment = StatCollector.translateToLocal(prop.getLanguageKey() + ".tooltip");
		prop.comment += " [default: " + prop.getDefault() + "]";
		propOrder.add(prop.getName());
		giveToPlayer = prop.getBoolean(giveToPlayer);
		prop = config.get(category, "giveToMob", true);
		prop.setLanguageKey("givingitem.config." + category + "." + prop.getName());
		prop.comment = StatCollector.translateToLocal(prop.getLanguageKey() + ".tooltip");
		prop.comment += " [default: " + prop.getDefault() + "]";
		propOrder.add(prop.getName());
		giveToMob = prop.getBoolean(giveToMob);
		prop = config.get(category, "swapWithPlayer", false);
		prop.setLanguageKey("givingitem.config." + category + "." + prop.getName());
		prop.comment = StatCollector.translateToLocal(prop.getLanguageKey() + ".tooltip");
		prop.comment += " [default: " + prop.getDefault() + "]";
		propOrder.add(prop.getName());
		swapWithPlayer = prop.getBoolean(swapWithPlayer);
		prop = config.get(category, "swapWithMob", false);
		prop.setLanguageKey("givingitem.config." + category + "." + prop.getName());
		prop.comment = StatCollector.translateToLocal(prop.getLanguageKey() + ".tooltip");
		prop.comment += " [default: " + prop.getDefault() + "]";
		propOrder.add(prop.getName());
		swapWithMob = prop.getBoolean(swapWithMob);

		config.setCategoryPropertyOrder(category, propOrder);

		if (config.hasChanged())
		{
			config.save();
		}
	}
}