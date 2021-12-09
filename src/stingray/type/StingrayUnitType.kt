package stingray.type;

import arc.struct.*;
import arc.util.*;
import arc.func.*;
import arc.scene.ui.layout.*;
import mindustry.game.*;
import mindustry.type.*;
import mindustry.world.meta.*;
import mindustry.ui.*;
import mindustry.gen.*;

import stingray.ai.types.*;
import stingray.entities.*;

open class StingrayUnitType : UnitType {

	lateinit var behavior: Seq<BehaviorPattern>;
	
	constructor(name: String) : super(name) {
		constructor = EntityMapping.register(name, { StingrayUnit() })
		defaultController = Prov { StingrayAI() };
	}
	
	override fun create(team: Team): mindustry.gen.Unit {
		val unit = super.create(team);
		
		if (unit !is StingrayUnit) {
			Log.warn("only ckats are allowed to be gods");
			return unit;
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