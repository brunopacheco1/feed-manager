package com.dev.bruno.dao;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;

import com.dev.bruno.exception.InvalidValueException;
import com.dev.bruno.exception.MandatoryFieldsException;
import com.dev.bruno.model.Local;
import com.dev.bruno.model.Robot;
import com.dev.bruno.model.Show;

@Stateless
public class ShowDAO extends AbstractDAO<Long, Show> {
	
	public String getEntityName() {
		return "Show";
	}
	
	public List<Show> listarShowsPendentes(String queryStr, Integer start,
			Integer limit, String order, String dir) throws InvalidValueException, MandatoryFieldsException    {

		if (start == null || limit == null || order == null || dir == null) {
			throw new MandatoryFieldsException("start, limit, order e dir são obrigatórios");
		}

		if (!orderOptions().contains(order) || !dirOptions().contains(dir)) {
			throw new InvalidValueException(String.format(
					"Possíveis valores para order[%s] e dir[%s]",
					StringUtils.join(orderOptions(), ", "),
					StringUtils.join(dirOptions(), ", ")));
		}

		StringBuilder hql = new StringBuilder(
				"select s from Show s where s.rejeicao is null and s.aprovacao is null ");

		if (queryStr != null && !queryStr.isEmpty()) {
			hql.append(" and (");

			boolean first = true;

			for (String queryOption : queryOptions()) {
				if (!first) {
					hql.append(" or ");
				}

				hql.append("upper(s.").append(queryOption)
						.append(") like upper(:").append(queryOption)
						.append(")");

				first = false;
			}

			hql.append(")");
		}

		hql.append(" order by s." + order + " " + dir);

		TypedQuery<Show> query = manager
				.createQuery(hql.toString(), Show.class);

		if (queryStr != null && !queryStr.isEmpty()) {
			for (String queryOption : queryOptions()) {
				query.setParameter(queryOption, "%" + queryStr + "%");
			}
		}

		return query.setFirstResult(start).setMaxResults(limit).getResultList();
	}
	
	public List<Show> listarShowsPendentesFiltros(Date dataIniciorealizacao, Date dataFimRealizacao, String nome, String local) throws InvalidValueException, MandatoryFieldsException    {

		StringBuilder hql = new StringBuilder("select s from Show s where s.rejeicao is null and s.aprovacao is null ");
		
		if(dataIniciorealizacao != null && dataFimRealizacao != null)
		hql.append(" and s.realizacao between :dateInicial  and :datefinal ");
		
		if((nome != null) && (!nome.isEmpty()) )
			hql.append(" and lower(s.nome) like lower(:nome) ");
		
		if((local != null) && (!local.isEmpty()))
			hql.append(" and lower(s.localStr) like lower(:local) ");
		

		hql.append(" order by s.realizacao desc, s.localStr asc");

		TypedQuery<Show> query = manager.createQuery(hql.toString(), Show.class);

		if(dataIniciorealizacao != null && dataFimRealizacao != null){
		query.setParameter("dateInicial",dataIniciorealizacao);
		query.setParameter("datefinal", dataFimRealizacao );
		}
		
		if((nome != null) && (!nome.isEmpty()) )
			query.setParameter("nome", "%"+nome+"%");
		
		if((local != null) && (!local.isEmpty()))
			query.setParameter("local", "%"+local+"%");
		
		return query.getResultList();
	}

	@Override
	public Set<String> orderOptions() {
		Set<String> orderOptions = new HashSet<>();
		
		orderOptions.add("id");
		orderOptions.add("local");
		orderOptions.add("realizacao");
		orderOptions.add("nome");
		orderOptions.add("sinopse");
		orderOptions.add("document.id");
		orderOptions.add("document.captureDate");
		orderOptions.add("document.chave");
		
		return orderOptions;
	}
	
	@Override
	public Set<String> queryOptions() {
		Set<String> queryOptions = new HashSet<>();
		
		queryOptions.add("nome");
		queryOptions.add("local");
		queryOptions.add("sinopse");
		queryOptions.add("document.chave");
		
		return queryOptions;
	}

