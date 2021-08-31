const Ability = require("abilities");
const AI = require("AIs");

/*скат unit itself*/
const CKAT = extendContent(UnitType, "skat-t3", {
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
		parent: UnitTypes.dagger,
		requirements: ItemStack.with(
			Items.graphite, 900,
			Items.silicon, 3000,
			Items.titanium, 2700, 
			Items.metaglass, 600
		)
	}
});
CKAT.constructor = () => extend(MechUnit, {});
CKAT.defaultController = AI.rammer;

Blocks.navalFactory.plans.add(
	new UnitFactory.UnitPlan(CKAT, 60*30, ItemStack.with(
		Items.graphite, 30, 
		Items.silicon, 100,
		Items.titanium, 90,
		Items.metaglass, 20
	))
);