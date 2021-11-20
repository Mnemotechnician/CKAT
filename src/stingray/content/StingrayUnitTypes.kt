package stingray.content;

import arc.*;
import arc.math.*;
import arc.struct.*;
import arc.graphics.g2d.*;
import mindustry.*;
import mindustry.type.*;
import mindustry.ctype.*;

import stingray.*;
import stingray.content.*;
import stingray.entities.*;
import stingray.entities.behavior.*;
import stingray.ai.types.*;

open class StingrayUnits : ContentList {

	override fun load() {
		urotry = object : UnitType() {}.with {
			speed = 1.1;
			
			hitSize = 27;
			canDrown = false;
			health = 400;
			buildSpeed = 0;
			armor = 0;
				
			legLength = 0;
			legCount = 0;
			mechStride = 0;
			mechStepShake = 0;
			
			immunities: ObjectSet.with(StatusEffects.wet);
		}
		urotry.constructor = {StingrayUnit(Seq.with(
			BiteBehavior(25, 60 / 3, 270, 10, 30),
			SwimBehavior(1.5f)
		))};
		urotry.defaultController = StingrayAI;
		
		mylio = object : UnitType() {}.with {
			speed = 0.5;
			hitSize = 37;
			
			canDrown = false;
			health = 650;
			buildSpeed = 0;
			armor = 8;
			
			legLength = 0;
			legCount = 0;
			mechStride = 0;
			mechStepShake = 0;
			
			immunities: ObjectSet.with(StatusEffects.wet);
		}
		mylio.constructor = {StingrayUnit(Seq.with(
			BiteBehavior(50, 60 / 4, 30, 40, 50),
			SwimBehavior(2.2f),
			DashBehavior(500, 60 * 10, 30, 8 * 30)
		))};
		mylio.defaultController = StingrayAI;
		
		undulate = object : UnitType() {}.with {
			speed: 1.2,
			hitSize = 64;
		
			canDrown = false;
			health = 1300;
			buildSpeed = 0;
			armor = 12;
			
			legLength = 0;
			legCount = 0;
			mechStride = 0;
			mechStepShake = 0;
			
			immunities = ObjectSet.with(StatusEffects.wet); 
		}
		undulate.constructor = {StingrayUnit(Seq.with(
			BiteBehavior(28, 60 / 8, 95, 32, 120),
			SwimBehavior(1.35)	
		))};
		undulate.defaultController = StingrayAI;
			
			
		dasya = object : UnitType() {}.with {
			speed = 0.9;
			
			hitSize = 96;
			canBoost = false;
			canDrown = false;
			health = 9100;
			buildSpeed = 0;
			armor = 24;
			
			legLength = 0;
			legCount = 0;
			mechStride = 0;
			mechStepShake = 0;
			
			immunities = ObjectSet.with(StatusEffects.wet, StatusEffects.freezing);
			
			weapons = Seq.with(object : Weapon("hurricane-generator") {}.with {
				top = true;
				x = 0;
				y = 32;
				reload = 90;
				recoil = 8;
				shake = 1
				ejectEffect = StingrayFx.hurricaneSpawn;
				
				bullet = object : BulletType() {
					init {
						collidesAir = collidesGround = false;
						collideTerrain = true;
						lifetime = 400;
						homingPower = 0.02;
						shootEffect = Fx.none;
						despawnEffect = Fx.none;
					}
					
					lateinit var region: TextureRegion;
					val suckRadius: Float = 90;
					val power: Float = 15;
					val visualSize: Float = 96;
					
					
					override fun load() {
						region = Core.atlas.find("ckat-stingray-hurricane");
					}
					
					override fun update(bullet: Bullet) {
						super.update(bullet);
						
						Units.nearbyEnemies(bullet.owner.team, bullet.x, bullet.y, suckRadius) {
							val power = (power / Math.cbrt(bullet.dst2(it) * it.hitSize) * bullet.fout()) as Float;
							val angle = it.angleTo(bullet); 
							
							it.vel.x += Angles.trnsx(angle, power * Time.delta);
							it.vel.y += Angles.trnsy(angle, power * Time.delta);
							
							it.damage(power * 0.01 * Time.delta);
						});
					}
					
					override fun draw(bullet: Bullet) {
						val progress = Interp.sineOut.apply(bullet.fout());
						Draw.alpha((b.fin() - 0.0125) * 40); /*5 + 10 ticks until full visibility*/
						Draw.rect(region, bullet.x, bullet.y, visualSize * progress, visualSize * progress, progress * 2880);
					}
					
					//no idea, this is present in the original code
					override fun continuousDamage() {
						return this.power * 0.01 * 60;
					}
				}
			});
		}
		dasya.constructor = {StingrayUnit(Seq.with(
			BiteBehavior(100, 60 / 5f, 140, 32, 350),
			SwimBehavior(1.3f),
			DashBehavior(2300, 60 * 15, 40, 8 * 40)
		))};
		dasya.defaultController = StingrayAI;
	}

	//what the hell
	companion object {
		lateinit val urotry: UnitType;
		lateinit val mylio: UnitType;
		lateinit val undulate: UnitType;
		lateinit val dasya: UnitType;
	}
	
}