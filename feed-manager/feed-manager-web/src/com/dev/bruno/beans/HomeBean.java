package com.dev.bruno.beans;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

import com.dev.bruno.exception.GenericException;
import com.dev.bruno.locator.ServiceLocator;
import com.dev.bruno.model.Robot;
import com.dev.bruno.service.RobotService;
import com.dev.bruno.service.ShowService;

@Name("homeBean")
@Scope(ScopeType.CONVERSATION)
public class HomeBean extends AbstractBean{
	
	private static final long serialVersionUID = -3116434157312180392L;
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	private List<List<Robot>> robots;
	private List<Robot> allRobots;
	private Map<Robot, Long> totalNormalizadoMap;
	private Map<Robot, Long> totalNaoNormalizadoMap;
	private Map<Robot, Long> totalAceitoMap;
	private Map<Robot, Long> totalNaoAceitoMap;
	private Map<Robot, Date> totalUltimaCaptacaoMap;
	private Map<Robot, Date> totalUltimaNormalizacaoMap;
	private RobotService robotService = ServiceLocator.getInstance().lookup(RobotService.class);
	private ShowService showService = ServiceLocator.getInstance().lookup(ShowService.class);
	
	public List<List<Robot>> getRobots() {
		allRobots = new ArrayList<>();
		robots = new ArrayList<>();
			
		try {
			allRobots = robotService.list(null, 0, 2000, "id", "asc");
		} catch (GenericException e) {
			allRobots = new ArrayList<>();
			robots = new ArrayList<>();
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
		
		Long totalNormalizado = 0l;
		Long totalNaoNormalizado = 0l;
		Long totalAceito = 0l;
		Long totalNaoAceito = 0l;
		Date totalUltimaCaptacao = null;
		Date totalUltimaNormalizacao = null;
		
		totalNormalizadoMap = new HashMap<>();
		totalNaoNormalizadoMap = new HashMap<>();
		totalAceitoMap = new HashMap<>();
		totalNaoAceitoMap = new HashMap<>();
		totalUltimaCaptacaoMap = new HashMap<>();
		totalUltimaNormalizacaoMap = new HashMap<>();
		
		for(Robot robot : allRobots) {
			try {
				Long normalizado = showService.contarNormalizados(robot);
				Long naoNormalizado = showService.contarNaoNormalizados(robot);
				Long aceito = showService.contarAceitos(robot);
				Long naoAceito = showService.contarNaoAceitos(robot);
				Date ultimaCaptacao = showService.ultimaCaptacao(robot);
				Date ultimaNormalizacao = showService.ultimaNormalizacao(robot);
				
				totalNormalizado += normalizado;
				totalNaoNormalizado += naoNormalizado;
				totalAceito += aceito;
				totalNaoAceito += naoAceito;
				
				if(ultimaCaptacao != null && (totalUltimaCaptacao == null || totalUltimaCaptacao.before(ultimaCaptacao))) {
					totalUltimaCaptacao = ultimaCaptacao;
				}
				
				if(ultimaNormalizacao != null && (totalUltimaNormalizacao == null || totalUltimaNormalizacao.before(ultimaNormalizacao))) {
					totalUltimaNormalizacao = ultimaNormalizacao;
				}
				
				totalNormalizadoMap.put(robot, normalizado);
				totalNaoNormalizadoMap.put(robot, naoNormalizado);
				totalAceitoMap.put(robot, aceito);
				totalNaoAceitoMap.put(robot, naoAceito);
				totalUltimaCaptacaoMap.put(robot, ultimaCaptacao);
				totalUltimaNormalizacaoMap.put(robot, ultimaNormalizacao);
			} catch (GenericException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			}
		}
		
		Robot robot = new Robot();
		robot.setName("Resumo");
		
		totalNormalizadoMap.put(robot, totalNormalizado);
		totalNaoNormalizadoMap.put(robot, totalNaoNormalizado);
		totalAceitoMap.put(robot, totalAceito);
		totalNaoAceitoMap.put(robot, totalNaoAceito);
		totalUltimaCaptacaoMap.put(robot, totalUltimaCaptacao);
		totalUltimaNormalizacaoMap.put(robot, totalUltimaNormalizacao);
		
		allRobots.add(robot);
		
		Integer limit = 4;
		
		Integer lines = allRobots.size() / limit;
		
		if(allRobots.size() % limit != 0) {
			lines++;
		}
		
		for(int i = 0; i < lines; i++) {
			int start = i * limit;
			
			int end = Math.min(start + limit, allRobots.size());
			
			robots.add(allRobots.subList(start, end));
		}
		
		return robots;
	}

	public Map<Robot, Long> getTotalNormalizadoMap() {
		return totalNormalizadoMap;
	}

	public Map<Robot, Long> getTotalNaoNormalizadoMap() {
		return totalNaoNormalizadoMap;
	}

	public Map<Robot, Long> getTotalAceitoMap() {
		return totalAceitoMap;
	}

	public Map<Robot, Long> getTotalNaoAceitoMap() {
		return totalNaoAceitoMap;
	}

	public Map<Robot, Date> getTotalUltimaCaptacaoMap() {
		return totalUltimaCaptacaoMap;
	}

	public Map<Robot, Date> getTotalUltimaNormalizacaoMap() {
		return totalUltimaNormalizacaoMap;
	}
	
	public List<Robot> getAllRobots() {
		getRobots();
		return allRobots;
	}

	public void setAllRobots(List<Robot> allRobots) {
		this.allRobots = allRobots;
	}

	public String dataFormatada(Date date) {
		if(date == null) {
			return "";
		}
		return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(date);
	}
}