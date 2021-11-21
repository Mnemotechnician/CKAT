package stingray.entities;

import kotlin.reflect.*;
import arc.struct.*;
import arc.util.*;
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
			it.draw(this);
		}
	}
	
	override open fun write(writes: Writes) {
		super.write(writes);
		Log.info("writing $this")
		writes.i(behavior.size);
		behavior.each {
			writes.i(it.version());
			writes.str(it.name);
			it.write(writes);
			Log.info("writing $it: revision ${it.version()}")
		};
	}
	
	override open fun read(reads: Reads) {
		super.read(reads);
		Log.info("reading $this");
		val size = reads.i();
		for (num in 0 until size) {
			val version = reads.i();
			val name = reads.str();
			val ptype = BehaviorPattern.patternMap.get(name);
			if (type == null) {
				Log.warn("Unknown behavior: $name, trying to skip");
				continue;
			}
			
			val instance = ptype.createInstance();
			if (instance !is BehaviorPattern) {
				Log.warn("${ptype.getCanonicalName()} is not a behavior pattern and cannot be read, trying to skip");
				continue;
			}
			
			Log.info("reading $instance: revision $version");
			instance.read(reads, version);
		};
	}
	
}