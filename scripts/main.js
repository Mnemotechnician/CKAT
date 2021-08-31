const Ability = require("abilities");
const AI = require("AIs");

/*T1 скат*/
const Urotry = extendContent(UnitType, "skat-t1", {
	type: "ground",
	speed: 1.1,
	hitSize: 16,
	canBoost: false,
	canDrown: false, 
	health: 240,
	buildSpeed: 0,
	armor: 0,
	
	legLength: 0,
	legCount: 0,
	mechStride: 0,
	mechStepShake: 0,
	
	abilities: new Seq([
		Ability.bite(25, 60 / 3, 270, 10), //75 DPS per each enemy in range
		Ability.swim(1.5)
	]),
	
	research: {
		parent: UnitTypes.dagger,
		requirements: ItemStack.with(
			Items.graphite, 400,
			Items.silicon, 750,
			Items.titanium, 230, 
			Items.metaglass, 230
		)
	}
});
Urotry.constructor = () => extend(MechUnit, {});
Urotry.defaultController = AI.rammer;



/*T2 скат*/
const Mylio = extendContent(UnitType, "skat-t2", {
	type: "ground",
	speed: 0.5,
	hitSize: 32,
	canBoost: false,
	canDrown: false, 
	health: 650,
	buildSpeed: 0,
	armor: 8,
	
	legLength: 0,
	legCount: 0,
	mechStride: 0,
	mechStepShake: 0,
	
	abilities: new Seq([
		Ability.bite(50, 60 / 4, 30, 40), //200 DPS per each unit in range
		Ability.swim(2.2)
		/*Ability.dash(...) TODO*/
	]),
	
	research: {
		parent: Urotry,
		requirements: ItemStack.with(
			Items.graphite, 2600,
			Items.silicon, 4500,
			Items.titanium, 1200, 
		)
	}
});
Mylio.constructor = () => extend(MechUnit, {});
Mylio.defaultController = AI.rammer;



/*Anuke's скат unit itself*/
const Undulate = extendContent(UnitType, "skat-t3", {
	type: "ground",
	speed: 1.2,
	hitSize: 64,
	canBoost: false,
	canDrown: false, 
	health: 1300,
	buildSpeed: 0,
	armor: 12,
	
	legLength: 0,
	legCount: 0,
	mechStride: 0,
	mechStepShake: 0,
	
	abilities: new Seq([
		Ability.bite(28, 60 / 8, 95, 32), //224 DPS per each enemy in range
		Ability.swim(1.35)
	]),
	
	research: {
		parent: Mylio,
		requirements: ItemStack.with(
			Items.lead, 14000,
			Items.silicon, 10000,
			Items.titanium, 6000, 
			Items.metaglass, 3000
		)
	}
});
Undulate.constructor = () => extend(MechUnit, {});
Undulate.defaultController = AI.rammer;


/*Assigning units to factories*/
Blocks.navalFactory.plans.add(
	new UnitFactory.UnitPlan(Urotry, 60*30, ItemStack.with(
		Items.graphite, 20,
		Items.silicon, 35,
		Items.titanium, 10, 
		Items.metaglass, 10
	))
);