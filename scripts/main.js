function SkatBiteAbilityLambda(damage_, reload_) {
	return extend(Ability, {
		name: "Skat Bite",
		damage: damage_,
		reload: reload_,
		reloadTimer: 0,
		
		effect: Fx.shootSmall,
		
		update(skat) {
			if (this.reloadTimer++ < this.reload) return;
			this.reloadTimer = 0; 
			
			Units.nearbyEnemies(skat.team, skat.x, skat.y, skat.hitSize + 5, enemy => {
				let angle = skat.angleTo(enemy.x, enemy.y);
				let angleDist = Math.abs(angle - skat.rotation);
				
				/*two checks for situations like "angle = 350, rotation = 10"*/
				if (angleDist < 30 || 360 - angleDist < 30) {
					enemy.damage(this.damage);
					this.effect.at(enemy.x, enemy.y, angle);
					skat.heal(this.damage / 4);
					
					if (Mathf.chance(0.2)) enemy.apply(StatusEffects.unmoving, 60);
				}
			});
		}
	});
};

var CKAT = extendContent(UnitType, "skat", {
	type: "ground",
	speed: 1.3,
	hitSize: 64,
	canBoost: false,
	canDrown: false, /*strong скат can't drown*/
	health: 621,
	buildSpeed: 0,
	armor: 20,
	legLength: 0, /*ehhhhhhh*/

	abilities: new Seq([SkatBiteAbilityLambda(20, 60 / 8)]),
	
	research: {
		parent: UnitTypes.dagger,
		requirements: ItemStack.with(
			Items.graphite, 3000,
			Items.silicon, 3000,
			Items.titanium, 3000, 
			Items.metaglass, 600
		)
	},
	
	/*скат gets faster when travels on water: x1.5 speed on normal water, x2.25 on deep*/
	update(skat) {
		this.super$update(skat);
		
		let tile = Vars.world.tile(skat.x / 8, skat.y / 8);
		if (tile != null) {
			let floor = tile.floor();
			if (floor.liquidDrop == Liquids.water) {
				/*Dividing by tile's speed multi nivilates the speed multi of the tile*/
				skat.speedMultiplier *= 1.5 * (floor.isDeep() ? 1.5 : 1) / floor.speedMultiplier;
			}
		}
	}
});

/*We don't want скат to have legs, do we?*/
CKAT.mechStride = 0;
CKAT.mechStepShake = 0;

CKAT.constructor = () => extend(MechUnit, {});
CKAT.defaultController = () => extend(GroundAI, {});

Blocks.navalFactory.plans.add(
	new UnitFactory.UnitPlan(CKAT, 60*30, ItemStack.with(
		Items.graphite, 100, 
		Items.silicon, 100,
		Items.titanium, 100,
		Items.metaglass, 20
	))
);