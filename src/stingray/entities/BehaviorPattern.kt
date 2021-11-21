package stingray.entities;

import arc.scene.ui.layout.*;
import mindustry.gen.*;

/** Similar to Ability, except it's handled by the Unit, one instance of pattern is created per stingray unit and it's stats are displayed */
abstract open class BehaviorPattern(val name: String) {
	
	open fun apply(parent: mindustry.gen.Unit) {
		
	}
	
	open fun draw(parent: mindustry.gen.Unit) {
		
	}
	
	open fun display(table: Table) {
		table.add("@ckat-stingray.ability.$name").fillY().center().marginRight(20);
	}
	
}