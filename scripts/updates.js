function parseVersion(version) {
	return Number.parseInt(version.replace(/[\./\s]/g, ""));
};

Events.on(ClientLoadEvent, () => {
	try {
		let mod = Vars.mods.getMod("ckat-stingray");
		let version = mod.meta.version;
		
		Http.get("https://raw.githubusercontent.com/MHeMoTexHuK/CKAT/master/LATEST_STABLE", r => {
			let latest = r.getResultAsString();
			
			if (!latest || latest === "") {
				Log.err("Received a malformed version.");
				return;
			} else if (parseVersion(version) < parseVersion(latest)) {
				if (!Vars.headless) {
					let dialog = new BaseDialog("update");
					dialog.cont.add("@ckat-stingray.update").row();
					dialog.cont.add(Core.bundle.format("ckat-stingray.version", mod.meta.version, latest)).padBottom(60).row();
					dialog.cont.add("@ckat-stingray.update-tip").padBottom(30).row();
					dialog.cont.image(Core.atlas.find("ckat-stingray-ckat")).size(100, 100).marginBottom(30).row();
					dialog.cont.button("@ckat-stingray.dismiss", () => dialog.hide()).size(200, 50);
					dialog.show();
				} else {
					Log.info("New ckat version available!")
				}
			}
		});
	} catch (e) {
		Log.err("Failed to fetch the latest version: " + e.getMessage());
	}
});