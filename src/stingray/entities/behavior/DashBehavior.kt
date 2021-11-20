package stingray.entities.behavior;

import arc.util.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.content.*;
import mindustry.entities.*;

import stingray.content.*;
import stingray.entities.*;

open class BiteBehavior(var damage: Float, var reload: Float, var angleMax: Float, var range: Float) : BehaviorPattern() {
	
	var target: mindustry.gen.Unit? = null;
	var dashCharge: Float = 0;
	var dashTimer: Float = 0;
	var effectTimer: Float = 0;
	
	val speed: Float = 2;
	
	override fun apply(parent: mindustry.gen.Unit) {
		if (dashTimer <= 0) {
			if ((dashCharge += Time.delta) >= reload) {
				dashCharge = 0;
				dashTimer = range / speed / 60f;
			}
		} else {
			//actual dash process
			target = Units.closestTarget(parent.team, parent.x, parent.y, range + parent.hitSize) {
				val angle = parent.angleTo(it.x, it.y);
				val angleDist = Math.abs(angle - parent.rotation);
				return (angleDist < angleMax || 360 - angleDist < angleMax) && it.maxHealth > damage / 4;
			};
			
			if (target == null) return;
			
			dashTimer -= Time.delta;
			if (parent.dst(target) <= parent.hitSize + target.hitSize) {
				val pushForce = (parent.hitSize * parent.hitSize) / (target.hitSize * target.hitSize);
				
				target.vel.x += Angles.trnsx(angle, pushForce);
				target.vel.y += Angles.trnsy(angle, pushForce);
				target.damage(damage);
				
				dashTimer = 0;
			} else {
				parent.vel.x += Angles.trnsx(angle, speed * Time.delta);
				parent.vel.y += Angles.trnsy(angle, speed * Time.delta);
			}
		}
	}
	
	override fun draw(parent: mindustry.gen.Unit) {
		if (target == null || Units.invalidateTarget(target, parent.team, parent.x, parent.y)) return;
		
		Draw.color(Pal.accent);
		//idk what all these magic values mean and and this point I don't want to know
		Lines.circle(target.x, target.y, (target.hitSize * 1.5) / Math.min(parent.dst(target) / 80, 1.5));
		
		if ((effectTimer += Time.delta) > 8) {
			effectTimer = 0;
			StingrayFx.moveArrow.at(parent.x, parent.y, parent.rotation);
		}
	}
	
}