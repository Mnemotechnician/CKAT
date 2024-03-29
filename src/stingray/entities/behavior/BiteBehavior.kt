package stingray.entities.behavior;

import arc.*;
import arc.util.*;
import arc.util.io.*;
import arc.math.*;
import arc.scene.ui.layout.*;
import mindustry.gen.*;
import mindustry.content.*;
import mindustry.entities.*;
import stingray.util.*
import stingray.entities.*;

open class BiteBehavior(var damage: Float = 0f, var reload: Float = 1f, var angleMax: Float = 0f, var padding: Float = 0f, var maxHeal: Float = 0f) : BehaviorPattern("bite") {
	
	var reloadTimer = 0f;
	var healed = 0f;
	
	val effect = Fx.generate;
	
	constructor() : this(0f, 1f, 0f, 0f, 0f) {};
	
	override open fun apply(parent: mindustry.gen.Unit) {
		reloadTimer += Time.delta;
		if (reloadTimer < reload) return;
		
		while (reloadTimer >= 0f) { //incase if Time.delta >= reload
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
			
			if (healed < (maxHeal / (1f / reload))) { //maxHeal is the target heal/second
				parent.heal(Math.min(damage / 5f, Math.max(maxHeal - healed, 0f)));
				healed += damage / 5f;
			}
			
			if (enemy is mindustry.gen.Unit) {
				if (Mathf.chance(0.04)) enemy.apply(StatusEffects.unmoving, 100f);
				
				if (Mathf.chance(0.33)) enemy.apply(StatusEffects.shocked, 50f)
			}
		}
	}
	
	override fun display(table: Table) {
		super.display(table);
		
		table.table {
			it.defaults().growX().pad(5f);
			
			it.add("@ckat-stingray.stat.damage");
			it.add("@ckat-stingray.stat.reload");
			it.add("@ckat-stingray.stat.angle");
			it.add("@ckat-stingray.stat.range");
			it.add("@ckat-stingray.stat.max-heal");
			
			it.row();
			
			it.add("$damage / ${Core.bundle["ckat-stingray-bite"]}");
			it.add("${(reload / 60).toFixed(2)} ${Core.bundle["ckat-stingray-seconds"]}");
			it.add("${angleMax * 2}°");
			it.add("${padding / 8} ${Core.bundle["ckat-stingray-blocks"]}");
			it.add("${maxHeal} / ${Core.bundle["ckat-stingray-bite"]}");
		}
	}
	
	override open fun write(writes: Writes) {
		super.write(writes);
		writes.f(reloadTimer);
	}
	
	override open fun read(reads: Reads, revision: Int) {
		super.read(reads, revision);
		reloadTimer = reads.f();
	}
	
	override open fun copy() = BiteBehavior(damage, reload, angleMax, padding, maxHeal);
	
}