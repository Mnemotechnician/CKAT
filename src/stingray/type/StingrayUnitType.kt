package stingray.type;

import arc.scene.ui.layout.*;
import mindustry.type.*;
import mindustry.world.meta.*;
import mindustry.ui.*;

import stingray.entities.*;

/** Only used for custom stat display */
open class StingrayUnitType(name: String) : UnitType(name) {
	
	override fun setStats() {
		super.setStats();
		
		//i can't think of a better way
		val example = create(null);
		if (example !is StingrayUnit) return;
		
		stats.add(Stat.abilities, StatValue {
			it.row();
			it.table { table: Table ->
				table.center().left();
				example.behavior.each { behavior: BehaviorPattern ->
					behavior.display(table);
					table.row();
				}
			}.left().growX();
		});
	}
	
}