package br.ufscar.dc.dsw.projeto.service;

import br.ufscar.dc.dsw.projeto.dto.ProjetoCadastroDTO;
import br.ufscar.dc.dsw.projeto.dto.ProjetoEdicaoDTO;
import br.ufscar.dc.dsw.projeto.model.EstrategiaModel;
import br.ufscar.dc.dsw.projeto.model.ProjetoModel;
import br.ufscar.dc.dsw.projeto.repository.EstrategiaRepository;
import br.ufscar.dc.dsw.projeto.repository.ProjetoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjetoService {

    private final ProjetoRepository projetoRepository;
    private final EstrategiaRepository estrategiaRepository;

    public ProjetoService(ProjetoRepository projetoRepository, EstrategiaRepository estrategiaRepository) {
        this.projetoRepository = projetoRepository;
        this.estrategiaRepository = estrategiaRepository;
    }

    public List<ProjetoModel> listar() {
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

    public ProjetoModel salvar(ProjetoCadastroDTO dto) {
        ProjetoModel projeto = new ProjetoModel();
        projeto.setNome(dto.getNome());
        projeto.setDescricao(dto.getDescricao());

        if (dto.getEstrategiasIds() != null && !dto.getEstrategiasIds().isEmpty()) {
            List<EstrategiaModel> estrategias = dto.getEstrategiasIds().stream()
                    .map(estrategiaRepository::findById)
                    .filter(java.util.Optional::isPresent)
                    .map(java.util.Optional::get)
                    .collect(Collectors.toList());
            projeto.setEstrategias(estrategias);
        }

        return salvar(projeto);
    }

    @Transactional
    public ProjetoModel atualizar(ProjetoEdicaoDTO dto) {
        ProjetoModel projeto = buscar(dto.getId());
        if (projeto == null) {
            throw new RuntimeException("Projeto não encontrado");
        }

        projeto.setNome(dto.getNome());
        projeto.setDescricao(dto.getDescricao());

        if (dto.getEstrategiasIds() != null) {
            List<EstrategiaModel> estrategias = dto.getEstrategiasIds().stream()
                    .map(estrategiaRepository::findById)
                    .filter(java.util.Optional::isPresent)
                    .map(java.util.Optional::get)
                    .collect(Collectors.toList());
            projeto.setEstrategias(estrategias);
        } else {
            projeto.getEstrategias().clear();
        }

        return salvar(projeto);
    }

    public ProjetoModel criarProjetoComEstrategiasPadrao(String nome, String descricao) {
        ProjetoModel projeto = new ProjetoModel(nome, descricao);
        List<EstrategiaModel> estrategiasPadrao = estrategiaRepository.findAll();
        projeto.getEstrategias().addAll(estrategiasPadrao);
        return projetoRepository.save(projeto);
    }
}
