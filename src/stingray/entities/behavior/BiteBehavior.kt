package stingray.entities.behavior;

import arc.util.*;
import mindustry.gen.*;
import mindustry.content.*;
import mindustry.entities.*;
import stingray.entities.*;

open class BiteBehavior(var damage: Float, var reload: Float, var angleMax: Float, var padding: Float, var maxHeal: Float) : BehaviorPattern() {
	
	var reloadTimer: Float = 0;
	var healed: Float = 0;
	
	val effect = Fx.generate;
	
	override fun apply(parent: mindustry.gen.Unit) {
		if ((reloadTimer += Time.delta) < reload) return;
		
		while ((reloadTimer -= reload) >= 0) {
			this.healed = 0;
			
			Units.nearbyEnemies(parent.team, parent.x, parent.y, parent.hitSize / 2 + padding) {
				attackIfInfront(parent, it);
			};
			
			Units.nearbyBuildings(parent.x, parent.y, parent.hitSize / 2 + padding) {
				if (it.team != parent.team) {
					tryAttack(parent, it);
				}
			};
		}
	}
	
	fun tryAttack(parent: mindustry.gen.Unit, enemy: Healthc) {
		val angle = parent.angleTo(enemy.x, enemy.y);
		val angleDist = Math.abs(angle - parent.rotation);
		
		if (angleDist < angleMax || 360 - angleDist < angleMax) {
			enemy.damage(damage);
			effect.at(enemy.x, enemy.y, angle);
			
			if (healed < maxHeal) {
				parent.heal(Math.min(damage / 5f, Math.max(maxHeal - healed, 0)));
				healed += damage / 5f;
			}
			
			if (Mathf.chance(0.07f) && enemy instanceof mindustry.gen.Unit) enemy.apply(StatusEffects.unmoving, 100);
		}
	}
	
}