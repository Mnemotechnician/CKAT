package stingray;

import io.mnemotechnician.autoupdater.*;
import arc.*;
import arc.util.*;
import mindustry.game.*;
import mindustry.mod.*;
import mindustry.type.*;
import mindustry.content.*;
import mindustry.world.blocks.units.*;

import stingray.content.*;

open class StingrayMod : Mod() {
	
	init {
		Events.on(EventType.ClientLoadEvent::class.java) {
			Updater.checkUpdates(this);
		};
	}

	override open fun loadContent() {
		StingrayFx().load();
		StingrayUnitTypes().load();
		StingrayTechTree().load();
		
		//Add construction plans, idk where to put it other than here
		val cost = ItemStack.with(Items.graphite, 20, Items.silicon, 35, Items.titanium, 10, Items.metaglass, 10);
		(Blocks.navalFactory as UnitFactory).plans.add(UnitFactory.UnitPlan(StingrayUnitTypes.urotry, 60 * 30f, cost));
		
		(Blocks.additiveReconstructor as Reconstructor).addUpgrade(StingrayUnitTypes.urotry, StingrayUnitTypes.mylio);
		(Blocks.multiplicativeReconstructor as Reconstructor).addUpgrade(StingrayUnitTypes.mylio, StingrayUnitTypes.undulate);
		(Blocks.exponentialReconstructor as Reconstructor).addUpgrade(StingrayUnitTypes.undulate, StingrayUnitTypes.dasya);
	}
}
