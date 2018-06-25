package swordstat.network;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import swordstat.Main;
import swordstat.init.EntitySorter.EntitySorting;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SendEntitySortMessage implements IMessage {

	private EntitySorting entitySorting;
	
	public SendEntitySortMessage() {

	}
	
	public SendEntitySortMessage( final EntitySorting entitySorting ) {
		
		this.entitySorting = entitySorting;
	}
	
	public EntitySorting getEntitySorting() {
		
		return entitySorting;
	}
	
	@Override
	public void toBytes( ByteBuf buf ) {
		
		ObjectMapper objectMapper = new ObjectMapper();
		String mapJsonString;
		try {
			mapJsonString = objectMapper.writeValueAsString(entitySorting.getInternalMapping());
		}
		catch ( JsonProcessingException e ) {
			Main.LOGGER.error("Error converting the entity sorting to JSON for sending to client");
			Main.LOGGER.error(e.getMessage());
			return;
		}
		ByteBufUtils.writeUTF8String(buf, mapJsonString);
	}

	@Override
	public void fromBytes( ByteBuf buf ) {

		String mapJsonString = ByteBufUtils.readUTF8String(buf);		
		ObjectMapper mapper = new ObjectMapper();
		
		Map<String, Map<String, Class<? extends Entity>>> map = null;
		try {
			map = mapper.readValue(
					mapJsonString,
					new TypeReference<Map<String, Map<String, Class<? extends Entity>>>>(){}
			);
		}
		catch ( IOException e ){
			Main.LOGGER.error("Error reading json");
			Main.LOGGER.error(e.getMessage());
		}
		catch ( ClassCastException e ){
			Main.LOGGER.error("Error casting");
		}
		
		if ( map == null ){
			entitySorting = new EntitySorting(new HashMap<String, Map<String, Class<? extends Entity>>>());
		}
		else {
			entitySorting = new EntitySorting(map);
		}
	}
}
