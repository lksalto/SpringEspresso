package br.ufscar.dc.dsw.services;

import br.ufscar.dc.dsw.dtos.DicaDto;
import br.ufscar.dc.dsw.dtos.EstrategiaDto;
import br.ufscar.dc.dsw.dtos.ExemploDto;
import br.ufscar.dc.dsw.models.DicaModel;
import br.ufscar.dc.dsw.models.EstrategiaModel;
import br.ufscar.dc.dsw.models.ExemploModel;
import br.ufscar.dc.dsw.repositories.EstrategiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.access.prepost.PreAuthorize;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList; // Change from HashSet to ArrayList
import java.util.List;    // Change from Set to List
import java.util.UUID;

@Service
@Transactional
public class EstrategiaService {

    @Autowired
    private EstrategiaRepository estrategiaRepository;

    private final Path uploadLocation = Paths.get("src/main/resources/static/uploads/estrategias");

    public EstrategiaService() {
        try {
            Files.createDirectories(uploadLocation);
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível criar o diretório de upload.", e);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    public EstrategiaModel save(EstrategiaDto dto, List<MultipartFile> imagensExemplo) {
        // Convert DTO to Model, handling existing entities for updates
        EstrategiaModel estrategia = convertDtoToModel(dto);

        // Handle image uploads for examples
        if (imagensExemplo != null && !imagensExemplo.isEmpty()) {
            int i = 0;
            for (ExemploModel exemplo : estrategia.getExemplos()) { // Iterate through the model's examples
                if (i < imagensExemplo.size()) {
                    MultipartFile imagem = imagensExemplo.get(i);
                    if (imagem != null && !imagem.isEmpty()) { // Check for null or empty file
                        if (exemplo.getUrlImagem() != null && !exemplo.getUrlImagem().isEmpty()) {
                            deleteFile(exemplo.getUrlImagem()); // Delete old image if exists during update
                        }
                        String newFilename = saveFile(imagem);
                        exemplo.setUrlImagem(newFilename);
                    }
                }
                i++; // Increment counter for both successful and skipped image assignments
            }
        }
        return estrategiaRepository.save(estrategia);
    }

    @Transactional(readOnly = true)
    public EstrategiaModel findById(UUID id) {
        return estrategiaRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<EstrategiaModel> findAll() {
        return estrategiaRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void delete(UUID id) {
        EstrategiaModel estrategia = findById(id);
        if (estrategia != null) {
            estrategia.getExemplos().forEach(ex -> {
                if (ex.getUrlImagem() != null && !ex.getUrlImagem().isEmpty()) {
                    deleteFile(ex.getUrlImagem());
                }
            });
            estrategiaRepository.deleteById(id);
        }
    }

    private String saveFile(MultipartFile file) {
        if (file.isEmpty()) return null;
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.contains(".")) {
            throw new RuntimeException("Arquivo sem extensão não é permitido.");
        }
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFilename = UUID.randomUUID().toString() + extension;
        try {
            Path destinationFile = this.uploadLocation.resolve(newFilename).normalize().toAbsolutePath();
            Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);
            return newFilename;
        } catch (IOException e) {
            throw new RuntimeException("Falha ao salvar o arquivo.", e);
        }
    }

    private void deleteFile(String filename) {
        try {
            Path file = uploadLocation.resolve(filename);
            Files.deleteIfExists(file);
        } catch (IOException e) {
            System.err.println("ERRO ao excluir arquivo: " + filename);
        }
    }

    // Converte DTO para Model para salvar no banco
    private EstrategiaModel convertDtoToModel(EstrategiaDto dto) {
        EstrategiaModel model;
        if (dto.getId() != null) {
            // Load existing entity for updates
            model = findById(dto.getId());
            if (model == null) {
                // This case should ideally be handled before calling save with an ID
                // that doesn't exist, but defensive coding:
                model = new EstrategiaModel();
                // For new instances created here, ensure ID is null to allow generation
                model.setId(null);
            }
        } else {
            // For new entities
            model = new EstrategiaModel();
        }

        model.setNome(dto.getNome());
        model.setDescricao(dto.getDescricao());

        // --- Handle Dicas ---
        // Clear existing dicas and add new ones from DTO
        // This is crucial for handling deletions/updates of child entities
        model.getDicas().clear();
        if (dto.getDicas() != null) {
            for (DicaDto dicaDto : dto.getDicas()) {
                DicaModel dicaModel = new DicaModel();
                // ONLY set ID if it's not null, meaning it's an existing Dica being updated
                if (dicaDto.getId() != null) {
                    dicaModel.setId(dicaDto.getId());
                }
                dicaModel.setTexto(dicaDto.getDica());
                // *** CRITICAL FIX: Set the bidirectional relationship ***
                dicaModel.setEstrategia(model);
                model.getDicas().add(dicaModel);
            }
        }

        // --- Handle Exemplos ---
        // Clear existing exemplos and add new ones from DTO
        model.getExemplos().clear();
        if (dto.getExemplos() != null) {
            for (ExemploDto exemploDto : dto.getExemplos()) {
                ExemploModel exemploModel = new ExemploModel();
                // ONLY set ID if it's not null, meaning it's an existing Exemplo being updated
                if (exemploDto.getId() != null) {
                    exemploModel.setId(exemploDto.getId());
                }
                exemploModel.setTexto(exemploDto.getTexto());

                if (exemploDto.getUrlImagem() != null && !exemploDto.getUrlImagem().isEmpty()) {
                    exemploModel.setUrlImagem(exemploDto.getUrlImagem());
                }
                exemploModel.setEstrategia(model);
                model.getExemplos().add(exemploModel);
            }
        }
        return model;
    }

    // Converte Model para DTO para exibir no formulário (no changes needed here)
    public EstrategiaDto convertModelToDto(EstrategiaModel model) {
        EstrategiaDto dto = new EstrategiaDto();
        dto.setId(model.getId());
        dto.setNome(model.getNome());
        dto.setDescricao(model.getDescricao());

        List<DicaDto> dicasDto = new ArrayList<>(); // Change Set to List and HashSet to ArrayList
        if (model.getDicas() != null) {
            model.getDicas().forEach(dicaModel -> {
                DicaDto dicaDto = new DicaDto();
                dicaDto.setId(dicaModel.getId());
                dicaDto.setDica(dicaModel.getTexto());
                dicasDto.add(dicaDto);
            });
        }
        dto.setDicas(dicasDto);

        List<ExemploDto> exemplosDto = new ArrayList<>(); // Change Set to List and HashSet to ArrayList
        if (model.getExemplos() != null) {
            model.getExemplos().forEach(exemploModel -> {
                ExemploDto exemploDto = new ExemploDto();
                exemploDto.setId(exemploModel.getId());
                exemploDto.setTexto(exemploModel.getTexto());
                exemploDto.setUrlImagem(exemploModel.getUrlImagem());
                exemplosDto.add(exemploDto);
            });
        }
        dto.setExemplos(exemplosDto);

        return dto;
    }
}