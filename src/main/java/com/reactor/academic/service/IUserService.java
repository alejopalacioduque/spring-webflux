package com.reactor.academic.service;

import com.reactor.academic.documment.User;
import com.reactor.academic.security.UserSecurity;
import reactor.core.publisher.Mono;

public interface IUserService extends ICRUD<User, String> {

    Mono<UserSecurity> findByUser(String userId);

}