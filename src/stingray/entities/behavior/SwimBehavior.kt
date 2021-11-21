package stingray.entities.behavior;

import arc.*;
import arc.scene.ui.layout.*;
import mindustry.content.*;
import stingray.entities.*;

open class SwimBehavior(var multiplier: Float) : BehaviorPattern("swim") {
	
	override open fun apply(parent: mindustry.gen.Unit) {
		val floor = parent.floorOn();
		
		if (floor.liquidDrop == Liquids.water) {
			val multi = if (floor.isDeep) {
				multiplier * multiplier;
			} else {
				multiplier;
			}
			
			parent.speedMultiplier *= multi / floor.speedMultiplier;
		}
	}
	
	override open fun display(table: Table) {
		super.display(table);
		
		val speed: String = Core.bundle["ckat-stingray-speed"];
		table.table {
			it.add("@ckat-stingray.stat.swim-shallow").marginRight(5f);
			it.add("@ckat-stingray.stat.swim-deep").row();
			it.add("${multiplier}x $speed");
			it.add("${Math.round(multiplier * multiplier * 100f) / 100f}x $speed");
		}.growY();
	}
	
}