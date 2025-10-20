package br.ufscar.dc.dsw.projeto.service;

import br.ufscar.dc.dsw.projeto.model.BugModel;
import br.ufscar.dc.dsw.projeto.model.SessaoModel;
import br.ufscar.dc.dsw.projeto.repository.BugRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = false)
public class BugService {

    private final BugRepository bugRepository;

    public BugService(BugRepository bugRepository) {
        this.bugRepository = bugRepository;
    }

    public BugModel salvar(BugModel bug) {
        return bugRepository.save(bug);
    }

    @Transactional(readOnly = true)
    public BugModel buscarPorId(Long id) {
        return bugRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<BugModel> buscarPorSessaoId(Long sessaoId) {
        return bugRepository.findBySessaoId(sessaoId);
    }

    public void deletar(Long id) {
        bugRepository.deleteById(id);
    }

    @Transactional
    public void excluirBug(Long id) {
        BugModel bug = bugRepository.findById(id).orElseThrow(() -> 
                new RuntimeException("Bug não encontrado: " + id));
        SessaoModel sessao = bug.getSessao();
        if (sessao != null) {
            sessao.getBugs().remove(bug); // Remove da lista da sessão
        }
        bugRepository.delete(bug); // Depois de removido da sessão, pode deletar
    }

}