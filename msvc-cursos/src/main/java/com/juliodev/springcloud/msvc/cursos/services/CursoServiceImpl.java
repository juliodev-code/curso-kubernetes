package com.juliodev.springcloud.msvc.cursos.services;

import com.juliodev.springcloud.msvc.cursos.clients.UsuarioClientRest;
import com.juliodev.springcloud.msvc.cursos.models.Usuario;
import com.juliodev.springcloud.msvc.cursos.models.entity.Curso;
import com.juliodev.springcloud.msvc.cursos.repositories.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CursoServiceImpl implements CursoService {

    @Autowired
    private CursoRepository repository;

    @Autowired
    private UsuarioClientRest client;

    @Override
    @Transactional(readOnly = true)
    public List<Curso> listar() {
        return (List<Curso>) this.repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Curso> porId(Long id) {
        return this.repository.findById(id);
    }

    @Override
    @Transactional
    public Curso guardar(Curso curso) {
        return this.repository.save(curso);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        this.repository.deleteById(id);
    }

    @Override
    public Optional<Usuario> asignarUsuario(Usuario usuario, Long cursoId) {
        return Optional.empty();
    }

    @Override
    public Optional<Usuario> crearUsuario(Usuario usuario, Long cursoId) {
        return Optional.empty();
    }

    @Override
    public Optional<Usuario> eliminarUsuario(Usuario usuario, Long cursoId) {
        return Optional.empty();
    }
}
