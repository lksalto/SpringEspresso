package br.ufscar.dc.dsw.projeto.service;

import br.ufscar.dc.dsw.projeto.model.UsuarioModel;
import br.ufscar.dc.dsw.projeto.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = false)
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public List<UsuarioModel> listarTodos() {
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public UsuarioModel buscarPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }
}
