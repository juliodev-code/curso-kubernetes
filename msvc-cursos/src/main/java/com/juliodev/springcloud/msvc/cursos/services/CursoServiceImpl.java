package com.juliodev.springcloud.msvc.cursos.services;

import com.juliodev.springcloud.msvc.cursos.clients.UsuarioClientRest;
import com.juliodev.springcloud.msvc.cursos.models.Usuario;
import com.juliodev.springcloud.msvc.cursos.models.entity.Curso;
import com.juliodev.springcloud.msvc.cursos.models.entity.CursoUsuario;
import com.juliodev.springcloud.msvc.cursos.repositories.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    @Transactional(readOnly = true)
    public Optional<Curso> porIdConUsuarios(Long id) {
        Optional<Curso> o = this.repository.findById(id);
        if(o.isPresent()){
            Curso curso = o.get();
            if(!curso.getCursoUsuarios().isEmpty()){
                List<Long> ids = curso.getCursoUsuarios().stream()
                        .map(cu -> cu.getUsuarioId()).collect(Collectors.toList());

                List<Usuario> usuarios = this.client.obtenerAlumnosPorCurso(ids);
                curso.setUsuarios(usuarios);
            }

            return Optional.of(curso);
        }
        return Optional.empty();
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
    @Transactional
    public Optional<Usuario> asignarUsuario(Usuario usuario, Long cursoId) {
        Optional<Curso> o = this.repository.findById(cursoId);
        if(o.isPresent()){
            //we review the user with the usuario msvc
            Usuario usuarioMsvc = this.client.detalle(usuario.getId());

            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioMsvc.getId());

            Curso curso = o.get();
            curso.addCursoUsuario(cursoUsuario);

            return Optional.of(usuarioMsvc);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Usuario> crearUsuario(Usuario usuario, Long cursoId) {
        Optional<Curso> o = this.repository.findById(cursoId);
        if(o.isPresent()){
            //we review the user with the usuario msvc
            Usuario usuarioNuevoMsvc = this.client.crear(usuario);

            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioNuevoMsvc.getId());

            Curso curso = o.get();
            curso.addCursoUsuario(cursoUsuario);

            return Optional.of(usuarioNuevoMsvc);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Usuario> eliminarUsuario(Usuario usuario, Long cursoId) {
        Optional<Curso> o = this.repository.findById(cursoId);
        if(o.isPresent()){
            //we review the user with the usuario msvc
            Usuario usuarioMsvc = this.client.detalle(usuario.getId());

            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioMsvc.getId());

            Curso curso = o.get();
            curso.removeCursoUsuario(cursoUsuario);
            this.repository.save(curso);
            return Optional.of(usuarioMsvc);
        }
        return Optional.empty();
    }
}
