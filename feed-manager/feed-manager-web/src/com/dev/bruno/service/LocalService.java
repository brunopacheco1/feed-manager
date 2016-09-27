package com.dev.bruno.service;

import javax.ejb.EJB;
import javax.ejb.Singleton;

import com.dev.bruno.dao.LocalDAO;
import com.dev.bruno.exception.GenericException;
import com.dev.bruno.model.Local;
import com.dev.bruno.model.LocalSimilar;
import com.dev.bruno.service.AbstractService;
import com.dev.bruno.utils.SimilarityUtils;

@Singleton
public class LocalService extends AbstractService {

	@EJB
	private LocalDAO localDAO;
	
	public Local add(String nome) throws GenericException {
		Local result = localDAO.findByNome(nome);
		
		if(result != null) {
			return result;
		}
		
		for(Local local : localDAO.list()) {
			if(SimilarityUtils.similar(local.getNome().toLowerCase(), nome.toLowerCase())) {
				LocalSimilar localSimilar = new LocalSimilar();
				localSimilar.setLocal(local);
				localSimilar.setNome(nome);
				
				localDAO.add(localSimilar);
				
				result = local;
			}
		}
		
		if(result != null) {
			return result;
		}
		
		Local local = new Local();
		local.setNome(nome);
		
		localDAO.add(local);
		
		return local;
	}
}