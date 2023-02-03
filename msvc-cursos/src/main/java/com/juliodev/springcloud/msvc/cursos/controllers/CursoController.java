package com.juliodev.springcloud.msvc.cursos.controllers;

import com.juliodev.springcloud.msvc.cursos.models.entity.Curso;
import com.juliodev.springcloud.msvc.cursos.services.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class CursoController {

    @Autowired
    private CursoService service;

    @GetMapping
    public ResponseEntity<List<Curso>> listar() {
        return ResponseEntity.ok(this.service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable Long id){
        Optional<Curso> o = service.porId(id);
        if(o.isPresent()){
            return ResponseEntity.ok(o.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/")
    public ResponseEntity<?> crear(@Valid @RequestBody Curso curso, BindingResult validationResult){
        if(validationResult.hasErrors()){
            return validar(validationResult);
        }
        Curso cursoDb = this.service.guardar(curso);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cursoDb);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?>editar(@Valid @RequestBody Curso curso, BindingResult validationResult, @PathVariable Long id){

        if(validationResult.hasErrors()){
            return validar(validationResult);
        }

        Optional<Curso> o = this.service.porId(id);
        if(o.isPresent()){
            Curso cursoDb = o.get();
            cursoDb.setNombre(curso.getNombre());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(this.service.guardar(cursoDb));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id){
        Optional<Curso> o = this.service.porId(id);
        if(o.isPresent()){
            this.service.eliminar(o.get().getId());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private ResponseEntity<Map<String, String>> validar(BindingResult validationResult) {
        Map<String, String> errores = new HashMap();
        validationResult.getFieldErrors().forEach(fieldError -> {
            errores.put(fieldError.getField(), "El Campo " + fieldError.getField() + " " + fieldError.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errores);
    }


}
