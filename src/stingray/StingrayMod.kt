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
		Events.on(EventType.ClientLoadEvent::class.java, {
			Updater.checkUpdates(this);
		});
	}

	override open fun loadContent() {
		StingrayFx().load();
		StingrayUnitTypes().load();
		StingrayTechTree().load();
		
		//Add construction plans, idk where to put it other than here
		Blocks.navalFactory.plans.add(
			new UnitFactory.UnitPlan(StringrayUnitTypes.Urotry, 60 * 30f, ItemStack.with(
				Items.graphite, 20,
				Items.silicon, 35,
				Items.titanium, 10, 
				Items.metaglass, 10
			))
		);
		Blocks.additiveReconstructor.addUpgrade(StingrayUnitTypes.Urotry, StringrayUnitTypes.Mylio);
		Blocks.multiplicativeReconstructor.addUpgrade(StingrayUnitTypes.Mylio, StingrayUnitTypes.Undulate);
		Blocks.exponentialReconstructor.addUpgrade(StingrayUnitTypes.Undulate, StingrayUnitTypes.Dasya);
	}
}
