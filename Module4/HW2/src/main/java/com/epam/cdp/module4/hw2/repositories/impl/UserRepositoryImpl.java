package com.epam.cdp.module4.hw2.repositories.impl;


import com.epam.cdp.module4.hw2.model.User;
import com.epam.cdp.module4.hw2.repositories.UserRepository;
import com.epam.cdp.module4.hw2.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private Storage storage;

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    @Override
    public User save(User entity) {
        String newEntryId = String.valueOf(entity.getId());
        storage.getUserStorage().put(newEntryId, entity);
        return storage.getUserStorage().get(newEntryId);
    }

    @Override
    public User findById(Long id) {
        return storage.getUserStorage().get(Long.toString(id));
    }

    @Override
    public User update(User entity) {
        String updatedEntityId = String.valueOf(entity.getId());
        return storage.getUserStorage().put(updatedEntityId, entity);
    }

    @Override
    public boolean delete(Long id) {
        User user = storage.getUserStorage().remove(String.valueOf(id));
        return user != null;
    }

    @Override
    public User findByEmail(String email) {
        Optional<User> user = storage.getUserStorage().values().stream()
                .filter(entry -> entry.getEmail().equalsIgnoreCase(email))
                .findAny();

        return user.orElse(null);
    }

    @Override
    public List<User> findByName(String name, int pageSize, int pageNum) {
        List<User> storageUsers = storage.getUserStorage().values().stream()
                .filter(user -> user.getName().contains(name))
                .collect(Collectors.toList());

        return getItems(pageSize, pageNum, storageUsers);
    }
}
