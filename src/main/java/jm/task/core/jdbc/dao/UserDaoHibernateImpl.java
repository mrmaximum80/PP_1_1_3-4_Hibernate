package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;


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
        Session session = factory.getCurrentSession();
        session.beginTransaction();
        String command =
                "CREATE TABLE IF NOT EXISTS users (id BIGINT(12) PRIMARY KEY AUTO_INCREMENT, " +
                        "name VARCHAR(20), lastName VARCHAR(20), age TINYINT(3))";
        session.createSQLQuery(command).addEntity(User.class).executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void dropUsersTable() {
        Session session = factory.getCurrentSession();
        session.beginTransaction();
        String command =
                "DROP TABLE IF EXISTS users";
        session.createSQLQuery(command).addEntity(User.class).executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Session session = factory.getCurrentSession();
        User user = new User(name, lastName, age);
        session.beginTransaction();
        session.save(user);
        session.getTransaction().commit();
        session.close();
        System.out.println("User с именем – " + name + " добавлен в базу данных");
    }

    @Override
    public void removeUserById(long id) {
        Session session = factory.getCurrentSession();
        session.beginTransaction();
        User user = session.get(User.class, id);
        session.delete(user);
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public List<User> getAllUsers() {
        Session session = factory.getCurrentSession();
        session.beginTransaction();
        List<User> users = session.createQuery("from User").getResultList();
        session.getTransaction().commit();
        session.close();
        return users;
    }

    @Override
    public void cleanUsersTable() {
        Session session = factory.getCurrentSession();
        session.beginTransaction();
        session.createQuery("delete User").executeUpdate();
        session.getTransaction().commit();
        session.close();
    }
}
