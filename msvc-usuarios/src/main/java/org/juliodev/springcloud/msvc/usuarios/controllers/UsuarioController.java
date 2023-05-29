package org.juliodev.springcloud.msvc.usuarios.controllers;


import org.juliodev.springcloud.msvc.usuarios.models.entity.Usuario;
import org.juliodev.springcloud.msvc.usuarios.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @GetMapping
    public Map<String, List<Usuario>>  listar(){
        return Collections.singletonMap("usuarios", this.service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable Long id){
        Optional<Usuario> usuarioOptional = this.service.porId(id);
        if(usuarioOptional.isPresent()){
            return ResponseEntity.ok(usuarioOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody Usuario usuario, BindingResult validationResult){


        if(validationResult.hasErrors()){
            return validar(validationResult);
        }

        if(!usuario.getEmail().isEmpty() && this.service.existePorEmail(usuario.getEmail())){
            return ResponseEntity.badRequest()
                    .body(Collections
                            .singletonMap("mensaje", "Ya existe! un usuario con ese email electronico"));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(this.service.guardar(usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@Valid @RequestBody Usuario usuario, BindingResult validationResult, @PathVariable Long id){

        if(validationResult.hasErrors()){
            return validar(validationResult);
        }
        Optional<Usuario> o = this.service.porId(id);
        if(o.isPresent()){
            Usuario usuarioDB = o.get();

            if(!usuario.getEmail().isEmpty() &&
                    !usuario.getEmail().equalsIgnoreCase(usuarioDB.getEmail())
                    && this.service.porEmail(usuario.getEmail()).isPresent()){
                return ResponseEntity.badRequest()
                        .body(Collections
                                .singletonMap("mensaje", "Ya existe un usuario con ese correo electronico"));
            }

            usuarioDB.setNombre(usuario.getNombre());
            usuarioDB.setEmail(usuario.getEmail());
            usuarioDB.setPassword(usuario.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED).body(this.service.guardar(usuarioDB));
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id){
        Optional<Usuario> o = this.service.porId(id);
        if(o.isPresent()){
            this.service.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/usuarios-por-curso")
    public ResponseEntity<?> obtenerAlumnosPorCurso(@RequestParam List<Long> ids){
        return ResponseEntity.ok(this.service.listarPorIds(ids));
    }



    private ResponseEntity<Map<String, String>> validar(BindingResult validationResult) {
        Map<String, String> errores = new HashMap();
        validationResult.getFieldErrors().forEach(fieldError -> {
            errores.put(fieldError.getField(), "El Campo " + fieldError.getField() + " " + fieldError.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errores);
    }


}
