package com.juliodev.springcloud.msvc.cursos.controllers;

import com.juliodev.springcloud.msvc.cursos.models.Usuario;
import com.juliodev.springcloud.msvc.cursos.models.entity.Curso;
import com.juliodev.springcloud.msvc.cursos.services.CursoService;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

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
        Optional<Curso> o = this.service.porIdConUsuarios(id);
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

    @PutMapping("/asignar-usuario/{cursoId}")
    public ResponseEntity<?> asignarUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId){
        Optional<Usuario> o;
        try{
            o = this.service.asignarUsuario(usuario, cursoId);
        }
        catch(FeignException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("mensaje", "No existe el usuario por" +
                            " el id o error en la comunicacion:" + e.getMessage()));
        }
        if(o.isPresent()){
            return ResponseEntity.status(HttpStatus.CREATED).body(o.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/crear-usuario/{cursoId}")
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId){
        Optional<Usuario> o;
        try{
            o = this.service.crearUsuario(usuario, cursoId);
        }
        catch(FeignException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("mensaje", "No se pudo crear el usuario" +
                            " o error en la comunicacion:" + e.getMessage()));
        }
        if(o.isPresent()){
            return ResponseEntity.status(HttpStatus.CREATED).body(o.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/eliminar-usuario/{cursoId}")
    public ResponseEntity<?> eliminarUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId){
        Optional<Usuario> o;
        try{
            o = this.service.eliminarUsuario(usuario, cursoId);
        }
        catch(FeignException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("mensaje", "No existe el usuario por" +
                            " el id o error en la comunicacion:" + e.getMessage()));
        }
        if(o.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(o.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/eliminar-curso-usuario/{id}")
    public ResponseEntity<?>eliminarCursoUsuarioPorId(@PathVariable Long id){
        this.service.eliminarCursoUsuarioPorId(id);
        return ResponseEntity.noContent().build();
    }


    private ResponseEntity<Map<String, String>> validar(BindingResult validationResult) {
        Map<String, String> errores = new HashMap();
        validationResult.getFieldErrors().forEach(fieldError -> {
            errores.put(fieldError.getField(), "El Campo " + fieldError.getField() + " " + fieldError.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errores);
    }


}
