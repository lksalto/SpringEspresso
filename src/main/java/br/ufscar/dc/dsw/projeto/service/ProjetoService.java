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
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProjetoService {

    private final ProjetoRepository projetoRepository;
    private final EstrategiaRepository estrategiaRepository;

    public ProjetoService(ProjetoRepository projetoRepository, EstrategiaRepository estrategiaRepository) {
        this.projetoRepository = projetoRepository;
        this.estrategiaRepository = estrategiaRepository;
    }

    public List<ProjetoModel> listar() { return projetoRepository.findAll(); }
    public ProjetoModel salvar(ProjetoModel projeto) { return projetoRepository.save(projeto); }
    public ProjetoModel buscar(Long id) { return projetoRepository.findById(id).orElse(null); }
    public void remover(Long id) { projetoRepository.deleteById(id); }

    public ProjetoModel salvar(ProjetoCadastroDTO dto) {
        ProjetoModel projeto = new ProjetoModel();
        projeto.setNome(dto.getNome());
        projeto.setDescricao(dto.getDescricao());

        if (dto.getEstrategiasIds() != null) {
            List<EstrategiaModel> estrategias = dto.getEstrategiasIds().stream()
                    .map(estrategiaRepository::findById)
                    .filter(java.util.Optional::isPresent)
                    .map(java.util.Optional::get)
                    .collect(Collectors.toList());
            projeto.setEstrategias(estrategias);
        }

        // Lógica para membros aqui, se necessário

        return salvar(projeto);
    }

    @Transactional
    public ProjetoModel atualizar(ProjetoEdicaoDTO dto) {
        ProjetoModel projeto = buscar(dto.getId());
        if (projeto != null) {
            projeto.setNome(dto.getNome());
            projeto.setDescricao(dto.getDescricao());

            // Atualiza estratégias
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
            
            // Lógica para membros aqui, se necessário

            return salvar(projeto);
        }
        return null;
    }

    public ProjetoModel criarProjetoComEstrategiasPadrao(String nome, String descricao) {
        ProjetoModel projeto = new ProjetoModel(nome, descricao);
        List<EstrategiaModel> estrategiasPadrao = estrategiaRepository.findAll();
        projeto.getEstrategias().addAll(estrategiasPadrao);
        return projetoRepository.save(projeto);
    }
}
