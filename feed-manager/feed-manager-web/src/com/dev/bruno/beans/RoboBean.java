package com.dev.bruno.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import com.dev.bruno.exception.GenericException;
import com.dev.bruno.locator.ServiceLocator;
import com.dev.bruno.model.Robot;
import com.dev.bruno.service.CrawlingService;
import com.dev.bruno.service.RobotService;

@Name("roboBean")
@Scope(ScopeType.CONVERSATION)
public class RoboBean extends AbstractBean{
	
	private static final long serialVersionUID = -3116434157312180392L;
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	private List<Robot> robots;
	private RobotService robotService = ServiceLocator.getInstance().lookup(RobotService.class);
	private Boolean seleted;
	private CrawlingService crawlingService = ServiceLocator.getInstance().lookup(CrawlingService.class);
	
	public List<Robot> getRobots() {
		if (robots == null) {
			robots = new ArrayList<>();
			
			try {
				robots.addAll(robotService.list(null, 0, 2000, "id", "asc"));
			} catch (GenericException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			}
		}
		return robots;
		
	}
	
	//executa o robos que etão selecionado atrvés da variável selected
	public void executarRobo() throws GenericException {

		Boolean selected = false;
		
		for (Robot robot : robots) {
			if (robot.getSelected()) {
				selected = true; 
				
				crawlingService.runCrawler(robot);
				
				robot.setSelected(false);
			}
		}
		
		if (selected) {
			addInfoMessageInfo("Robô(s) iniciado(s)!");
		} else {
			addInfoMessageInfo("Selecione ao menos um robô.");
		}
	}
	
	public void limparCaptacao() throws GenericException {
		Boolean selected = false;
		
		for (Robot robot : robots) {
			if (robot.getSelected()) {
				selected = true; 
				
				robotService.clearDocuments(robot.getId());
				robot.setSelected(false);
			}
		}
		
		if (!selected) {
			addInfoMessageInfo("Selecione ao menos um robô.");
		} else {
			addInfoMessageInfo("Shows removidos com sucesso.");
		}
	}
	
	public String getStatus(Robot robot) throws GenericException {
		return robotService.status(robot).getMessage();
	}
	

	public void setRobots(List<Robot> robots) {
		this.robots = robots;
	}

	public Boolean getSeleted() {
		return seleted;
	}
	
	public void setSeleted(Boolean seleted) {
		this.seleted = seleted;
	}
}