package stingray.content;

import mindustry.*;
import mindustry.type.*;
import mindustry.ctype.*;
import mindustry.content.*;
import mindustry.world.blocks.units.*;

import stingray.content.*;

open class StingrayTechTree : ContentList {
	
	override open fun load() {
		unitTech(StingrayUnitTypes.urotry, UnitTypes.dagger);
		unitTech(StingrayUnitTypes.mylio, StingrayUnitTypes.urotry);
		unitTech(StingrayUnitTypes.undulate, StingrayUnitTypes.mylio);
		unitTech(StingrayUnitTypes.dasya, StingrayUnitTypes.undulate);
		
		//Add construction plans
		val cost = ItemStack.with(Items.graphite, 20, Items.silicon, 35, Items.titanium, 10, Items.metaglass, 10);
		(Blocks.navalFactory as UnitFactory).plans.add(UnitFactory.UnitPlan(StingrayUnitTypes.urotry, 60 * 30f, cost));
		
		(Blocks.additiveReconstructor as Reconstructor).addUpgrade(StingrayUnitTypes.urotry, StingrayUnitTypes.mylio);
		(Blocks.multiplicativeReconstructor as Reconstructor).addUpgrade(StingrayUnitTypes.mylio, StingrayUnitTypes.undulate);
		(Blocks.exponentialReconstructor as Reconstructor).addUpgrade(StingrayUnitTypes.undulate, StingrayUnitTypes.dasya);
	}
	
	inline fun unitTech(unit: UnitType, parent: UnitType) {
		TechTree.TechNode(TechTree.get(parent), unit, unit.researchRequirements());
	};
	
}