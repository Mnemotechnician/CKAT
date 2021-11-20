package stingray.entities;

import arc.struct.*;
import mindustry.gen.*;
import stingray.entities.*;

open class StingrayUnit(var behavior: Seq<BehaviorPattern>) : mindustry.gen.Unit() {
	
	override fun update() {
		behavior.each {
			it.apply(this);
		};
		
		super.update();
	}
	
}