package com.company.enroller.persistence;

import java.util.Collection;

import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import com.company.enroller.model.Participant;

@Component("participantService")
public class ParticipantService {

	DatabaseConnector connector;

	public ParticipantService() {
		connector = DatabaseConnector.getInstance();
	}

	public Collection<Participant> getAll() {
		String hql = "FROM Participant";
		Query query = connector.getSession().createQuery(hql);
		return query.list();
	}

    public Participant findByLogin(String login) {
        String hql = "FROM Participant WHERE login = :login";
        Query<Participant> query = connector.getSession().createQuery(hql,  Participant.class);
        query.setParameter("login", login);
        return query.uniqueResult();
    }

    public void add(Participant participant) {
        Transaction transaction = connector.getSession().beginTransaction();
        connector.getSession().save(participant);
        transaction.commit();
    }

    public void delete(Participant participant) {
        Transaction transaction = connector.getSession().beginTransaction();
        connector.getSession().delete(participant);
        transaction.commit();
    }

    public void update(Participant participant) {
        Transaction transaction = connector.getSession().beginTransaction();
        connector.getSession().merge(participant);
        transaction.commit();
    }
}
