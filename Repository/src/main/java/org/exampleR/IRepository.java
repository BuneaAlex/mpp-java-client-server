package org.exampleR;

import org.exampleM.Identifiable;

public interface IRepository<ID, E extends Identifiable<ID>> {

    E findOne(ID id);

    Iterable<E> findAll();

    void save(E entity);

    void delete(ID id);

    void update(E entity);
}
