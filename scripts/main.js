var CKAT = extendContent(UnitType, "skat", {
	type: "ground",
	speed: 1.3,
	hitSize: 64,
	canBoost: false,
	canDrown: true,
	health: 621,
	buildSpeed: 0,
	armor: 20,
	research: {
		parent: UnitTypes.dagger,
		requirements: ItemStack.with(
			Items.graphite, 10000,
			Items.silicon, 10000,
			Items.titanium, 10000, 
			Items.metaglass, 2000
		)
	}
});

CKAT.canDrown = true; //????????????????
//скат is strong and doesn't want to drown

//Idk wtf is this, this was the first solution i found via discord search
CKAT.constructor = () => extend(UnitEntity, {});
CKAT.defaultController = () => extend(GroundAI, {});

Blocks.groundFactory.plans.add(
	new UnitFactory.UnitPlan(CKAT, 60*30, ItemStack.with(
		Items.graphite, 100, 
		Items.silicon, 100,
		Items.titanium, 100,
		Items.metaglass, 20
	))
);