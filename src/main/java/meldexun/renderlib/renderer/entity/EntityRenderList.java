package meldexun.renderlib.renderer.entity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraftforge.client.MinecraftForgeClient;

public class EntityRenderList {

	private final List<Entity> entitiesPass0 = new ArrayList<>();
	private final List<Entity> multipassEntitiesPass0 = new ArrayList<>();
	private final List<Entity> outlineEntitiesPass0 = new ArrayList<>();
	private final List<Entity> entitiesPass1 = new ArrayList<>();
	private final List<Entity> multipassEntitiesPass1 = new ArrayList<>();
	private final List<Entity> outlineEntitiesPass1 = new ArrayList<>();

	public void addEntity(Entity entity) {
		if (entity.shouldRenderInPass(0))
			this.entitiesPass0.add(entity);
		if (entity.shouldRenderInPass(1))
			this.entitiesPass1.add(entity);
	}

	public void addMultipassEntity(Entity entity) {
		if (entity.shouldRenderInPass(0))
			this.multipassEntitiesPass0.add(entity);
		if (entity.shouldRenderInPass(1))
			this.multipassEntitiesPass1.add(entity);
	}

	public void addOutlineEntity(Entity entity) {
		if (entity.shouldRenderInPass(0))
			this.outlineEntitiesPass0.add(entity);
		if (entity.shouldRenderInPass(1))
			this.outlineEntitiesPass1.add(entity);
	}

	public List<Entity> getEntities() {
		return MinecraftForgeClient.getRenderPass() == 0 ? this.entitiesPass0 : this.entitiesPass1;
	}

	public List<Entity> getMultipassEntities() {
		return MinecraftForgeClient.getRenderPass() == 0 ? this.multipassEntitiesPass0 : this.multipassEntitiesPass1;
	}

	public List<Entity> getOutlineEntities() {
		return MinecraftForgeClient.getRenderPass() == 0 ? this.outlineEntitiesPass0 : this.outlineEntitiesPass1;
	}

}
