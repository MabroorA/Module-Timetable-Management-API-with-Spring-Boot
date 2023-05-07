package edu.leicester.co2103.controller;


import edu.leicester.co2103.ErrorInfo;
import edu.leicester.co2103.domain.Convenor;
import edu.leicester.co2103.domain.Module;
import edu.leicester.co2103.domain.Session;
import edu.leicester.co2103.repo.ModuleRepository;
import edu.leicester.co2103.repo.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@RestController
public class ModuleRestController {
    @Autowired
    ModuleRepository repo;

    /*
    Retrieve all modules (endpoint #7)
     */
    @GetMapping("/modules")
    public ResponseEntity<List<Module>> listAllModules() {
        List<Module> modules = repo.findAll();
        if (modules.isEmpty()) {
            return new ResponseEntity<List<Module>>(HttpStatus.NO_CONTENT);

        } else
            return new ResponseEntity<List<Module>>(modules, HttpStatus.OK);
    }

    /*
    Creating a Module
     */
    @PostMapping("/modules")
    public ResponseEntity<?> createModule(@RequestBody Module module, UriComponentsBuilder ucBuilder) {

        if (repo.existsById(module.getCode())) {
            return new ResponseEntity<ErrorInfo>(new ErrorInfo("A Module with code " + module.getCode() + " already exists."),
                    HttpStatus.CONFLICT);
        }
        repo.save(module);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/modules/{code}").buildAndExpand(module.getCode()).toUri());
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    /*
    Getting a specific module
     */
    @GetMapping("/modules/{code}")
    public ResponseEntity<?> getModule(@PathVariable("code") String code) {

        if (repo.findById(code).isPresent()) {
            Module module = repo.findById(code).get();
            return new ResponseEntity<Module>(module, HttpStatus.OK);
        } else
            return new ResponseEntity<ErrorInfo>(new ErrorInfo("Module with code " + code + " not found"),
                    HttpStatus.NOT_FOUND);
    }

    /*
    Updating a module
     */
    @PutMapping("/modules/{code}")
    public ResponseEntity<?> updateModule(@PathVariable("code") String code, @RequestBody Module newModule) {
        if (repo.findById(code).isPresent()) {
            Module currentModule = repo.findById(code).get();
            currentModule.setCode(newModule.getCode());
            currentModule.setTitle(newModule.getTitle());
            currentModule.setLevel(newModule.getLevel());
            currentModule.setSessions(newModule.getSessions());


            currentModule.getSessions().clear();
            currentModule.getSessions().addAll(newModule.getSessions());

            repo.save(currentModule);
            return new ResponseEntity<Module>(currentModule, HttpStatus.OK);
        } else
            return new ResponseEntity<ErrorInfo>(new ErrorInfo("Module with code " + code + " not found."),
                    HttpStatus.NOT_FOUND);
    }

    /*
    Partially updating a module and the module is not fully replaced
     */
    @PatchMapping("/modules/{code}")
    public ResponseEntity<?> partialUpdateModule(@PathVariable("code") String code, @RequestBody Module moduleUpdates) {
        if (repo.findById(code).isPresent()) {
            Module currentModule = repo.findById(code).get();

            if (moduleUpdates.getTitle() != null) {
                currentModule.setTitle(moduleUpdates.getTitle());
            }
            if (moduleUpdates.getLevel() != 0) {
                currentModule.setLevel(moduleUpdates.getLevel());
            }

            // Handle session updates if present in the request body
            if (moduleUpdates.getSessions() != null) {
                for (Session updatedSession : moduleUpdates.getSessions()) {
                    boolean sessionFound = false;
                    for (Session currentSession : currentModule.getSessions()) {
                        if (updatedSession.getId() == currentSession.getId()) {
                            currentSession.setTopic(updatedSession.getTopic());
                            currentSession.setDatetime(updatedSession.getDatetime());
                            currentSession.setDuration(updatedSession.getDuration());

                            sessionFound = true;
                            break;
                        }
                    }
                    if (!sessionFound) {
                        // Add new session if not found in the currentModule
                        currentModule.getSessions().add(updatedSession);
                    }
                }
            }

            repo.save(currentModule);
            return new ResponseEntity<Module>(currentModule, HttpStatus.OK);
        } else {
            return new ResponseEntity<ErrorInfo>(new ErrorInfo("Module with code " + code + " not found."), HttpStatus.NOT_FOUND);
        }
    }

    /*
    Deleting a module (endpoint 11)
     */
    @DeleteMapping("/modules/{code}")
    public ResponseEntity<?> deleteConvenor(@PathVariable("code") String code) {

        if (repo.findById(code).isPresent()) {
            repo.deleteById(code);
            return ResponseEntity.ok(null);
        } else
            return new ResponseEntity<ErrorInfo>(new ErrorInfo("Module with code " + code + " not found."),
                    HttpStatus.NOT_FOUND);

    }

    /*
     list all sessions in a module (endpoint #12)
     */
    @GetMapping("/modules/{code}/sessions")
    public ResponseEntity<?> getsessionsbymodule(@PathVariable("code") String code) {

        if (repo.findById(code).isPresent()) {
            Module module = repo.findById(code).get();
            List<Session> sessions = module.getSessions();
            return new ResponseEntity<List<Session>>(sessions, HttpStatus.OK);
        } else
            return new ResponseEntity<ErrorInfo>(new ErrorInfo("Module with code " + code + " not found"),
                    HttpStatus.NOT_FOUND);
    }


    /*
    Creating a Session for a Specific  Module
     */
    @PostMapping("/modules/{code}/sessions")
    public ResponseEntity<?> createSessionbymodule(@PathVariable("code") String code, @RequestBody Session session) {
        Optional<Module> optionalModule = repo.findById(code);
        if (optionalModule.isPresent()) {
            Module module = optionalModule.get();
            List<Session> sessions = module.getSessions();
            sessions.add(session);
            module.setSessions(sessions);

            repo.save(module);
            return new ResponseEntity<Session>(session, HttpStatus.CREATED);
        } else {

            return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
        }
    }

    /*
    Get a Specific session from a  specific module
     */
    @GetMapping("/modules/{code}/sessions/{id}")
    public ResponseEntity<?> getSessionbymodule(@PathVariable("code") String code, @PathVariable("id") long id) {

        if (repo.findById(code).isPresent()) {
            Module module = repo.findById(code).get();
            List<Session> sessions = module.getSessions();
            Session session = sessions.stream().filter(s -> s.getId() == id).findFirst().orElse(null);
            if (session != null) {
                return new ResponseEntity<Session>(session, HttpStatus.OK);
            } else {
                return new ResponseEntity<ErrorInfo>(new ErrorInfo("Session with id " + id + " not found in module " + code),
                        HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<ErrorInfo>(new ErrorInfo("Module with code " + code + " not found"),
                    HttpStatus.NOT_FOUND);
        }
    }

    /*
    Updating a session (by replacing)
     */
    @PutMapping("/modules/{code}/sessions/{id}")
    public ResponseEntity<?> updateSessionByModule(@PathVariable("code") String code, @PathVariable("id") long id, @RequestBody Session newSession) {
        Optional<Module> moduleOptional = repo.findById(code);
        if (moduleOptional.isPresent()) {
            Module module = moduleOptional.get();
            Optional<Session> sessionOptional = module.getSessions().stream().filter(s -> s.getId() == id).findFirst();
            if (sessionOptional.isPresent()) {
                Session currentSession = sessionOptional.get();
                currentSession.setTopic(newSession.getTopic());
                currentSession.setDatetime(newSession.getDatetime());
                currentSession.setDuration(newSession.getDuration());
                repo.save(module);
                return new ResponseEntity<>(currentSession, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ErrorInfo("Session with id " + id + " not found in module " + code),
                        HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(new ErrorInfo("Module with code " + code + " not found"),
                    HttpStatus.NOT_FOUND);
        }
    }

    /*
    Updating using patch(partially replacing)
     */

    /*
    Deleting a specific session from a specific module
     */
    /*
    session repo needed for deletingbyid
     */
    @Autowired
    SessionRepository sessionrepo;
    @DeleteMapping("/modules/{code}/sessions{id}")
    public ResponseEntity<?> deleteSessionbymodule(@PathVariable("code") String code, @PathVariable("id") long id) {

        if (repo.findById(code).isPresent()) {
            Module module = repo.findById(code).get();
            List<Session> sessions = module.getSessions();
            Session session = sessions.stream().filter(s -> s.getId() == id).findFirst().orElse(null);
            if (session != null) {
                module.getSessions().remove(session);
                sessionrepo.deleteById(id);
                return new ResponseEntity<>(new ErrorInfo("session deleted"), HttpStatus.OK);
            } else {
                return new ResponseEntity<ErrorInfo>(new ErrorInfo("Session with id " + id + " not found."),
                        HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<ErrorInfo>(new ErrorInfo("Module with code " + code + " not found."),
                    HttpStatus.NOT_FOUND);

        }

    }
}