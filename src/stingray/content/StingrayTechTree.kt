package stingray.content;

import mindustry.*;
import mindustry.type.*;
import mindustry.ctype.*;
import mindustry.content.*;
import stingray.content.*;

open class StingrayTechTree : ContentList {
	
	override fun load() {
		unitTech(StingrayUnitTypes.Urotry, UnitTypes.dagger);
		unitTech(StingrayUnitTypes.Mylio, StingrayUnitTypes.Urotry);
		unitTech(StingrayUnitTypes.Undulate, StingrayUnitTypes.Mylio);
		unitTech(StingrayUnitTypes.Dasya, StingrayUnitTypes.Undulate);
	}
	
	inline fun unitTech(unit: UnitType, parent: UnitType) {
		TechTree.TechNode(TechTree.get(parent), unit, unit.researchRequirements());
	};
	
}