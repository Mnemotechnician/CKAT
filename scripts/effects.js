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
	let ox = Angles.trnsx(e.rotation, 20), oy = Angles.trnsy(e.rotation, 20);
	
	Draw.alpha(e.fin());
	Draw.color(Pal.spore);
	Lines.lineAngle(e.x - ox, e.y - oy, e.rotation - 15, size);
	Lines.lineAngle(e.x - ox, e.y - oy, e.rotation, size);
	Lines.lineAngle(e.x - ox, e.y - oy, e.rotation + 15, size);
	
	if (e.data instanceof Team) Drawf.light(e.data(), e.x, e.y, 16, Pal.reactorPurple, 0.5);
	else Drawf.light(e.x, e.y, 16, Pal.reactorPurple, 0.5);
})

module.exports = {
	moveArrow: moveArrow,
	hurricaneSpawn: hurricaneSpawn
}