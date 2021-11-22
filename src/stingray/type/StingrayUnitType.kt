package stingray.type;

import arc.struct.*;
import arc.func.*;
import arc.scene.ui.layout.*;
import mindustry.game.*;
import mindustry.type.*;
import mindustry.world.meta.*;
import mindustry.ui.*;

import stingray.ai.types.*;
import stingray.entities.*;

open class StingrayUnitType(name: String) : UnitType(name) {

	lateinit var behavior: Seq<BehaviorPattern>;
	
	init {
		constructor = Prov { StingrayUnit() };
		defaultController = Prov { StingrayAI() };
	}
	
	override fun create(team: Team): mindustry.gen.unit {
		val unit = super.create(team);
		
		if (unit !is StingrayUnit) {
			Log.warn("endless suffering");
			return unit;
		}
		
		behavior.each {
			unit.behavior.add(it.copy());
		}
		
		return unit;
	}
	
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