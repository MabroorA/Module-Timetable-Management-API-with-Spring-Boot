package edu.leicester.co2103.controller;


import edu.leicester.co2103.ErrorInfo;
import edu.leicester.co2103.domain.Module;
import edu.leicester.co2103.repo.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
public class ModuleRestController {
    @Autowired
    ModuleRepository repo;
    /*
    Retrieve all modules (endpoint #7)
     */
    @GetMapping("/modules")
    public ResponseEntity<List<Module>> listAllModules(){
        List<Module> modules = repo.findAll();
        if (modules.isEmpty()) {
            return new ResponseEntity<List<Module>>(HttpStatus.NO_CONTENT);

        }else
            return new ResponseEntity<List<Module>>(HttpStatus.OK);
    }
    /*
    Post for a Module
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
}
