const RammerAI = () => extend(AIController, {
	updateMovement() {
		let skat = this.unit;
		
		let e = Units.closestTarget(skat.team, skat.x, skat.y, 160, e => true);
		if (e != null) {
			this.target = e;
			this.moveTo(e, 6);
		} else if (this.command() == UnitCommand.rally) {
			this.target = this.targetFlag(skat.x, skat.y, BlockFlag.rally, false);
			if (this.target != null && !skat.within(this.target, 70)) {
				this.pathfind(Pathfinder.fieldRally);
				this.faceTarget();
				return;
			}
		} else if (this.command() != UnitCommand.idle) {
			let spawner = this.getClosestSpawner();
			let move = spawner != null && !skat.within(spawner, Vars.state.rules.dropZoneRadius + skat.hitSize * 1.5);
			if (move) this.pathfind(Pathfinder.fieldCore);
		}
		
		this.faceTarget();
	}
});

module.exports = {
	"rammer": RammerAI
};