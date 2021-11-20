package stingray;

import arc.*
import arc.util.*
import mindustry.game.EventType.*
import mindustry.mod.*
import stingray.content.*;

class StingrayMod : Mod() {
	
	init {
		
	}

	override fun loadContent() {
		StingrayUnits.load();
		StingrayTechTree.load();
		StingrayFX.load();
	}
}
