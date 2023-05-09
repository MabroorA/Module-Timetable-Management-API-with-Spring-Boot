package edu.leicester.co2103.controller;

import edu.leicester.co2103.ErrorInfo;
import edu.leicester.co2103.domain.Convenor;
import edu.leicester.co2103.domain.Module;
import edu.leicester.co2103.domain.Session;
import edu.leicester.co2103.repo.ConvenorRepository;
import edu.leicester.co2103.repo.ModuleRepository;
import edu.leicester.co2103.repo.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class SessionRestController {
    @Autowired
    SessionRepository repo;
    @Autowired
    ModuleRepository M_repo;
    @Autowired
    ConvenorRepository C_repo;
    /*
    Deleting all sessions
     */
    @DeleteMapping("/sessions")
    public ResponseEntity<?> DeleteallSessions() {
        repo.deleteAll();
        return ResponseEntity.ok(null);
    }
    @GetMapping("/sessions")
    public ResponseEntity<List<Session>> listAllConvenors(){
        List<Session> sessions = repo.findAll();
        if (sessions.isEmpty()) {
            return new ResponseEntity<List<Session>>(HttpStatus.NO_CONTENT);

        }else
            return new ResponseEntity<List<Session>>(sessions,HttpStatus.OK);
    }

//    @GetMapping("/sessions/convenor/{id}/module/{code}")
//    public ResponseEntity<?> sessionsByModuleOrConvenor(@PathVariable("id") long id, @PathVariable("code") String code) {
//        Optional<Convenor> optionalConvenor = C_repo.findById(id);
//        if (optionalConvenor.isPresent()) {
//            Convenor convenor = optionalConvenor.get();
//            Module module = new Module();
//            List<Session> sessions = repo.findAll().stream()
//                    .filter(session -> session.module().getCode().equals(code))
//                    .filter(session -> session.getConvenor().getId() == id)
//                    .collect(Collectors.toList());
//            return new ResponseEntity<List<Session>>(sessions, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<ErrorInfo>(new ErrorInfo("Convenor with id " + id + " not found"),
//                    HttpStatus.NOT_FOUND);
//        }
//    }
//    Endpoint 20 not completed
}
