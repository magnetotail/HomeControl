package de.foospace.homecontrol.controlberry.remoteClasses;

import java.io.Serializable;

import de.foospace.homeControl.core.data.domain.Action;
import de.foospace.homeControl.core.data.domain.RaspberryPi;
import de.foospace.homeControl.core.remoteInterfaces.ActionInterface;
import de.foospace.homeControl.core.remoteInterfaces.IConfigurator;
import de.foospace.homecontrol.controlberry.App;

public class Configurator implements IConfigurator{
	
	private App app;

	public Configurator(App app) {
		this.app = app;
	}
	
	@Override
	public void setSystemConfiguration(RaspberryPi pi) {
		app.setConfiguration(pi);
	}

	@Override
	public RaspberryPi getSystemConfiguration() {
		return app.getConfiguration();
	}

	@Override
	public ActionInterface activateAction(Action action) {
		return null;
	}

	@Override
	public ActionInterface getRemoteActionInterfaceForAction(Action action) {
		return null;
	}

}
