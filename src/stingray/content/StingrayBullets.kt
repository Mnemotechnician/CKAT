package stingray.content


import arc.*;
import arc.util.*;
import arc.math.*;
import arc.func.*;
import arc.struct.*;
import arc.graphics.g2d.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.ctype.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.bullet.*;

object StingrayBullets : ContentList {
	
	lateinit var hurricane: BulletType
	
	override fun load() {
		hurricane = object : BulletType() {
			init {
				collidesAir = false;
				collidesGround = false;
				collideTerrain = true;
				lifetime = 400f;
				homingPower = 0.02f;
				shootEffect = Fx.none;
				despawnEffect = Fx.none;
			}
			
			lateinit var region: TextureRegion;
			val suckRadius: Float = 90f;
			val power: Float = 15f;
			val visualSize: Float = 96f;
			
			
			override fun load() {
				region = Core.atlas.find("ckat-stingray-hurricane");
			}
				
			override fun update(bullet: Bullet) {
				super.update(bullet);
				
				Units.nearbyEnemies(bullet.team, bullet.x, bullet.y, suckRadius) {
					val power = power / Math.cbrt((bullet.dst2(it) * it.hitSize).toDouble()).toFloat() * bullet.fout();
					val angle = it.angleTo(bullet);
					
					it.vel.x += Angles.trnsx(angle, power * Time.delta);
					it.vel.y += Angles.trnsy(angle, power * Time.delta);
					
					it.damage(power * 0.01f * Time.delta);
				};
			}
			
			override fun draw(bullet: Bullet) {
				val progress = Interp.sineOut.apply(bullet.fout());
				Draw.alpha((bullet.fin() - 0.0125f) * 40f); /*5 + 10 ticks until full visibility*/
				Draw.rect(region, bullet.x, bullet.y, visualSize * progress, visualSize * progress, Interp.sineIn.apply(progress) * 2880);
			}
			
			//no idea, this is present in the original code
			override fun continuousDamage(): Float {
				return power * 0.01f * 60f;
			}
		}
	}
}