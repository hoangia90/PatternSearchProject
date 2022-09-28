package fr.cea.bigpi.fhe.dap.patternsearch.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.cea.bigpi.fhe.dap.patternsearch.model.Data;
import fr.cea.bigpi.fhe.dap.patternsearch.repository.DataRepository;

@Service
public class DataService {

	@Autowired
	DataRepository dataRepository;

	// getting all data by using the method findaAll() of CrudRepository
	public List<Data> getAllData() {
		List<Data> data = new ArrayList<Data>();
		dataRepository.findAll().forEach(datum -> data.add(datum));
		return data;
	}

	// getting a specific item by using the method findById() of CrudRepository
	public Data getDataById(int id) {
//		return dataRepository.findById(id).get();
		return dataRepository.findOne(id);
	}

	public Data saveOrUpdate(String dataNo, String partnerID, Integer status, String description) {
		Data data = new Data();
		data.setStatus(status);
		data.setDescription(description);
		data.setCreatedDate(LocalDate.now().toString());
		data.setPartnerId(partnerID);
		data = saveOrUpdate(data);
		return data;
	}

	// saving a specific item by using the method save() of CrudRepository
	public Data saveOrUpdate(Data data) {
		return dataRepository.save(data);
	}

	// deleting a specific item by using the method deleteById() of CrudRepository
	public void delete(int id) {
//		dataRepository.deleteById(id);
		dataRepository.delete(id);
	}

	// updating a item by using the method save() of CrudRepository
	public void update(Data data, int dataid) {
		data.setDataId(dataid);
		dataRepository.save(data);
	}

}
