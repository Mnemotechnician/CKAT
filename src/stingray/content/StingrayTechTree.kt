package stingray.content;

import mindustry.*;
import mindustry.type.*;
import mindustry.ctype.*;
import mindustry.content.*;

import stingray.content.*;

open class StingrayTechTree : ContentList {
	
	override open fun load() {
		unitTech(StingrayUnitTypes.urotry, UnitTypes.dagger);
		unitTech(StingrayUnitTypes.mylio, StingrayUnitTypes.urotry);
		unitTech(StingrayUnitTypes.undulate, StingrayUnitTypes.mylio);
		unitTech(StingrayUnitTypes.dasya, StingrayUnitTypes.undulate);
	}
	
	inline fun unitTech(unit: UnitType, parent: UnitType) {
		TechTree.TechNode(TechTree.get(parent), unit, unit.researchRequirements());
	};
	
}