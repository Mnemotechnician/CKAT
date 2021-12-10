package stingray;

import io.mnemotechnician.autoupdater.*;
import arc.*;
import arc.util.*;
import mindustry.gen.*;
import mindustry.game.*;
import mindustry.mod.*;
import mindustry.type.*;
import mindustry.content.*;
import mindustry.world.blocks.payloads.*;

import stingray.type.*;
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
			if (build is PayloadSource.PayloadSourceBuild && Blocks.payloadSource.canProduce(unit) && unit != build.unit) {
				build.unit = unit;
				//build.block = null; todo: what the fuck anuken
				build.payload = null;
				build.scl = 0f;
			}
		}
	}
}
