package dao;

import java.util.List;

public interface CrudDAO<T> {
    void insert(T t);
    void update(T t);
    void delete(String id);
    T findById(String id);
    List<T> getAll();
}
