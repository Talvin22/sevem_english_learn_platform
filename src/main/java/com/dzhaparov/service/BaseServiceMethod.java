package com.dzhaparov.service;

public interface BaseServiceMethod<T, D> {

    void createUser(D userDto);
    void deleteUser(D userDto);
    void updateUser(T id, D userDto);
    D getUser(T id);

}
