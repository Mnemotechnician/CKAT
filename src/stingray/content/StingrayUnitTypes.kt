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

open class StingrayUnitTypes : ContentList {

	override open fun load() {
		urotry = object : UnitType("urotry") {}.apply {
			speed = 1.1f;
			
			hitSize = 27f;
			canDrown = false;
			health = 400f;
			buildSpeed = 0;
			armor = 0;
			
			legLength = 0;
			legCount = 0;
			mechStride = 0;
			mechStepShake = 0;
			
			immunities = ObjectSet.with(StatusEffects.wet);
		}
		urotry.constructor = {StingrayUnit(Seq.with(
			BiteBehavior(25f, 60f / 3f, 270f, 10f, 30f),
			SwimBehavior(1.5f)
		))};
		urotry.defaultController = StingrayAI();
		
		mylio = object : UnitType("mylio") {}.apply {
			speed = 0.5f;
			hitSize = 37f;
			
			canDrown = false;
			health = 650f;
			buildSpeed = 0;
			armor = 8;
			
			legLength = 0;
			legCount = 0;
			mechStride = 0;
			mechStepShake = 0;
			
			immunities = ObjectSet.with(StatusEffects.wet);
		}
		mylio.constructor = {StingrayUnit(Seq.with(
			BiteBehavior(50f, 60f / 4f, 30f, 40f, 50f),
			SwimBehavior(2.2f),
			DashBehavior(500f, 60f * 10f, 30f, 8 * 30f)
		))};
		mylio.defaultController = StingrayAI();
		
		undulate = object : UnitType("undulate") {}.apply {
			speed = 1.2f,
			hitSize = 64f;
		
			canDrown = false;
			health = 1300f;
			buildSpeed = 0;
			armor = 12;
			
			legLength = 0;
			legCount = 0;
			mechStride = 0;
			mechStepShake = 0;
			
			immunities = ObjectSet.with(StatusEffects.wet); 
		}
		undulate.constructor = {StingrayUnit(Seq.with(
			BiteBehavior(28f, 60 / 8f, 95f, 32f, 120f),
			SwimBehavior(1.35f)	
		))};
		undulate.defaultController = StingrayAI();
			
			
		dasya = object : UnitType("dasya") {}.apply {
			speed = 0.9f;
			
			hitSize = 96f;
			canBoost = false;
			canDrown = false;
			health = 9100f;
			buildSpeed = 0;
			armor = 24;
			
			legLength = 0;
			legCount = 0;
			mechStride = 0;
			mechStepShake = 0;
			
			immunities = ObjectSet.with(StatusEffects.wet, StatusEffects.freezing);
			
			weapons = Seq.with(object : Weapon("hurricane-generator") {}.apply {
				top = true;
				x = 0f;
				y = 32f;
				reload = 90f;
				recoil = 8;
				shake = 1
				ejectEffect = StingrayFx.hurricaneSpawn;
				
				bullet = object : BulletType() {
					init {
						collidesAir = collidesGround = false;
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
						
						Units.nearbyEnemies(bullet.owner.team, bullet.x, bullet.y, suckRadius) {
							val power = (power / Math.cbrt(bullet.dst2(it) * it.hitSize) * bullet.fout()) as Float;
							val angle = it.angleTo(bullet); 
							
							it.vel.x += Angles.trnsx(angle, power * Time.delta);
							it.vel.y += Angles.trnsy(angle, power * Time.delta);
							
							it.damage(power * 0.01f * Time.delta);
						};
					}
					
					override fun draw(bullet: Bullet) {
						val progress = Interp.sineOut.apply(bullet.fout());
						Draw.alpha((b.fin() - 0.0125f) * 40f); /*5 + 10 ticks until full visibility*/
						Draw.rect(region, bullet.x, bullet.y, visualSize * progress, visualSize * progress, progress * 2880);
					}
					
					//no idea, this is present in the original code
					override fun continuousDamage() {
						return power * 0.01f * 60f;
					}
				}
			});
		}
		dasya.constructor = {StingrayUnit(Seq.with(
			BiteBehavior(100f, 60 / 5f, 140f, 32f, 350f),
			SwimBehavior(1.3f),
			DashBehavior(2300f, 60 * 15f, 40f, 8 * 40f)
		))};
		dasya.defaultController = StingrayAI();
	}

	//what the hell
	companion object {
		lateinit var urotry: UnitType;
		lateinit var mylio: UnitType;
		lateinit var undulate: UnitType;
		lateinit var dasya: UnitType;
	}
	
}