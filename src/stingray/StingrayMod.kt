package stingray;

import arc.*
import arc.util.*
import mindustry.game.EventType.*
import mindustry.mod.*
import stingray.content.*;

open class StingrayMod : Mod() {
	
	init {
		
	}

	override open fun loadContent() {
		StingrayUnits.load();
		StingrayTechTree.load();
		StingrayFX.load();
	}
}
