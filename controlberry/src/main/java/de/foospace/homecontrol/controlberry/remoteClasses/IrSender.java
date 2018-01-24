package de.foospace.homecontrol.controlberry.remoteClasses;

import java.io.IOException;

import de.foospace.homeControl.core.remoteInterfaces.IrControllable;


public class IrSender implements IrControllable {

	public String sendIRCode(String code) throws IOException{
		System.out.println(code);
		return code;
	}

}
