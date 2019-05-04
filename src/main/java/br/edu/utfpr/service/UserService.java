package br.edu.utfpr.service;

import br.edu.utfpr.model.dao.RoleDAO;
import br.edu.utfpr.model.dao.UserDAO;
import br.edu.utfpr.model.domain.Role;
import br.edu.utfpr.model.domain.User;
import br.edu.utfpr.util.JPAUtil;

/**
 * Created by ronifabio on 01/05/2019.
 */
public class UserService extends AbstractService<String, User> {

    public UserService() {
        dao = new UserDAO();
    }

    public boolean saveUserAndRole(User user, Role role){
        RoleDAO roleDAO = new RoleDAO();

        boolean isSuccess = true;
        try {
            JPAUtil.beginTransaction();
            dao.save(user);
            roleDAO.save(role);
            JPAUtil.commit();
        } catch (Exception e) {
            e.printStackTrace();
            isSuccess = false;
            JPAUtil.rollBack();
        } finally {
            JPAUtil.closeEntityManager();
        }
        return isSuccess;
    }
}
