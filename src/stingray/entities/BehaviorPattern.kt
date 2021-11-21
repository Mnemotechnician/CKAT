package stingray.entities;

import arc.struct.*;
import arc.util.io.*;
import arc.scene.ui.layout.*;
import mindustry.gen.*;

/** Similar to Ability, except that it's handled by the Unit object, one instance of pattern is created per unit and it's stats can displayed in the database */
abstract open class BehaviorPattern(val name: String) {

	init {
		//todo: I'm accessing the map every time a new unit object is created. could i move that to unit types or somewhere else?
		BehaviorPattern.patternMap.put(name, this::class);
	}
	
	open fun apply(parent: mindustry.gen.Unit) {
		
	}
	
	open fun draw(parent: mindustry.gen.Unit) {
		
	}
	
	open fun display(table: Table) {
		table.add("@ckat-stingray.ability.$name").fillY().center().pad(5f).marginRight(15f);
	}
	
	open fun version(): Int {
		return 1;
	}
	
	open fun write(writes: Writes) {
		
	}
	
	open fun read(reads: Reads, revision: Int) {
		
	}
	
	companion object {
		
		val patternMap = ObjectMap<String, Class<BehaviorPattern>>(5);
		
	}
	
}