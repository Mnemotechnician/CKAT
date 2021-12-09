package stingray.entities;

import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.gen.*;

import stingray.entities.*;
import stingray.type.*;

open class StingrayUnit : mindustry.gen.MechUnit() {

	val behavior = Seq<BehaviorPattern>(5);
	
	private var initialized = false;
	
	override open fun update() {
		super.update();
		
		if (!initialized) {
			initialize();
			initialized = true;
		}
		
		behavior.each {
			it.apply(this);
		};
	}
	
	/** Copies the behavior of it's UnitType in it's default implementation */
	open fun initialize() {
		if (type is StingrayUnitType) {
			type.behavior.each {
				behavior.add(it.copy());
			}
		}
	}
	
	override open fun draw() {
		super.draw();
		behavior.each {
			it.draw(this);
		}
	}
	
	override fun classId(): Int {
		return mappingId
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
	
	companion object {
		val mappingId = EntityMapping.register("ckat-stingray", ::StingrayUnit)
	}
	
}