package stingray.content;

import mindustry.*;
import mindustry.gen.*
import mindustry.type.*;
import mindustry.ctype.*;
import mindustry.content.*;
import mindustry.world.blocks.units.*;
import mindustry.world.blocks.payloads.*
import stingray.type.*
import stingray.content.*;

object StingrayTechTree : ContentList {
	
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
		
		//make it possible to produce stingrays via a payload source
		Blocks.payloadSource.config(StingrayUnitType::class.java) { build: Building, unit: StingrayUnitType ->
			if (build is PayloadSource.PayloadSourceBuild && (Blocks.payloadSource as PayloadSource).canProduce(unit) && unit != build.unit) {
				build.unit = unit;
				//build.block = null; todo: what the fuck anuken
				build.payload = null;
				build.scl = 0f;
			}
		}
	}
	
	fun unitTech(unit: UnitType, parent: UnitType) {
		TechTree.TechNode(TechTree.get(parent), unit, unit.researchRequirements());
	};
	
}