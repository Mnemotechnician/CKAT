package stingray.entities;

import arc.struct.*;
import arc.util.io.*;
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
	
	override open fun write(writes: Writes) {
		super.write(writes);
		behavior.each {
			writes.i(it.version());
			it.write(writes);
		};
	}
	
	override open fun read(reads: Reads) {
		super.read(reads);
		behavior.each {
			Int version = reads.i();
			it.read(reads, version);
		};
	}
	
}