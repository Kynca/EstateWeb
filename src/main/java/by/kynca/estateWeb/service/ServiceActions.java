package by.kynca.estateWeb.service;

import by.kynca.estateWeb.entity.AbstractBean;

import java.util.List;

public interface ServiceActions<T extends AbstractBean> {
    T save(T entity);

    List<T> findAll(int page, String sort);
    T findById(Long id);
}
