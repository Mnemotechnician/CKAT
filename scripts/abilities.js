/*Calls the function provided in the argument and adds the result to the unit's abilities. Required because hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh
hhhhhh hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh hhhhhhhhh aaaaaaaaAAAAAAAAAaa
i regret doing this. i hope people won't lunch me when I'll commit this*/
function AbilityInitializatorLambda(func_) {
	return extend(Ability, {
		func: func_,
		
		update(unit) {
			unit.abilities = this.func();
		}
	});
};


/*скат bite ability, damages any enemies infront of it and randomly applies unmoving effect to units*/
function SkatBiteAbilityLambda(damage_, reload_, angle_, padding_) {
	return extendContent(Ability, {
		damage: damage_,
		reload: reload_,
		angle: angle_ / 2,
		padding: padding_,
		reloadTimer: 0,
		
		effect: Fx.generate,
		
		update(skat) {
			if ((this.reloadTimer += Time.delta) < this.reload) return;
			this.reloadTimer = 0; 
			
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
				skat.heal(this.damage / 5);
				
				if (Mathf.chance(0.07) && enemy instanceof Unit) enemy.apply(StatusEffects.unmoving, 100);
			}
		}
	});
};

/*Skat swim ability, increases swim speed when travelling on water*/
function SkatSwimAbilityLambda(multiplier_) {
	return extend(Ability, {
		multiplier: multiplier_,
		
		update(skat) {
			let tile = Vars.world.tile(skat.x / 8, skat.y / 8);
			if (tile != null) {
				let floor = tile.floor();
				if (floor.liquidDrop == Liquids.water) {
					/*Dividing by tile's speed multi nivilates the speed multi of the tile*/
					skat.speedMultiplier *= (floor.isDeep() ? this.multiplier * this.multiplier : this.multiplier) / floor.speedMultiplier;
				}
			}
		}
	});
}

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
		
		maxDash: length_ / 4 * 1.5, /*1.5 length divided by speed*/
		speed: 4,
		
		
		update(skat) {
			if (this.isDashing && !Units.invalidateTarget(this.enemy, skat.team, skat.x, skat.y)) {
				this.dashFrame(skat);
			} else if ((this.reloadTimer += Time.delta) > this.reload) {
				this.enemy = Units.closestEnemy(skat.team, skat.x, skat.y, this.length + skat.hitSize, e => {
					let angle = skat.angleTo(e.x, e.y);
					let angleDist = Math.abs(angle - skat.rotation);
					return (angleDist < this.angle || 360 - angleDist < this.angle);
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
			/*TODO: actually make this work (yeah, like I'll do this, yeah)*/
			/*For a very dumb reason Units.invalidateTarget does not accept null. Thanks, javascript, I won't forget this.*/
			if (!this.isDashing || this.target == null || Units.invalidateTarget(this.enemy)) return;
			
			Draw.color(Pal.accent);
			Lines.circle(this.enemy.x, this.enemy.y, this.enemy.hitSize * 1.3);
		}
	})
}

module.exports = {
	bite: SkatBiteAbilityLambda,
	swim: SkatSwimAbilityLambda,
	dash: SkatDashAbilityLambda,
	init: AbilityInitializatorLambda
};