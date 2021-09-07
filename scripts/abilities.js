const CFx = require("effects");

/*I'm just hhh*/
function AbilityInitializatorLambda(func_) {
	return extend(Ability, {
		func: func_,
		
		update(unit) {
			unit.abilities = this.func();
		}
	});
};


/*скат bite ability, damages any enemies infront of it and randomly applies unmoving effect to units*/
function SkatBiteAbilityLambda(damage_, reload_, angle_, padding_, maxHeal_) {
	return extendContent(Ability, {
		damage: damage_,
		reload: reload_,
		angle: angle_ / 2,
		padding: padding_,
		maxHeal: maxHeal_ / (60 / reload_), //Max heal per hit
		
		reloadTimer: 0,
		healed: 0,
		
		effect: Fx.generate,
		
		update(skat) {
			if ((this.reloadTimer += Time.delta) < this.reload) return;
			this.reloadTimer = 0; 
			this.healed = 0;
			
			Units.nearbyEnemies(skat.team, skat.x, skat.y, skat.hitSize / 2 + this.padding, enemy => this.attackIfInfront(skat, enemy));
			Units.nearbyBuildings(skat.x, skat.y, skat.hitSize / 2 + this.padding, enemy => { if (enemy.team != skat.team) this.attackIfInfront(skat, enemy) });
		},
		
		attackIfInfront(skat, enemy) {
			let angle = skat.angleTo(enemy.x, enemy.y);
			let angleDist = Math.abs(angle - skat.rotation);
			
			/*two checks for situations like "angle = 350, rotation = 10"*/
			if (angleDist < this.angle || 360 - angleDist < this.angle) {
				enemy.damage(this.damage);
				this.effect.at(enemy.x, enemy.y, angle);
				
				if (this.healed < this.maxHeal) {
					skat.heal(Math.min(this.damage / 5, Math.max(this.maxHeal - this.healed, 0)));
					this.healed += this.damage / 5;
				}
				
				if (Mathf.chance(0.07) && enemy instanceof Unit) enemy.apply(StatusEffects.unmoving, 100);
			}
		}
	});
};

/*Skat swim ability, increases swim speed when travelling on water*/
function SkatSwimAbilityLambda(multiplier_) {
	return extend(Ability, {
		multiplier: multiplier_,
		
		isSwimming: false,
		
		update(skat) {
			let tile = Vars.world.tile(skat.x / 8, skat.y / 8);
			if (tile != null) {
				let floor = tile.floor();
				this.isSwimming = floor.liquidDrop == Liquids.water;
				
				if (this.isSwimming) {
					skat.speedMultiplier *= (floor.isDeep() ? this.multiplier * this.multiplier : this.multiplier) / floor.speedMultiplier;
				}
			}
		}
	});
}

/*Skat dash ability, скат will periodically dash on the closest enemy infront of it, dealing high damage and pushing the enemy back (force is based on masses of both units)*/
function SkatDashAbilityLambda(damage_, reload_, angle_, length_) {
	return extend(Ability, {
		damage: damage_,
		reload: reload_,
		angle: angle_,
		length: length_,
		
		isDashing: false,
		enemy: null,
		reloadTimer: 0,
		dashTimer: 0,
		
		maxDash: length_ / 2.5 * 1.5, /*1.5 length divided by speed*/
		speed: 2.5,
		
		timer: new Interval(1),
		
		
		update(skat) {
			if (this.isDashing && !Units.invalidateTarget(this.enemy, skat.team, skat.x, skat.y)) {
				this.dashFrame(skat);
			} else if ((this.reloadTimer += Time.delta) > this.reload) {
				this.enemy = Units.closestEnemy(skat.team, skat.x, skat.y, this.length + skat.hitSize, e => {
					let angle = skat.angleTo(e.x, e.y);
					let angleDist = Math.abs(angle - skat.rotation);
					return (angleDist < this.angle || 360 - angleDist < this.angle) && e.maxHealth * 4 > this.damage;
				});
				
				if (this.enemy != null) {
					this.isDashing = true;
					this.dashFrame(skat);
					this.reloadTimer = 0;
				}
			}
		},
		
		dashFrame(skat) {
			if ((this.dashTimer += Time.delta) >= this.maxDash) {
				this.isDashing = false;
				this.dashTimer = 0;
				return;
			}
			
			let angleNormal = skat.angleTo(this.enemy);
			let angle = 360 - angleNormal;
			skat.rotation = angleNormal;
			
			if (skat.dst(this.enemy) <= skat.hitSize + this.enemy.hitSize) {
				let pushForce = (skat.hitSize * skat.hitSize) / (this.enemy.hitSize * this.enemy.hitSize);
				
				this.enemy.vel.x += Mathf.sin(angle) * pushForce;
				this.enemy.vel.y += Mathf.cos(angle) * pushForce;
				this.enemy.damage(this.damage);
				
				this.isDashing = false;
				this.dashTimer = 0;
			} else {
				skat.vel.x += Mathf.sin(angle) * this.speed;
				skat.vel.y += Mathf.cos(angle) * this.speed;
			}
		},
		
		draw(skat) {
			if (!this.isDashing || Units.invalidateTarget(this.enemy, skat.team, skat.x, skat.y)) return;
			
			Draw.color(Pal.accent);
			Lines.circle(this.enemy.x, this.enemy.y, (this.enemy.hitSize * 1.5) / Math.min(skat.dst(this.enemy) / 80, 1.5));
			
			if (this.timer.get(8)) CFx.moveArrow.at(skat.x, skat.y, skat.rotation);
		},
		
		displayBars(skat, bars) {
        	bars.add(new Bar("stat.ckat-stingray-dashReload", Pal.accent, 
        		() => this.isDashing ? Math.min(this.dashTimer / this.maxDash, 1) : Math.min(this.reloadTimer / this.reload)
        	)).row();
    	}
	})
}

module.exports = {
	bite: SkatBiteAbilityLambda,
	swim: SkatSwimAbilityLambda,
	dash: SkatDashAbilityLambda,
	init: AbilityInitializatorLambda
};