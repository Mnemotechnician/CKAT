package stingray.entities;

import mindustry.gen.*;

/** Similar to Ability, but one instance of pattern is created per stingray unit */
abstract open class BehaviorPattern {
	
	open fun apply(parent: mindustry.gen.Unit) {
		
	}
	
	open fun draw(parent: mindustry.gen.Unit) {
		
	}
	
}