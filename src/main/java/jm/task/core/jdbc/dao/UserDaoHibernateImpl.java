package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;


import javax.persistence.*;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    private SessionFactory factory;

    public UserDaoHibernateImpl() {
    }

    public UserDaoHibernateImpl(SessionFactory factory) {
        this.factory = factory;
    }


    @Override
    public void createUsersTable() {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            String command = "CREATE TABLE IF NOT EXISTS users (id BIGINT(12) PRIMARY KEY AUTO_INCREMENT, " +
                    "name VARCHAR(20), lastName VARCHAR(20), age TINYINT(3))";
            session.createSQLQuery(command).addEntity(User.class).executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            String command = "DROP TABLE IF EXISTS users";
            session.createSQLQuery(command).addEntity(User.class).executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = factory.getCurrentSession()) {
            User user = new User(name, lastName, age);
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
            System.out.println("User с именем – " + name + " добавлен в базу данных");
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            User user = session.get(User.class, id);
            session.delete(user);
            session.getTransaction().commit();
        }
    }

    @Override
    public List<User> getAllUsers() {
        try (Session session = factory.openSession()) {
            TypedQuery<User> typedQuery = session.createQuery("from User");
            List<User> users = typedQuery.getResultList();
            return users;
        }
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = factory.getCurrentSession()) {
            session.beginTransaction();
            session.createQuery("delete User").executeUpdate();
            session.getTransaction().commit();
        }
    }
}
