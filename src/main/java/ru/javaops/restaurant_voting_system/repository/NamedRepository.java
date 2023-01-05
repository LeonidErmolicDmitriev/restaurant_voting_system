package ru.javaops.restaurant_voting_system.repository;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

@NoRepositoryBean
public interface NamedRepository<T> extends BaseRepository<T> {

    @Transactional(readOnly = true)
    T findByName(String name);
}