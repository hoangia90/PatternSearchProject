package fr.cea.bigpi.fhe.dap.patternsearch.repository;

import org.springframework.data.repository.CrudRepository;

import fr.cea.bigpi.fhe.dap.patternsearch.model.Data;

@org.springframework.stereotype.Repository
public interface DataRepository extends CrudRepository<Data, Integer> {
}