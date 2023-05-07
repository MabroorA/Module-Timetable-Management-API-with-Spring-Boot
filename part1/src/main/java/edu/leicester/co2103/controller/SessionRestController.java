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

import java.util.List;
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
//    @GetMapping("/sessions/convenor/{id}/module/{code}")
//    public ResponseEntity<?> Session_by_module_or_convenor(@PathVariable("id") long id,@PathVariable("id") String code,Convenor convenor,Module mode) {
//
//        if (repo.findById(id).isPresent()) {
//            Convenor convenor = repo.findById(id).get();
//            return new ResponseEntity<Convenor>(convenor, HttpStatus.OK);
//        } else
//            return new ResponseEntity<ErrorInfo>(new ErrorInfo("Convenor with id " + id + " not found"),
//                    HttpStatus.NOT_FOUND);
//    }
}
