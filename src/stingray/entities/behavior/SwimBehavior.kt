package stingray.entities.behavior;

import mindustry.content.*;
import stingray.entities.*;

open class SwimBehavior(var multiplier: Float) : BehaviorPattern() {
	
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
	
}