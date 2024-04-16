package org.itmo.eventapp.main.service;

import org.itmo.eventapp.main.model.entity.User;

public interface IEmailTokenService<TEntity> {

    TEntity findByToken(String token);

    String updateUserToken(User user);
}
