package com.lfaoanl.marketcrates.network;

import com.lfaoanl.marketcrates.References;
import com.lfaoanl.marketcrates.network.packets.CrateItemsPacket;
import com.lfaoanl.marketcrates.tileentities.CrateTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class CratesPacketHandler {


    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(References.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void init(FMLCommonSetupEvent event) {
        INSTANCE.registerMessage(0, CrateItemsPacket.class, CrateItemsPacket::encode, CrateItemsPacket::decode, CratesPacketHandler::handle);
    }

    public static void handle(final CrateItemsPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {

            World world = Minecraft.getInstance().world;

            if (world != null) {
                TileEntity tile = world.getTileEntity(msg.getPosition());

                if (tile instanceof CrateTileEntity) {
                    ((CrateTileEntity) tile).receiveContents(msg.items);
                }
            }
        });

        ctx.get().setPacketHandled(true);
    }


}
