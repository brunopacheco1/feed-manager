package com.dev.bruno.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.dev.bruno.dao.LocalDAO;
import com.dev.bruno.dao.ShowDAO;
import com.dev.bruno.exception.GenericException;
import com.dev.bruno.model.Local;
import com.dev.bruno.model.LocalSimilar;
import com.dev.bruno.service.AbstractService;
import com.dev.bruno.utils.SimilarityUtils;

@Stateless
public class SimilarityService extends AbstractService {

	@EJB
	private LocalDAO localDAO;
	
	@EJB
	private ShowDAO showDAO;
	
	public List<String> checkSimilarity(Double percentage) throws GenericException {
		List<String> locais = localDAO.listNomes();
		Set<String> somas = new HashSet<>();
		List<String> resultado = new ArrayList<>();
		
		for(String input1 : locais) {
			for(String input2 : locais) {
				if(somas.contains(input1 + "_" + input2) || somas.contains(input2 + "_" + input1)) {
					continue;
				}
				
				somas.add(input1 + "_" + input2);
				somas.add(input2 + "_" + input1);
				
				Double dice = SimilarityUtils.dice(input1.toLowerCase(), input2.toLowerCase());
				
				if(dice != 1d && dice > percentage) {
					resultado.add(input1 + "_>_" + input2);
				}
			}
		}
		
		return resultado;
	}
	
	public void correctSimilarity(List<String> expressions) throws GenericException {
		for(String expression : expressions) {
			if(expression == null || expression.isEmpty()) {
				continue;
			}
			
			String [] names = expression.split("_>_");
			
			if(names.length < 2) {
				continue;
			}
			
			String principal = names[names.length - 1];
			
			Local localPrincipal = localDAO.findByNome(principal);
			
			if(localPrincipal == null) {
				continue;
			}

			Set<String> locaisSimilares = new HashSet<>();
			
			for(int i = 0; i < names.length - 1; i++) {
				Local local = localDAO.findByNome(names[i]);
				
				if(local == null) {
					continue;
				}
				
				if(!local.getId().equals(localPrincipal.getId())) {
					showDAO.updateLocal(local, localPrincipal);
				}
				
				for(LocalSimilar localSimilar : local.getLocaisSimilares()) {
					locaisSimilares.add(localSimilar.getNome());
				}
				
				locaisSimilares.add(names[i]);
				
				if(!local.getId().equals(localPrincipal.getId())) {
					localDAO.remove(local);
				}
			}
			
			for(String localSimilar : locaisSimilares) {
				LocalSimilar similar = new LocalSimilar();
				similar.setNome(localSimilar);
				similar.setLocal(localPrincipal);
				
				localDAO.add(similar);
			}
		}
	}
}