const moveArrow = new Effect(60, e => {
	let size = 9 * e.fin();
	
	Draw.color(Pal.accent);
	Lines.stroke(2);
	Lines.lineAngle(e.x, e.y, e.rotation + 225, size);
	Lines.lineAngle(e.x, e.y, e.rotation + 135, size);
	Draw.color();
});

module.exports = {
	moveArrow: moveArrow
}