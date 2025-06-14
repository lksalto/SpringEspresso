package br.ufscar.dc.dsw.services;

import br.ufscar.dc.dsw.dtos.LivroRecordDto;
import br.ufscar.dc.dsw.models.LivroModel;
import br.ufscar.dc.dsw.models.ReviewModel;
import br.ufscar.dc.dsw.repositories.AutorRepository;
import br.ufscar.dc.dsw.repositories.LivroRepository;
import br.ufscar.dc.dsw.repositories.PublisherRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class LivroService {
    private final LivroRepository livroRepository;
    private final AutorRepository autorRepository;
    private final PublisherRepository publisherRepository;

    public LivroService(
            LivroRepository livroRepository,
            AutorRepository autorRepository,
            PublisherRepository publisherRepository
    ) {
        this.livroRepository = livroRepository;
        this.autorRepository = autorRepository;
        this.publisherRepository = publisherRepository;
    }

    public List<LivroModel> getAllLivros() {
        return livroRepository.findAll();
    }

    @Transactional
    public LivroModel saveLivro(LivroRecordDto livroRecordDto) {
        LivroModel livro = new LivroModel();
        livro.setTitulo(livroRecordDto.titulo());
        livro.setPublisher(publisherRepository.findById(livroRecordDto.publisherId()).get());
        livro.setAutores(autorRepository.findAllById(livroRecordDto.autorIds()).stream().collect(Collectors.toSet()));

        ReviewModel reviewModel = new ReviewModel();
        reviewModel.setComentario(livroRecordDto.reviewComentario());
        reviewModel.setLivro(livro);
        livro.setReview(reviewModel);

        return livroRepository.save(livro);
    }

    @Transactional
    public void deleteLivro(UUID id) {
        livroRepository.deleteById(id);
    }
}
