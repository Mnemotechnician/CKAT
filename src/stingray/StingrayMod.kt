package stingray;

import io.mnemotechnician.autoupdater.*;
import arc.*;
import arc.util.*;
import mindustry.game.*;
import mindustry.mod.*;
import stingray.content.*;

open class StingrayMod : Mod() {
	
	init {
		Events.on(EventType.ClientLoadEvent::class.java, {
			Updater.checkUpdates(this);
		});
	}

	override open fun loadContent() {
		StingrayFx().load();
		StingrayUnitTypes().load();
		StingrayTechTree().load();
	}
}