	public void updateLocal(Local oldLocal, Local newLocal) throws MandatoryFieldsException {
		if(oldLocal == null || newLocal == null) {
			throw new MandatoryFieldsException("oldLocal e newLocal são obrigatórios");
		}
		
		Query query = manager.createQuery("update Show set local = :newLocal where local = :oldLocal");
		query.setParameter("oldLocal", oldLocal);
		query.setParameter("newLocal", newLocal);
		
		query.executeUpdate();
	}
	
	public void removeFromRobot(Long robotId) throws MandatoryFieldsException {
		if(robotId == null) {
			throw new MandatoryFieldsException("RobotId é obrigatório");
		}
		
		Query query = manager.createQuery("delete from Show where id in (select s.id from Show s where s.document.url.robot.id = :id)");
		query.setParameter("id", robotId);
		query.executeUpdate();
		
		query = manager.createQuery("delete from Document where id in (select d.id from Document d where d.url.robot.id = :id)");
		query.setParameter("id", robotId);
		query.executeUpdate();
		
		query = manager.createQuery("delete from DocumentURL where id in (select u.id from DocumentURL u where u.robot.id = :id)");
		query.setParameter("id", robotId);
		query.executeUpdate();
	}

	public Long contarNormalizados(Robot robot) throws MandatoryFieldsException {
		if(robot == null) {
			throw new MandatoryFieldsException("robot é obrigatório");
		}
		
		StringBuilder hql = new StringBuilder("select count(s) from Show s where s.document.url.robot = :robot");
		
		TypedQuery<Long> query = manager.createQuery(hql.toString(), Long.class);
		query.setParameter("robot", robot);
		
		return query.getSingleResult();
	}
	
	public Long contarNaoNormalizados(Robot robot) throws MandatoryFieldsException {
		if(robot == null) {
			throw new MandatoryFieldsException("robot é obrigatório");
		}
		
		StringBuilder hql = new StringBuilder("select count(u) from DocumentURL u where u.robot = :robot and u not in (select d.url from Document d where d.url.robot = :robot)");
		
		TypedQuery<Long> query = manager.createQuery(hql.toString(), Long.class);
		query.setParameter("robot", robot);
		
		return query.getSingleResult();
	}
	
	public Long contarAceitos(Robot robot) throws MandatoryFieldsException {
		if(robot == null) {
			throw new MandatoryFieldsException("robot é obrigatório");
		}
		
		StringBuilder hql = new StringBuilder("select count(s) from Show s where s.aprovacao is not null and s.document.url.robot = :robot");
		
		TypedQuery<Long> query = manager.createQuery(hql.toString(), Long.class);
		query.setParameter("robot", robot);
		
		return query.getSingleResult();
	}
	
	public Long contarNaoAceitos(Robot robot) throws MandatoryFieldsException {
		if(robot == null) {
			throw new MandatoryFieldsException("robot é obrigatório");
		}
		
		StringBuilder hql = new StringBuilder("select count(s) from Show s where s.rejeicao is not null and s.document.url.robot = :robot");
		
		TypedQuery<Long> query = manager.createQuery(hql.toString(), Long.class);
		query.setParameter("robot", robot);
		
		return query.getSingleResult();
	}

	public Date ultimaCaptacao(Robot robot) throws MandatoryFieldsException {
		if(robot == null) {
			throw new MandatoryFieldsException("robot é obrigatório");
		}
		
		StringBuilder hql = new StringBuilder("select max(u.captureDate) from DocumentURL u where u.robot = :robot");
		
		TypedQuery<Date> query = manager.createQuery(hql.toString(), Date.class);
		query.setParameter("robot", robot);
		
		return query.getSingleResult();
	}

	public Date ultimaNormalizacao(Robot robot) throws MandatoryFieldsException {
		if(robot == null) {
			throw new MandatoryFieldsException("robot é obrigatório");
		}
		
		StringBuilder hql = new StringBuilder("select max(d.normalizationDate) from Document d where d.url.robot = :robot");
		
		TypedQuery<Date> query = manager.createQuery(hql.toString(), Date.class);
		query.setParameter("robot", robot);
		
		return query.getSingleResult();
	}
}