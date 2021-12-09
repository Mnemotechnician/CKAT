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
		val type = type; //immutability
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
		
		val type = type;
		if (type !is StingrayUnitType) {
			Log.warn("not a stingray")
			return;
		}
		
		type.behavior.each {
			val version = reads.i();
			val b = it.copy();
			b.read(reads, version);
			this.behavior.add(b);
		}
		initialized = true;
	}
	
	override fun toString(): String {
		return "This is a tale of a mighty ckat, ${type.name} — a descendant of ${this::class.java}"
	}
	
	companion object {
		val mappingId = EntityMapping.register("ckat-stingray", ::StingrayUnit)
	}
	
}