package stingray.entities;

import arc.struct.*;
import arc.util.io.*;
import arc.scene.ui.layout.*;
import mindustry.gen.*;

/** 
 * Similar to Ability, except that it's handled by the Unit object, one instance of pattern is created per unit and it's stats can displayed in the database
 * All patterns should have a constructor accepting no arguments, in order to allow to restore it during save loading
 */
abstract open class BehaviorPattern(val name: String) {
	
	/** Called in the update function of the unit */
	open fun apply(parent: mindustry.gen.Unit) {
		
	}
	
	/** Called in the draw function of the unit */
	open fun draw(parent: mindustry.gen.Unit) {
		
	}
	
	/** Should display the ability in the core database */
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
	
	/** 
	 * Should provide a copy of the pattern
	 * *inhales*
	 * AAAAAAA
	 */
	abstract open fun copy(): BehaviorPattern;
	
}