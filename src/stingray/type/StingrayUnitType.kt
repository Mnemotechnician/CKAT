package stingray.type;

import mindustry.type.*;
import mindustry.world.meta.*;
import mindustry.ui.*;

import stingray.entities.*;

/** Only used for custom stat display */
class StingrayUnitType(name: String) : UnitType(name) {
	
	override fun setStats() {
		super.setStats();
		
		//i can't think of a better way
		val example = create(null);
		if (example !is StingrayUnit) return;
		
		stats.add(Stat.abilities) StatValue {
			it.table(Styles.black3) { table: Table ->
				example.behavior.each { behavior: BehaviorPattern ->
					table.table {
						behavior.display(it);
					}.marginBottom(10).row();
				}
			}.growX();
		};
	}
	
}