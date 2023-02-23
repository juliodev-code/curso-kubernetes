package org.juliodev.springcloud.msvc.usuarios.services;

import org.juliodev.springcloud.msvc.usuarios.clients.CursoClienteRest;
import org.juliodev.springcloud.msvc.usuarios.models.entity.Usuario;
import org.juliodev.springcloud.msvc.usuarios.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoClienteRest client;

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listar() {
        return (List<Usuario>) this.usuarioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> porId(Long id) {
        return this.usuarioRepository.findById(id);
    }

    @Override
    @Transactional
    public Usuario guardar(Usuario usuario) {
        return this.usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        this.usuarioRepository.deleteById(id);
        this.client.eliminarCursoUsuarioPorId(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarPorIds(Iterable<Long> ids) {
        return (List<Usuario>) this.usuarioRepository.findAllById(ids);
    }

    @Override
    public Optional<Usuario> porEmail(String email) {
        return this.usuarioRepository.porEmail(email);
    }

    @Override
    public boolean existePorEmail(String email) {
        return this.usuarioRepository.existsByEmail(email);
    }
}
