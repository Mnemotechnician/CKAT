package stingray.entities;

import kotlin.reflect.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.gen.*;
import stingray.entities.*;
import stingray.type.*;

open class StingrayUnit : mindustry.gen.MechUnit() {

	val behavior = Seq<BehaviorPattern>(5);

	init {
		if (!behavior.isEmpty()) {
			behavior.clear();
		}
		
		val type = this.type;
		if (type !is StingrayUnitType) {
			Log.warn("not a stingray, get out of my land!");
		} else {
			type.behavior.each {
				val copy = it.copy();
				behavior.add(copy);
			}
		}
	}
	
	override open fun update() {
		super.update();
		behavior.each {
			it.apply(this);
		};
	}
	
	override open fun draw() {
		super.draw();
		behavior.each {
			it.draw(this);
		}
	}
	
	override fun write(writes: Writes) {
		super.write(writes);
		behavior.each {
			writes.i(it.version());
			it.write(writes);
		}
	}
	
	override fun read(reads: Reads) {
		super.read(reads);
		behavior.each {
			val version = reads.i();
			it.read(reads, version);
		}
	}
	
}