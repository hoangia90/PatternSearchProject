package fr.cea.bigpi.fhe.dmp.repository;

import org.springframework.data.repository.CrudRepository;

import fr.cea.bigpi.fhe.dmp.model.Data;

@org.springframework.stereotype.Repository
public interface DataRepository extends CrudRepository<Data, Integer> {
}
