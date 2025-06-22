package br.ufscar.dc.dsw.services;

import br.ufscar.dc.dsw.dtos.ProjetoCadastroDTO;
import br.ufscar.dc.dsw.dtos.ProjetoDTO;
import br.ufscar.dc.dsw.dtos.ProjetoEdicaoDTO;
import br.ufscar.dc.dsw.models.ProjetoModel;
import br.ufscar.dc.dsw.models.UsuarioModel;
import br.ufscar.dc.dsw.repositories.ProjetoRepository;
import br.ufscar.dc.dsw.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProjetoService {

    @Autowired
    private ProjetoRepository projetoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    public ProjetoDTO salvar(ProjetoCadastroDTO dto) {
        ProjetoModel projeto = new ProjetoModel();
        projeto.setNome(dto.nome());
        projeto.setDescricao(dto.descricao());
        projeto.setMembros(buscarMembros(dto.membrosIds()));
        ProjetoModel salvo = projetoRepository.save(projeto);
        return converterParaDTO(salvo);
    }

    public ProjetoDTO atualizar(ProjetoEdicaoDTO dto) {
        ProjetoModel projeto = projetoRepository.findById(dto.id())
                .orElseThrow(() -> new RuntimeException("Projeto não encontrado."));
        projeto.setNome(dto.nome());
        projeto.setDescricao(dto.descricao());
        projeto.setMembros(buscarMembros(dto.membrosIds()));
        ProjetoModel atualizado = projetoRepository.save(projeto);
        return converterParaDTO(atualizado);
    }

    public void excluir(UUID id) {
        projetoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<ProjetoDTO> listarParaUsuarioLogado() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UsuarioModel usuarioLogado = usuarioService.buscarPorEmail(email);
        if (usuarioLogado.getPapel().name().equals("ADMIN")) {
            return projetoRepository.findAll().stream().map(this::converterParaDTO).toList();
        }
        return projetoRepository.findAllByMembro(usuarioLogado).stream()
                .map(this::converterParaDTO)
                .toList();

    }

    @Transactional(readOnly = true)
    public ProjetoDTO buscarPorId(UUID id) {
        ProjetoModel projeto = projetoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Projeto não encontrado."));
        return converterParaDTO(projeto);
    }


    private Set<UsuarioModel> buscarMembros(List<UUID> ids) {
        if (ids == null) return new HashSet<>();
        return ids.stream()
                .map(id -> usuarioRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Usuário não encontrado.")))
                .collect(Collectors.toSet());
    }

    private ProjetoDTO converterParaDTO(ProjetoModel projeto) {
        List<String> nomes = projeto.getMembros().stream()
                .map(UsuarioModel::getNome)
                .toList();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean usuarioLogadoEhMembro = false;
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            UsuarioModel usuarioLogado = usuarioService.buscarPorEmail(authentication.getName());
            usuarioLogadoEhMembro = projeto.getMembros().contains(usuarioLogado);
        }
        return new ProjetoDTO(
                projeto.getId(),
                projeto.getNome(),
                projeto.getDescricao(),
                projeto.getDataCriacao(),
                nomes,
                usuarioLogadoEhMembro
        );
    }
}
