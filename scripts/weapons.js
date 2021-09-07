const CFx = require("effects");

const hurricaneWeapon = extendContent(Weapon, "hurricane-generator", {
	top: true,
	x: 0,
	y: 32,
	reload: 90,
	recoil: 8,
	shake: 1,
	ejectEffect: CFx.hurricaneSpawn,
	
	bullet: extend(BulletType, {
		collides: false,
		lifetime: 400,
		homingPower: 0.02,
		shootEffect: Fx.none,
		despawnEffect: Fx.none,
		
		suckRadius: 80,
		power: 10,
		visualSize: 64,
		
		load() {
			this.drawRegion = Core.atlas.find("ckat-stingray-hurricane");
		},
		
		update(bullet) {
			this.super$update(bullet);
			
			Units.nearbyEnemies(bullet.owner.team, bullet.x, bullet.y, this.suckRadius, enemy => {
				let power = this.power / Math.cbrt(bullet.dst2(enemy) * enemy.hitSize) * bullet.fout();
				let angle = enemy.angleTo(bullet); 
				
				enemy.vel.x += Angles.trnsx(angle, power * Time.delta);
				enemy.vel.y += Angles.trnsy(angle, power * Time.delta);
				
				enemy.damage(this.power * 0.01 * Time.delta);
			});
		},
		
		draw(bullet) {
			this.super$draw(bullet);
			
			Draw.alpha(bullet.fin() * 40); /*10 ticks until full visibility*/
			Draw.rect(this.drawRegion, bullet.x, bullet.y, this.visualSize * bullet.fout(), this.visualSize * bullet.fout(), bullet.fout() * 2880);
		},
		
		continuousDamage() { return this.power * 0.01 * 60 }
	})
});

module.exports = {
	hurricane: hurricaneWeapon
}