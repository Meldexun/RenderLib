package meldexun.renderlib.integration;

import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

import de.teamlapen.vampirism.client.render.RenderHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventBus;

public class Vampirism {

	private static RenderHandler renderHandler;
	static {
		try {
			Field listenersField = EventBus.class.getDeclaredField("listeners");
			listenersField.setAccessible(true);
			@SuppressWarnings("unchecked")
			Enumeration<Object> keys = ((ConcurrentHashMap<Object, ?>) listenersField.get(MinecraftForge.EVENT_BUS)).keys();
			while (keys.hasMoreElements()) {
				Object key = keys.nextElement();
				if (key instanceof RenderHandler) {
					renderHandler = (RenderHandler) key;
					break;
				}
			}
			if (renderHandler == null) {
				throw new NullPointerException();
			}
		} catch (Exception e) {
			throw new UnsupportedOperationException("Failed to find de.teamlapen.vampirism.client.render.RenderHandler instance", e);
		}
	}

	public static void onRenderLivingPost(EntityLivingBase entity) {
		renderHandler.onRenderLivingPost(new RenderLivingEvent.Post<>(entity, null, 0.0F, 0.0D, 0.0D, 0.0D));
	}

}
