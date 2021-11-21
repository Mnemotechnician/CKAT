package stingray.entities.behavior;

import arc.util.*;
import arc.math.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.content.*;
import mindustry.entities.*;

import stingray.content.*;
import stingray.entities.*;

open class DashBehavior(var damage: Float, var reload: Float, var angleMax: Float, var range: Float) : BehaviorPattern() {
	
	var target: mindustry.gen.Unit? = null;
	var dashCharge: Float = 0f;
	var dashTimer: Float = 0f;
	var effectTimer: Float = 0f;
	
	var speed: Float = 2f;
	
	override open fun apply(parent: mindustry.gen.Unit) {
		if (dashTimer <= 0f) {
			dashCharge += Time.delta
			if (dashCharge >= reload) {
				dashCharge = 0f;
				dashTimer = range / speed;
			}
		} else {
			//actual dash process
			var angle: Float = 0f;
			val target = Units.closestEnemy(parent.team, parent.x, parent.y, range + parent.hitSize) {
				angle = parent.angleTo(it.x, it.y);
				val angleDist = Math.abs(angle - parent.rotation);
				(angleDist < angleMax || 360f - angleDist < angleMax) && it.maxHealth > damage / 4f;
			};
			
			if (target == null) return;
			this.target = target;
			dashTimer -= Time.delta;
			
			if (parent.dst(target) <= parent.hitSize + target.hitSize) {
				val pushForce = (parent.hitSize * parent.hitSize) / (target.hitSize * target.hitSize);
				
				target.vel.x += Angles.trnsx(angle, pushForce);
				target.vel.y += Angles.trnsy(angle, pushForce);
				target.damage(damage);
				
				dashTimer = 0f;
				this.target = null;
			} else {
				parent.vel.x += Angles.trnsx(angle, speed * Time.delta);
				parent.vel.y += Angles.trnsy(angle, speed * Time.delta);
			}
		}
	}
	
	override fun draw(parent: mindustry.gen.Unit) {
		val target = this.target; //what the fuck
		if (target == null || Units.invalidateTarget(target, parent.team, parent.x, parent.y)) return;
		
		Draw.color(Pal.accent);
		//idk what all these magic values mean and and this point I don't want to know
		Lines.circle(target.x, target.y, (target.hitSize * 1.5f) / Math.min(parent.dst(target) / 80f, 1.5f));
		
		effectTimer += Time.delta;
		if (effectTimer > 8f) {
			effectTimer = 0f;
			StingrayFx.moveArrow.at(parent.x, parent.y, parent.rotation);
		}
	}
	
}