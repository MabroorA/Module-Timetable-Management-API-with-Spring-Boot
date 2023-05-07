package edu.leicester.co2103.repo;

import edu.leicester.co2103.domain.Convenor;
import org.springframework.data.repository.CrudRepository;

import edu.leicester.co2103.domain.Module;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleRepository extends CrudRepository<Module, String> {
    public List<Module> findAll();
}
