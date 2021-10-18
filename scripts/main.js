const Ability = require("abilities");
const AI = require("AIs");
const Weapon = require("weapons");

/*T1 скат*/
const Urotry = extend(UnitType, "skat-t1", {
	type: "ground",
	speed: 1.1,
	hitSize: 27,
	canBoost: false,
	canDrown: false, 
	health: 400,
	buildSpeed: 0,
	armor: 0,
		
	legLength: 0,
	legCount: 0,
	mechStride: 0,
	mechStepShake: 0,
	
	immunities: ObjectSet.with(StatusEffects.wet),
	
	abilities: Seq.with(
		Ability.init(() => Seq.with(
			Ability.bite(25, 60 / 3, 270, 10, 30), //75 DPS per each enemy in range
			Ability.swim(1.5)
		))
	)
});
Urotry.constructor = () => extend(MechUnit, {});
Urotry.defaultController = AI.rammer;



/*T2 скат*/
const Mylio = extend(UnitType, "skat-t2", {
	type: "ground",
	speed: 0.5,
	hitSize: 37,
	canBoost: false,
	canDrown: false, 
	health: 650,
	buildSpeed: 0,
	armor: 8,
	
	legLength: 0,
	legCount: 0,
	mechStride: 0,
	mechStepShake: 0,
	
	immunities: ObjectSet.with(StatusEffects.wet),
	
	abilities: Seq.with(
		Ability.init(() => Seq.with(
			Ability.bite(50, 60 / 4, 30, 40, 50), //200 DPS per each unit in range
			Ability.swim(2.2),
			Ability.dash(500, 60 * 10, 30, 8 * 30)
		))
	)
});
Mylio.constructor = () => extend(MechUnit, {});
Mylio.defaultController = AI.rammer;



/*Anuke's скат unit itself*/
const Undulate = extend(UnitType, "skat-t3", {
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
	
	immunities: ObjectSet.with(StatusEffects.wet),
	
	abilities: Seq.with(
		Ability.init(() => Seq.with(
			Ability.bite(28, 60 / 8, 95, 32, 120), //224 DPS per each enemy in range
			Ability.swim(1.35)
		))
	)
});
Undulate.constructor = () => extend(MechUnit, {});
Undulate.defaultController = AI.rammer;



/*WORSHIP THE MIGHTY T4 СКАТ!!!*/
const Dasya = extend(UnitType, "skat-t4", {
	type: "ground",

	speed: 0.9,

	hitSize: 96,
	canBoost: false,
	canDrown: false, 
	health: 9100,
	buildSpeed: 0,
	armor: 24,
	
	legLength: 0,
	legCount: 0,
	mechStride: 0,
	mechStepShake: 0,
	
	immunities: ObjectSet.with(StatusEffects.wet, StatusEffects.freezing),
	
	weapons: Seq.with(Weapon.hurricane),
	
	abilities: Seq.with(
		Ability.init(() => Seq.with(
			Ability.bite(100, 60 / 5, 140, 32, 350), //500 DPS per each enemy in range
			Ability.swim(1.3),
			Ability.dash(2300, 60 * 15, 40, 8 * 40)
		))
	)
})
Dasya.constructor = () => extend(MechUnit, {});
Dasya.defaultController = AI.rammer;


/*Assigning units to factories*/
Blocks.navalFactory.plans.add(
	new UnitFactory.UnitPlan(Urotry, 60 * 30, ItemStack.with(
		Items.graphite, 20,
		Items.silicon, 35,
		Items.titanium, 10, 
		Items.metaglass, 10
	))
);
Blocks.additiveReconstructor.addUpgrade(Urotry, Mylio);
Blocks.multiplicativeReconstructor.addUpgrade(Mylio, Undulate);
Blocks.exponentialReconstructor.addUpgrade(Undulate, Dasya);

/*Tech tree*/
function unitTech(unit, parent) {
	return new TechTree.TechNode(TechTree.get(parent), unit, unit.researchRequirements());
};

unitTech(Urotry, UnitTypes.dagger);
unitTech(Mylio, Urotry);
unitTech(Undulate, Mylio);
unitTech(Dasya, Undulate);