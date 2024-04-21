package org.example.repository;

import java.util.Collection;
import java.util.Optional;

public interface BaseRepository<K, V> {

    void save(V employee);

    V getById(K id);

    Optional<V> findById(K id);

    Collection<V> findAll();

    void deleteAll();

    void deleteById(K id);
}
