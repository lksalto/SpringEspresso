package br.ufscar.dc.dsw.projeto.service;

import br.ufscar.dc.dsw.projeto.dto.ProjetoCadastroDTO;
import br.ufscar.dc.dsw.projeto.dto.ProjetoEdicaoDTO;
import br.ufscar.dc.dsw.projeto.model.EstrategiaModel;
import br.ufscar.dc.dsw.projeto.model.ProjetoModel;
import br.ufscar.dc.dsw.projeto.repository.EstrategiaRepository;
import br.ufscar.dc.dsw.projeto.repository.ProjetoRepository;
import br.ufscar.dc.dsw.projeto.model.UsuarioModel;
import br.ufscar.dc.dsw.projeto.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjetoService {

    private final ProjetoRepository projetoRepository;
    private final EstrategiaRepository estrategiaRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository; // Certifique-se de que está injetado

    public ProjetoService(ProjetoRepository projetoRepository, EstrategiaRepository estrategiaRepository) {
        this.projetoRepository = projetoRepository;
        this.estrategiaRepository = estrategiaRepository;
    }

    public List<ProjetoModel> listar() {
        return projetoRepository.findAll();
    }

    public List<ProjetoModel> listarTodos() {
        return projetoRepository.findAll();
    }

    public ProjetoModel buscar(Long id) {
        return projetoRepository.findById(id).orElse(null);
    }

    public ProjetoModel salvar(ProjetoModel projeto) {
        return projetoRepository.save(projeto);
    }

    @Transactional
    public void remover(Long id) {
        ProjetoModel projeto = projetoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Projeto não encontrado"));

        // ✅ Remove vínculos com estratégias antes de deletar o projeto
        projeto.getEstrategias().clear();
        projetoRepository.save(projeto); // atualiza o vínculo intermediário

        // ✅ Agora é seguro deletar o projeto
        projetoRepository.delete(projeto);
    }

    public void salvar(ProjetoCadastroDTO dto) {
        ProjetoModel projeto = new ProjetoModel();
        projeto.setNome(dto.getNome());
        projeto.setDescricao(dto.getDescricao());

        // Associar estratégias
        if (dto.getEstrategiasIds() != null && !dto.getEstrategiasIds().isEmpty()) {
            List<EstrategiaModel> estrategias = estrategiaRepository.findAllById(dto.getEstrategiasIds());
            projeto.setEstrategias(estrategias);
        }

        // ADICIONAR: Associar membros
        if (dto.getMembrosIds() != null && !dto.getMembrosIds().isEmpty()) {
            List<UsuarioModel> membros = usuarioRepository.findAllById(dto.getMembrosIds());
            projeto.setMembros(membros);
        }

        projetoRepository.save(projeto);
    }

    public void atualizar(ProjetoEdicaoDTO dto) {
        ProjetoModel projeto = projetoRepository.findById(dto.getId()).orElse(null);
        if (projeto != null) {
            projeto.setNome(dto.getNome());
            projeto.setDescricao(dto.getDescricao());

            // Atualizar estratégias
            if (dto.getEstrategiasIds() != null) {
                List<EstrategiaModel> estrategias = estrategiaRepository.findAllById(dto.getEstrategiasIds());
                projeto.setEstrategias(estrategias);
            } else {
                projeto.setEstrategias(new ArrayList<>());
            }

            // ADICIONAR: Atualizar membros
            if (dto.getMembrosIds() != null) {
                List<UsuarioModel> membros = usuarioRepository.findAllById(dto.getMembrosIds());
                projeto.setMembros(membros);
            } else {
                projeto.setMembros(new ArrayList<>());
            }

            projetoRepository.save(projeto);
        }
    }

    public ProjetoModel criarProjetoComEstrategiasPadrao(String nome, String descricao) {
        ProjetoModel projeto = new ProjetoModel(nome, descricao);
        List<EstrategiaModel> estrategiasPadrao = estrategiaRepository.findAll();
        projeto.getEstrategias().addAll(estrategiasPadrao);
        return projetoRepository.save(projeto);
    }


}
