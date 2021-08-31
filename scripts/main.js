/*скат bite ability, damages any enemies infront of it and randomly applies unmoving effect to units*/
function SkatBiteAbilityLambda(damage_, reload_, angle_) {
	return extendContent(Ability, {
		damage: damage_,
		reload: reload_,
		angle: angle_ / 2,
		reloadTimer: 0,
		
		effect: Fx.generate,
		
		update(skat) {
			if ((this.reloadTimer += Time.delta) < this.reload) return;
			this.reloadTimer = 0; 
			
			Units.nearbyEnemies(skat.team, skat.x, skat.y, skat.hitSize + 5, enemy => this.attackIfInfront(skat, enemy));
			Units.nearbyBuildings(skat.x, skat.y, skat.hitSize + 5, enemy => { if (enemy.team != skat.team) this.attackIfInfront(skat, enemy) });
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

/*скат unit itself*/
var CKAT = extendContent(UnitType, "skat", {
	type: "ground",
	speed: 1.2,
	hitSize: 64,
	canBoost: false,
	canDrown: false, /*strong скат can't drown*/
	health: 800,
	buildSpeed: 0,
	armor: 10,
	
	legLength: 0, /*ehhhhhhh*/
	mechStride: 0,
	mechStepShake: 0,
	
	abilities: new Seq([SkatBiteAbilityLambda(20, 60 / 8, 80)]),
	
	research: {
		parent: UnitTypes.dagger,
		requirements: ItemStack.with(
			Items.graphite, 900,
			Items.silicon, 3000,
			Items.titanium, 2700, 
			Items.metaglass, 600
		)
	},
	
	/*скат gets faster when travels on water*/
	update(skat) {
		this.super$update(skat);
		
		let tile = Vars.world.tile(skat.x / 8, skat.y / 8);
		if (tile != null) {
			let floor = tile.floor();
			if (floor.liquidDrop == Liquids.water) {
				/*Dividing by tile's speed multi nivilates the speed multi of the tile*/
				skat.speedMultiplier *= 1.3 * (floor.isDeep() ? 1.5 : 1) / floor.speedMultiplier;
			}
		}
	}
});

CKAT.constructor = () => extend(MechUnit, {});
/*Custom скат ai: rams nearby units and buildings, travels to the drop zone if there's none*/
CKAT.defaultController = () => extend(AIController, {
	updateMovement() {
		let skat = this.unit;
		let e = Units.closestTarget(skat.team, skat.x, skat.y, 160, e => true);
		
		if (e != null) {
			this.target = e;
			this.moveTo(e, 6);
		} else {
			let spawner = this.getClosestSpawner();
			let move = spawner != null && !skat.within(spawner, Vars.state.rules.dropZoneRadius + skat.hitSize * 1.5);
			if (move) this.pathfind(Pathfinder.fieldCore);
		}
		
		this.faceTarget();
	}
});

Blocks.navalFactory.plans.add(
	new UnitFactory.UnitPlan(CKAT, 60*30, ItemStack.with(
		Items.graphite, 30, 
		Items.silicon, 100,
		Items.titanium, 90,
		Items.metaglass, 20
	))
);