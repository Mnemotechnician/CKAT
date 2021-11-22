package stingray.type;

import arc.struct.*;
import arc.scene.ui.layout.*;
import mindustry.type.*;
import mindustry.world.meta.*;
import mindustry.ui.*;

import stingray.entities.*;

open class StingrayUnitType(name: String) : UnitType(name) {

	lateinit var behavior: Seq<BehaviorPattern>;
	
	override fun setStats() {
		super.setStats();
		
		stats.add(Stat.abilities, StatValue {
			it.row();
			it.table { table: Table ->
				table.center().left();
				behavior.each { behavior: BehaviorPattern ->
					table.table(Styles.flatDown) {
						it.center().left();
						behavior.display(it);
					}.growX().row();
				}
			}.left().growX();
		});
	}
	
}