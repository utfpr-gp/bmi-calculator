package br.edu.utfpr.service;

import br.edu.utfpr.model.dao.UserDAO;
import br.edu.utfpr.model.domain.User;

/**
 * Created by ronifabio on 01/05/2019.
 */
public class UserService extends AbstractService<String, User> {

    public UserService() {
        dao = new UserDAO();
    }
}
