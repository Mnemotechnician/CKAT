package stingray.content;

import arc.math.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import mindustry.game.*;
import mindustry.graphics.*;
import mindustry.ctype.*;
import mindustry.entities.*;

open class StingrayFx : ContentList {
	
	override open fun load() {
		moveArrow = Effect(120f) {
			val size = 9f * it.fin();
			
			Draw.color(Pal.accent);
			Draw.alpha(it.fout() * 15f);
			Lines.stroke(2f);
			Lines.lineAngle(it.x, it.y, it.rotation + 225f, size);
			Lines.lineAngle(it.x, it.y, it.rotation + 135f, size);
			Draw.color();
		};
		
		hurricaneSpawn = Effect(10f) {
			val size = 20 * it.fslope();
			val ox = Angles.trnsx(it.rotation, 20f);
			val oy = Angles.trnsy(it.rotation, 20f);
			
			Draw.color(Pal.spore);
			Draw.alpha(it.fout() * 4f);
			Lines.stroke(3f);
			Lines.lineAngle(it.x - ox, it.y - oy, it.rotation - 15f, size);
			Lines.lineAngle(it.x - ox, it.y - oy, it.rotation, size);
			Lines.lineAngle(it.x - ox, it.y - oy, it.rotation + 15f, size);
			
			if (it.`data`() is Team) {
				Drawf.light(it.`data`(), it.x, it.y, 16f, Pal.reactorPurple, 0.5f);
			} else {
				Drawf.light(it.x, it.y, 16f, Pal.reactorPurple, 0.5f);
			}
		};
	}
	
	companion object {
		lateinit var moveArrow: Effect;
		lateinit var hurricaneSpawn: Effect;
	}
	
}