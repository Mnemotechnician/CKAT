package stingray.entities.behavior;

import arc.util.*;
import arc.math.*;
import mindustry.gen.*;
import mindustry.content.*;
import mindustry.entities.*;
import stingray.entities.*;

open class BiteBehavior(var damage: Float, var reload: Float, var angleMax: Float, var padding: Float, var maxHeal: Float) : BehaviorPattern() {
	
	var reloadTimer: Float = 0f;
	var healed: Float = 0f;
	
	val effect = Fx.generate;
	
	override open fun apply(parent: mindustry.gen.Unit) {
		reloadTimer += Time.delta;
		if (reloadTimer < reload) return;
		
		while (reloadTimer >= 0f) {
			reloadTimer -= reload;
			this.healed = 0f;
			
			Units.nearbyEnemies(parent.team, parent.x, parent.y, parent.hitSize / 2f + padding) {
				tryAttack(parent, it);
			};
			
			Units.nearbyBuildings(parent.x, parent.y, parent.hitSize / 2f + padding) {
				if (it.team != parent.team) {
					tryAttack(parent, it);
				}
			};
		}
	}
	
	open fun tryAttack(parent: mindustry.gen.Unit, enemy: Healthc) {
		val angle = parent.angleTo(enemy.x, enemy.y);
		val angleDist = Math.abs(angle - parent.rotation);
		
		if (angleDist < angleMax || 360f - angleDist < angleMax) {
			enemy.damage(damage);
			effect.at(enemy.x, enemy.y, angle);
			
			if (healed < maxHeal) {
				parent.heal(Math.min(damage / 5f, Math.max(maxHeal - healed, 0f)));
				healed += damage / 5f;
			}
			
			if (Mathf.chance(0.07f) && enemy is mindustry.gen.Unit) enemy.`apply`(StatusEffects.unmoving, 100f);
		}
	}
	
}