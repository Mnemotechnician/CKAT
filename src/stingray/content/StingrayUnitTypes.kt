package stingray.content;

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

import stingray.*;
import stingray.content.*;
import stingray.entities.*;
import stingray.entities.behavior.*;
import stingray.ai.types.*;
import stingray.type.*;

open class StingrayUnitTypes : ContentList {

	override open fun load() {
		urotry = object : StingrayUnitType("urotry") {}.apply {
			speed = 1.1f;
			
			hitSize = 27f;
			canDrown = false;
			health = 400f;
			buildSpeed = 0f;
			armor = 0f;
			
			legLength = 0f;
			legCount = 0;
			mechStride = 0f;
			mechStepShake = 0f;
			
			immunities = ObjectSet.with(StatusEffects.wet);
			
			behavior = Seq.with(
				BiteBehavior(25f, 60f / 3f, 135f, 10f, 10f),
				SwimBehavior(1.5f)
			);
		}
		urotry.constructor = Prov {StingrayUnit()};
		urotry.defaultController = Prov { StingrayAI() };
		
		mylio = object : StingrayUnitType("mylio") {}.apply {
			speed = 0.5f;
			hitSize = 37f;
			
			canDrown = false;
			health = 650f;
			buildSpeed = 0f;
			armor = 8f;
			
			legLength = 0f;
			legCount = 0;
			mechStride = 0f;
			mechStepShake = 0f;
			
			immunities = ObjectSet.with(StatusEffects.wet);
			
			behavior = Seq.with(
				BiteBehavior(50f, 60f / 4f, 15f, 40f, 13f),
				SwimBehavior(2.2f),
				DashBehavior(500f, 10f * 60f, 15f, 8 * 30f)
			);
		}
		mylio.constructor = Prov { StingrayUnit() };
		mylio.defaultController = Prov { StingrayAI() };
		
		undulate = object : StingrayUnitType("undulate") {}.apply {
			speed = 1.2f;
			hitSize = 64f;
		
			canDrown = false;
			health = 1300f;
			buildSpeed = 0f;
			armor = 12f;
			
			legLength = 0f;
			legCount = 0;
			mechStride = 0f;
			mechStepShake = 0f;
			
			immunities = ObjectSet.with(StatusEffects.wet); 
			
			behavior = Seq.with(
				BiteBehavior(38f, 60 / 8f, 47f, 32f, 15f),
				SwimBehavior(1.35f)	
			);
		}
		undulate.constructor = Prov { StingrayUnit() };
		undulate.defaultController = Prov { StingrayAI() };
			
			
		dasya = object : StingrayUnitType("dasya") {}.apply {
			speed = 0.9f;
			
			hitSize = 96f;
			canBoost = false;
			canDrown = false;
			health = 9100f;
			buildSpeed = 0f;
			armor = 24f;
			
			legLength = 0f;
			legCount = 0;
			mechStride = 0f;
			mechStepShake = 0f;
			
			immunities = ObjectSet.with(StatusEffects.wet, StatusEffects.freezing);
			
			behavior = Seq.with(
				BiteBehavior(75f, 60 / 5f, 70f, 32f, 60f),
				SwimBehavior(1.3f),
				DashBehavior(2300f, 15f * 60f, 20f, 8 * 40f)
			);
			
			weapons = Seq.with(object : Weapon("hurricane-generator") {}.apply {
				top = true;
				x = 0f;
				y = 32f;
				reload = 90f;
				recoil = 8f;
				shake = 1f;
				ejectEffect = StingrayFx.hurricaneSpawn;
				
				bullet = object : BulletType() {
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
			});
		}
		dasya.constructor = Prov { StingrayUnit() };
		dasya.defaultController = Prov { StingrayAI() };
	}

	//what the hell
	companion object {
		lateinit var urotry: StingrayUnitType;
		lateinit var mylio: StingrayUnitType;
		lateinit var undulate: StingrayUnitType;
		lateinit var dasya: StingrayUnitType;
	}
	
}