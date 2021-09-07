const moveArrow = new Effect(60, e => {
	let size = 9 * e.fin();
	
	Draw.alpha(e.fin());
	Draw.color(Pal.accent);
	Lines.stroke(2);
	Lines.lineAngle(e.x, e.y, e.rotation + 225, size);
	Lines.lineAngle(e.x, e.y, e.rotation + 135, size);
	Draw.color();
});

const hurricaneSpawn = new Effect(10, e => {
	let size = 20 * e.fslope();
	
	Draw.alpha(e.fin());
	Draw.color(Pal.sap);
	Lines.lineAngle(e.x, e.y, e.rotation - 15, size);
	Lines.lineAngle(e.x, e.y, e.rotation, size);
	Lines.lineAngle(e.x, e.y, e.rotation + 15, size);
})

module.exports = {
	moveArrow: moveArrow,
	hurricaneSpawn: hurricaneSpawn
}