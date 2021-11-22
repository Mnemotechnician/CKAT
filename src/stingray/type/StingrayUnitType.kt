package stingray.type;

import arc.struct.*;
import arc.scene.ui.layout.*;
import mindustry.type.*;
import mindustry.world.meta.*;
import mindustry.ui.*;

import stingray.entities.*;

open class StingrayUnitType(name: String) : UnitType(name) {

	lateinit var behavior: Seq<BehaviorPattern>(5);
	
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
					table.table(Styles.flatDown) {
						it.center().left();
						behavior.display(it);
					}.growX().row();
				}
			}.left().growX();
		});
	}
	
}