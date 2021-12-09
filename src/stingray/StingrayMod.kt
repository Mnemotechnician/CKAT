package stingray;

import io.mnemotechnician.autoupdater.*;
import arc.*;
import arc.util.*;
import mindustry.gen.*;
import mindustry.game.*;
import mindustry.mod.*;
import mindustry.type.*;
import mindustry.content.*;

import stingray.content.*;

open class StingrayMod : Mod() {
	
	init {
		Events.on(EventType.ClientLoadEvent::class.java) {
			Updater.checkUpdates(this);
		};
	}

	override open fun loadContent() {
		StingrayFx().load();
		StingrayUnitTypes().load();
		StingrayTechTree().load();
		
		//make it possible to produce stingrays via a payload source
		Blocks.payloadSource.config(StingrayUnitType::class.java) { build: Building, unit: StingrayUnitType ->
			build.unit = unit;
			build.block = null;
			build.payload = null;
			build.scl = 0f
		}
	}
}
