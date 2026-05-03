package com.company.enroller.persistence;

import java.util.Collection;
import java.util.Optional;

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

    public Collection<Participant> getAll(Optional<String> sortBy, Optional<String> sortOrder, Optional<String> key) {
        String hql = "FROM Participant p ";

        if (key.isPresent()) {
            hql += "WHERE p.login LIKE :key ";
        }

        if (sortBy.isPresent() && sortBy.get().equals("login")) {
            String order = sortOrder.map(String::toUpperCase)
                    .filter(o -> o.equals("ASC") || o.equals("DESC"))
                    .orElse("ASC");

            hql += "ORDER BY p.login " + order;
        }

        Query<Participant> query = connector.getSession().createQuery(hql, Participant.class);

        if (key.isPresent()) {
            query.setParameter("key", "%" + key.get() + "%");
        }

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