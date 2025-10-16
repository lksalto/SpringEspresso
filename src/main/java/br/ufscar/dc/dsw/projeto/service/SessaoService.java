package br.ufscar.dc.dsw.projeto.service;

import br.ufscar.dc.dsw.projeto.model.SessaoModel;
import br.ufscar.dc.dsw.projeto.repository.SessaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = false)
public class SessaoService {

    private final SessaoRepository sessaoRepository;

    public SessaoService(SessaoRepository sessaoRepository) {
        this.sessaoRepository = sessaoRepository;
    }

    public List<SessaoModel> buscarPorProjetoEEstrategia(Long projetoId, Long estrategiaId) {
        return sessaoRepository.findByProjetoIdAndEstrategiaId(projetoId, estrategiaId);
    }

    @Transactional(readOnly = true)
    public SessaoModel buscarPorId(Long id) {
        return sessaoRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public SessaoModel buscarPorIdComBugs(Long id) {
        return sessaoRepository.findByIdWithBugs(id).orElse(null);
    }

    public SessaoModel salvar(SessaoModel sessao) {
        return sessaoRepository.save(sessao);
    }
}
