var CKAT = extendContent(UnitType, "skat", {
	type: "ground",
	speed: 1.3,
	hitSize: 64,
	canBoost: false,
	canDrown: false, //strong скат can't drown
	health: 621,
	buildSpeed: 0,
	armor: 20,
	legLength: 0, //ehhhhhhh
	
	research: {
		parent: UnitTypes.dagger,
		requirements: ItemStack.with(
			Items.graphite, 3000,
			Items.silicon, 3000,
			Items.titanium, 3000, 
			Items.metaglass, 600
		)
	},
	
	//скат gets faster when travels on water: x1.5 speed on normal water, x2.25 on deep
	update(ckat) {
		this.super$update(ckat);
		
		let tile = Vars.world.tile(ckat.x / 8, ckat.y / 8);
		if (tile != null) {
			let floor = tile.floor();
			if (floor.liquidDrop == Liquids.water) {
				//Dividing by tile's speed multi nivilates the speed multi of the tile
				ckat.speedMultiplier *= 1.5 * (floor.isDeep() ? 1.5 : 1) / floor.speedMultiplier;
			}
		}
	}
});

//We don't want скат to have legs, do we?
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