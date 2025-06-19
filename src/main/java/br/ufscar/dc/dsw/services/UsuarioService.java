package br.ufscar.dc.dsw.services;

import br.ufscar.dc.dsw.repositories.UsuarioRepository;
import br.ufscar.dc.dsw.models.UsuarioModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Novas importações para Spring Security
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Para codificar a senha ao salvar

import java.util.Collections; // Para SimpleGrantedAuthority
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UsuarioService implements UserDetailsService { // Adiciona UserDetailsService

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired // Injete o PasswordEncoder aqui
    private BCryptPasswordEncoder passwordEncoder;

    public void salvar(UsuarioModel usuario) {
        Optional<UsuarioModel> existingUser = usuarioRepository.findByEmail(usuario.getEmail()); // Alterado de getLogin para getEmail
        if (usuario.getId() == null) {
            if (existingUser.isPresent()) {
                throw new IllegalArgumentException("email.error.duplicate"); // Alterado de login para email
            }
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        } else {
            if (existingUser.isPresent() && !existingUser.get().getId().equals(usuario.getId())) {
                throw new IllegalArgumentException("email.error.duplicate.anotherUser"); // Alterado de login para email
            }
            if (usuario.getSenha() != null && !usuario.getSenha().isEmpty()) {
                usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
            } else {
                UsuarioModel original = usuarioRepository.findById(usuario.getId())
                        .orElseThrow(() -> new IllegalArgumentException("user.error.notFoundById"));
                usuario.setSenha(original.getSenha());
            }
        }

        try {
            usuarioRepository.save(usuario);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("user.error.dataIntegrityViolation", e);
        }
    }

    public void excluir(UUID id) {
        if (!usuarioRepository.existsById(id)) {
            throw new IllegalArgumentException("user.error.notFound");
        }
        try {
            usuarioRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("user.error.deletionFailedDependencies", e);
        }
    }

    @Transactional(readOnly = true)
    public UsuarioModel buscarPorId(UUID id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("user.error.notFoundById"));
    }

    @Transactional(readOnly = true)
    public List<UsuarioModel> buscarTodos() {
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public UsuarioModel buscarPorLogin(String login) {
        return usuarioRepository.findByEmail(login)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        UsuarioModel usuario = usuarioRepository.findByEmail(login)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + login));

        // Retorna um objeto UserDetails do Spring Security
        return new org.springframework.security.core.userdetails.User(
                usuario.getEmail(),
                usuario.getSenha(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + usuario.getPapel().toString().toUpperCase()))
        );
    }
}