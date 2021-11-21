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
		if (type !is StingrayUnitType) throw Exception("not a stingray, get out of my land!");
		this.type.behavior.each {
			behavior.add(it);
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
		type.behavior.each {
			writes.i(it.version());
			it.write(writes);
		}
	}
	
	override fun read(reads: Reads) {
		super.read(reads);
		type.behavior.each {
			val version = reads.i();
			it.read(reads);
		}
	}
	
}