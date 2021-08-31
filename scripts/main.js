const Ability = require("abilities");
const AI = require("AIs");

/*T1 скат*/
const Urotry = extendContent(UnitType, "skat-t1", {
	type: "ground",
	speed: 1,
	hitSize: 16,
	canBoost: false,
	canDrown: false, 
	health: 0,
	buildSpeed: 0,
	armor: 0,
	
	legLength: 0,
	mechStride: 0,
	mechStepShake: 0,
	
	abilities: new Seq([
		Ability.bite(0, 60 / 1, 0, 0),
		Ability.swim(1)
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
	speed: 1,
	hitSize: 0,
	canBoost: false,
	canDrown: false, /*strong скат can't drown*/
	health: 0,
	buildSpeed: 0,
	armor: 0,
	
	legLength: 0, /*ehhhhhhh*/
	mechStride: 0,
	mechStepShake: 0,
	
	abilities: new Seq([
		Ability.bite(28, 60 / 1, 0, 0),
		Ability.swim(1)
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
	canDrown: false, /*strong скат can't drown*/
	health: 1300,
	buildSpeed: 0,
	armor: 10,
	
	legLength: 0, /*ehhhhhhh*/
	mechStride: 0,
	mechStepShake: 0,
	
	abilities: new Seq([
		Ability.bite(28, 60 / 8, 95, 5),
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