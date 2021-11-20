package stingray.content;

import arc.graphics.*;
import arc.graphics.g2d.*;
import mindustry.graphics.*;
import mindustry.ctype.*;
import mindustry.entities.*;

open class StingrayFX : ContentList {
	
	override fun load() {
		moveArrow = Effect(120) {
			val size = 9 * it.fin();
			
			Draw.color(Pal.accent);
			Draw.alpha(it.fout() * 15);
			Lines.stroke(2);
			Lines.lineAngle(it.x, it.y, it.rotation + 225, size);
			Lines.lineAngle(it.x, it.y, it.rotation + 135, size);
			Draw.color();
		};
		
		hurricaneSpawn = Effect(10) {
			val size = 20 * it.fslope();
			val ox = Angles.trnsx(it.rotation, 20), oy = Angles.trnsy(it.rotation, 20);
			
			Draw.color(Pal.spore);
			Draw.alpha(it.fout() * 4);
			Lines.stroke(3);
			Lines.lineAngle(it.x - ox, it.y - oy, it.rotation - 15, size);
			Lines.lineAngle(it.x - ox, it.y - oy, it.rotation, size);
			Lines.lineAngle(it.x - ox, it.y - oy, it.rotation + 15, size);
			
			if (it.data() instanceof Team) {
				Drawf.light(it.data(), it.x, it.y, 16, Pal.reactorPurple, 0.5f);
			} else {
				Drawf.light(it.x, it.y, 16, Pal.reactorPurple, 0.5f);
			}
		};
	}
	
	companion object {
		lateinit var moveArrow: Effect;
		lateinit var hurricaneSpawn: Effect;
	}
	
}