package meldexun.renderlib.integration;

import com.jredfox.menulib.menu.MenuRegistry;

public class MenuLib {

	public static int getFPSLimit() {
		return MenuRegistry.currentMenu != null ? MenuRegistry.currentMenu.getFrames() : -1;
	}

}
