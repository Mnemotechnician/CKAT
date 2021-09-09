const moveArrow = new Effect(120, e => {
	let size = 9 * e.fin();
	
	Draw.color(Pal.accent);
	Draw.alpha(e.fout() * 15);
	Lines.stroke(2);
	Lines.lineAngle(e.x, e.y, e.rotation + 225, size);
	Lines.lineAngle(e.x, e.y, e.rotation + 135, size);
	Draw.color();
});

const hurricaneSpawn = new Effect(10, e => {
	let size = 20 * e.fslope();
	let ox = Angles.trnsx(e.rotation, 20), oy = Angles.trnsy(e.rotation, 20);
	
	Draw.color(Pal.spore);
	Draw.alpha(e.fout() * 4);
	Lines.stroke(3);
	Lines.lineAngle(e.x - ox, e.y - oy, e.rotation - 15, size);
	Lines.lineAngle(e.x - ox, e.y - oy, e.rotation, size);
	Lines.lineAngle(e.x - ox, e.y - oy, e.rotation + 15, size);
	
	if (e.data instanceof Team) Drawf.light(e.data(), e.x, e.y, 16, Pal.reactorPurple, 0.5);
	else Drawf.light(e.x, e.y, 16, Pal.reactorPurple, 0.5);
});

/* unused, ignore. I'm too lazy to exclude it from coomit
const groundShake = new Effect(120, e => {
	let radius = 250 * e.fin();
	let innerRadius = Math.max(250 * (e.fin() - 0.1) * 1.111, 0);
	
	Draw.color(Pal.lightFlame);
	Draw.alpha(e.fout() * 4);
	Lines.stroke(5);
	Lines.circle(e.x, e.y, radius);
	
	Draw.color(Pal.lightOrange, Pal.lightFlame, e.fin());
	Lines.stroke(e.fout() * 5);
	Lines.circle(e.x, e.y, innerRadius);
	
	Draw.color();
});
*/

module.exports = {
	moveArrow: moveArrow,
	hurricaneSpawn: hurricaneSpawn/*,
	groundShake: groundShake*/
}