package stingray.ai.types;

import mindustry.*;
import mindustry.ai.*;
import mindustry.world.meta.*;
import mindustry.entities.*;
import mindustry.entities.units.*;

open class StingrayAI : AIController() {
	
	//too much fun
	override open fun updateMovement() {

		val e = Units.closestTarget(unit.team, unit.x, unit.y, 160f, {true});
		if (e != null) {
			target = e;
			moveTo(e, 6f);
		} else if (command() == UnitCommand.rally) {
			target = targetFlag(unit.x, unit.y, BlockFlag.rally, false);
			if (target != null && !unit.within(target, 70f)) {
				pathfind(Pathfinder.fieldRally);
				faceTarget();
				return;
			}
		} else if (command() != UnitCommand.idle) {
			val spawner = getClosestSpawner();
			if (spawner != null && !unit.within(spawner, Vars.state.rules.dropZoneRadius + unit.hitSize * 1.5f)) {
				pathfind(Pathfinder.fieldCore);
			}
		}
		
		faceTarget();
	}
	
}