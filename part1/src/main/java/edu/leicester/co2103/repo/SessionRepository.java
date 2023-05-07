package edu.leicester.co2103.repo;

import edu.leicester.co2103.domain.Convenor;
import org.springframework.data.repository.CrudRepository;

import edu.leicester.co2103.domain.Session;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionRepository extends CrudRepository<Session, Long> {
    public List<Session> findAll();
    public void deleteById(long id);
}
