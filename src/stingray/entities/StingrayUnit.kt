package stingray.entities;

import arc.struct.*;
import mindustry.gen.*;
import stingray.entities.*;

open class StingrayUnit(var behavior: Seq<BehaviorPattern>) : mindustry.gen.MechUnit() {
	
	override open fun update() {
		super.update();
		behavior.each {
			it.apply(this);
		};
	}
	
	override open fun draw() {
		super.draw();
		behavior.each {
			it.apply(this);
		}
	}
	
}