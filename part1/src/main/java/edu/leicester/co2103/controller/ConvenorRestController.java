package edu.leicester.co2103.controller;


import edu.leicester.co2103.ErrorInfo;
import edu.leicester.co2103.domain.Convenor;
import edu.leicester.co2103.repo.ConvenorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
public class ConvenorRestController {
    @Autowired
    ConvenorRepository repo;
    /*
    Retrieve all Conveners
     */
    @GetMapping("/convenors")
    public ResponseEntity<List<Convenor>> listAllConvenors(){
        List<Convenor> convenors = repo.findAll();
        if (convenors.isEmpty()) {
            return new ResponseEntity<List<Convenor>>(HttpStatus.NO_CONTENT);

        }else
            return new ResponseEntity<List<Convenor>>(convenors,HttpStatus.OK);
    }
    /*
        Creating a new covenor
     */
    @PostMapping("/convenors")
    public ResponseEntity<?> createConvenor(@RequestBody Convenor convenor, UriComponentsBuilder ucBuilder){
        if (repo.existsById(convenor.getId())) {
            return new ResponseEntity<ErrorInfo>(new ErrorInfo("A convenor named " + convenor.getName() + " already exists."),
                    HttpStatus.CONFLICT);

        }
        repo.save(convenor);
            HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/convenor/{id}").buildAndExpand(convenor.getId()).toUri());
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
    /*
        Updating a Convenor
     */
    @PutMapping("/convenors")
    public ResponseEntity<?> updateConvenor(@PathVariable("id") long id, @RequestBody Convenor newConvenor) {
        if (repo.findById(id).isPresent()) {
            Convenor currentConvenor = repo.findById(id).get();
            currentConvenor.setName(newConvenor.getName());
            currentConvenor.setPosition(newConvenor.getPosition());
            currentConvenor.setModules(newConvenor.getModules());


            currentConvenor.getModules().clear();
            currentConvenor.getModules().addAll(newConvenor.getModules());

            repo.save(currentConvenor);
            return new ResponseEntity<Convenor>(currentConvenor, HttpStatus.OK);
        } else
            return new ResponseEntity<ErrorInfo>(new ErrorInfo("Convenor with id " + id + " not found."),
                    HttpStatus.NOT_FOUND);
    }
    /*
    Get a specific Convenor
     */
    @GetMapping("/convenors/{id}")
    public ResponseEntity<?> getConvenor(@PathVariable("id") long id) {

        if (repo.findById(id).isPresent()) {
            Convenor convenor = repo.findById(id).get();
            return new ResponseEntity<Convenor>(convenor, HttpStatus.OK);
        } else
            return new ResponseEntity<ErrorInfo>(new ErrorInfo("Convenor with id " + id + " not found"),
                    HttpStatus.NOT_FOUND);
    }
    /*
    Delete a certain convenor
     */
    @DeleteMapping("/Convenor/{id}")
    public ResponseEntity<?> deleteConvenor(@PathVariable("id") long id) {

        if (repo.findById(id).isPresent()) {
            repo.deleteById(id);
            return ResponseEntity.ok(null);
        } else
            return new ResponseEntity<ErrorInfo>(new ErrorInfo("Convenor with id " + id + " not found."),
                    HttpStatus.NOT_FOUND);

    }
    /*
    List all modules taught by a convenor
    endpoint #6)
     */
//    @GetMapping("/convenors/{id}/modules")
//    public ResponseEntity<?> getConvenor(@PathVariable("id") long id) {
//
//        if (repo.findById(id).isPresent()) {
//            Convenor convenor = repo.findById(id).get();
//            return new ResponseEntity<Convenor>(convenor, HttpStatus.OK);
//        } else
//            return new ResponseEntity<ErrorInfo>(new ErrorInfo("Convenor with id " + id + " not found"),
//                    HttpStatus.NOT_FOUND);
//    }

}
