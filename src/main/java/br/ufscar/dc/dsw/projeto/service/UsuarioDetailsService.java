package br.ufscar.dc.dsw.projeto.service;

import java.util.ArrayList;

import br.ufscar.dc.dsw.projeto.model.UsuarioModel;
import br.ufscar.dc.dsw.projeto.repository.UsuarioRepository;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UsuarioModel usuario = usuarioRepository.findByEmail(email);
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuário não encontrado");
        }

        // Se você ainda não codificou a senha, use {noop} para texto puro
        return User.builder()
                   .username(usuario.getEmail())
                   .password("{noop}" + usuario.getSenha())
                   .authorities(new ArrayList<>()) // ou roles, se tiver
                   .build();
    }
}
