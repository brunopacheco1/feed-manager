package com.dev.bruno.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public abstract class AbstractBean implements Serializable {

	private static final long serialVersionUID = 4559887475881071667L;

	protected Logger logger = Logger.getLogger(this.getClass().getName());
	
	public static void addInfoMessageInfo(String message) {
	    FacesContext.getCurrentInstance().addMessage("info", new FacesMessage(FacesMessage.SEVERITY_INFO, message, message));
	}
	
	public boolean checkDate(Date datInicial, Date datFinal) {

		if ((datInicial != null && datFinal == null) || (datInicial == null && datFinal != null)) {
			addInfoMessageInfo("A data inicial e final devem estar preencidas!");
			return false;
		}else {
		
			if (datInicial!=null?datInicial.after(datFinal):false) {
				addInfoMessageInfo("A data inicial não pode ser maior que a data final!");
				return false;
			}
		}
		
		return true;
	}
}