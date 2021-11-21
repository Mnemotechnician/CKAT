package stingray.entities;

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
		Log.write("writing $this")
		behavior.each {
			writes.i(it.version());
			it.write(writes);
			Log.info("writing $it: revision ${it.version()}")
		};
	}
	
	override open fun read(reads: Reads) {
		super.read(reads);
		Log.info("reading $this");
		behavior.each {
			val version = reads.i();
			it.read(reads, version);
			Log.info("reading $it: revision $version")
		};
	}
	
}