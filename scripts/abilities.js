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
			this.super$update(skat);
			
			let tile = Vars.world.tile(skat.x / 8, skat.y / 8);
			if (tile != null) {
				let floor = tile.floor();
				if (floor.liquidDrop == Liquids.water) {
					/*Dividing by tile's speed multi nivilates the speed multi of the tile*/
					skat.speedMultiplier *= this.multiplier * (floor.isDeep() ? this.multiplier : 1) / floor.speedMultiplier;
				}
			}
		}
	});
}

module.exports = {
	bite: SkatBiteAbilityLambda,
	swim: SkatSwimAbilityLambda
};