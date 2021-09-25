package com.norsecraft.common.registry;

import com.norsecraft.NorseCraftMod;
import com.norsecraft.client.gui.DwarfTradeGuiInterpretation;
import com.norsecraft.common.gui.CrateGuiInterpretation;
import com.norsecraft.common.screenhandler.NorseCraftInventoryScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;

public class NCScreenHandlers {

    public static ScreenHandlerType<DwarfTradeGuiInterpretation> dwarfTrade;
    public static ScreenHandlerType<NorseCraftInventoryScreenHandler> norseCraftInventory;
    public static ScreenHandlerType<CrateGuiInterpretation> crate;

    public static void register() {
        NorseCraftMod.LOGGER.info("Register screen handlers");
        dwarfTrade = registerExtended("dwarf_trade", ((syncId, inventory, buf) -> new DwarfTradeGuiInterpretation(syncId, inventory, ScreenHandlerContext.EMPTY, buf)));
        norseCraftInventory = registerSimple("norsecraft_inv", NorseCraftInventoryScreenHandler::new);
        crate = registerSimple("crate", ((syncId, inventory) -> new CrateGuiInterpretation(syncId, inventory, ScreenHandlerContext.EMPTY)));
    }

    public static <R extends ScreenHandler> ScreenHandlerType<R> registerExtended(String name, ScreenHandlerRegistry.ExtendedClientHandlerFactory<R> factory) {
        return ScreenHandlerRegistry.registerExtended(NorseCraftMod.nc(name), factory);
    }

    public static <R extends ScreenHandler> ScreenHandlerType<R> registerSimple(String name, ScreenHandlerRegistry.SimpleClientHandlerFactory<R> factory) {
        return ScreenHandlerRegistry.registerSimple(NorseCraftMod.nc(name), factory);
    }


}
