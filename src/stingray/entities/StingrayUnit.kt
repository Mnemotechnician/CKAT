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
		
		writes.i(behavior.size)
		behavior.each {
			writes.i(it.version());
			writes.str(it::class.java.name); //yes, this will for sure impact the file size, but this is the most failsafe method
			it.write(writes);
		}
	}
	
	override fun read(reads: Reads) {
		super.read(reads);
		Log.info("reading $this")
		
		val type = type;
		if (type !is StingrayUnitType) {
			Log.warn("not a stingray")
			return;
		}
		
		Log.info("type behavior: ${type.behavior}")
		
		val size = reads.i();
		repeat(size) {
			val version = reads.i();
			val name = reads.str();
			try {
				val clazz = java.lang.Class.forName(name);
				//try to find such ability in the type
				var example: BehaviorPattern? = type.behavior.find { it::class.java == clazz }
				if (example == null) {
					//no such ability, try to create one
					example = clazz.newInstance();
				}
				
				val b = example.copy()
				b.read(reads, version);
				this.behavior.add(b);
			} catch (e: java.lang.ClassNotFoundException) {
				Log.err("unknown behavior class: $name. The save has probably been corrupted.", e)
			} catch (e: java.lang.InstantiationException) {
				Log.err("ability $name is not found in it's parent and it doesn't have an argumentless constructor", e)
			}
		}
		initialized = true;
	}
	
	override fun toString(): String {
		return "[blue]This is a tale of a mighty ckat — ${type.name} — a descendant of ${this::class.java}[]"
	}
	
	companion object {
		val mappingId = EntityMapping.register("ckat-stingray", ::StingrayUnit)
	}
	
}