package com.reactor.academic.service.impl;

import com.reactor.academic.documment.Rol;
import com.reactor.academic.documment.User;
import com.reactor.academic.repository.IUserRepo;
import com.reactor.academic.security.UserSecurity;
import com.reactor.academic.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    private IUserRepo repo;

    @Autowired
    public UserServiceImpl(IUserRepo repo) {
        this.repo = repo;
    }

    @Override
    public Mono<User> register(User t) {
        return repo.save(t);
    }

    @Override
    public Mono<User> modify(User t) {
        return repo.save(t);
    }

    @Override
    public Flux<User> list() {
        return repo.findAll();
    }

    @Override
    public Mono<User> listById(String s) {
        return repo.findById(s);
    }

    @Override
    public Mono<Void> delete(String v) {
        return repo.deleteById(v);
    }

    @Override
    public Mono<UserSecurity> findByUser(String user) {
        Mono<User> monoUser = repo.findOneByUser(user);

        List<String> roles = new ArrayList<String>();

        return monoUser.doOnNext(u -> {
            for (Rol role : u.getRoles()) {
                roles.add(role.getName());
            }
        }).flatMap(u ->
                Mono.just(new UserSecurity(u.getUser(), u.getPass(), u.getState(), roles))
        );
    }

}