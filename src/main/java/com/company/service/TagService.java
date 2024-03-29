package com.company.service;

import com.company.dto.TagDTO;
import com.company.entity.TagEntity;
import com.company.exceptions.BadRequestException;
import com.company.exceptions.ItemAlreadyExistsException;
import com.company.exceptions.ItemNotFoundException;
import com.company.repository.TagRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.LifecycleState;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
@Slf4j
public class TagService {
    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public String create(TagDTO dto) {
        if (tagRepository.existsByNameAndVisible(dto.getName(),Boolean.TRUE)) {
            log.error("Tag already exists!");
            throw new ItemAlreadyExistsException(
                    "Tag already exists"
            );
        }

        TagEntity entity = toEntity(dto);

        tagRepository.save(entity);

        return "Successfully saved a tag";

    }

    public String update(TagDTO dto,Integer id) {
        if (!tagRepository.existsById(id)) {
            log.error("Tag not found!");
            throw new ItemNotFoundException(
                    "Tag not found!"
            );
        }

        if (tagRepository.existsByNameAndVisible(dto.getName(),Boolean.TRUE)) {
            log.error("Tag already exists");
            throw new ItemAlreadyExistsException(
                    "Tag already exists"
            );
        }

        tagRepository.update(dto.getName(),id);

        return "Successfully updated a tag";

    }

    public String delete(Integer id) {
        if (!tagRepository.existsByIdAndVisible(id,Boolean.TRUE)) {
            log.error("Tag not found!");
            throw new ItemNotFoundException(
                    "Tag not found!"
            );
        }

        tagRepository.updateVisible(Boolean.FALSE,id);

        return "Successfully deleted a tag";

    }

    public List<TagDTO> list() {
        List<TagEntity> entities = tagRepository.findAllByVisible(Boolean.TRUE);

        if (entities.size() == 0) {
            log.error("No tags yet");
            throw new BadRequestException(
                    "No tags yet"
            );
        }

        List<TagDTO> dtoList = new LinkedList<>();

        entities.forEach(tagEntity -> {
            TagDTO dto = toDTO(tagEntity);

            dtoList.add(dto);
        });

        return dtoList;

    }

    private TagEntity toEntity(TagDTO dto) {
        TagEntity entity = new TagEntity();
        entity.setName(dto.getName());

        return entity;
    }

    private TagDTO toDTO(TagEntity entity) {
        TagDTO dto = new TagDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());

        return dto;
    }

    public boolean exists(Integer id) {
        return tagRepository.existsByIdAndVisible(id,Boolean.TRUE);
    }

    public boolean exists(String tagName) {
        return tagRepository.existsByNameAndVisible(tagName,Boolean.TRUE);
    }

    public TagEntity find(String tagName) {
        return tagRepository.findByNameAndVisible(tagName,Boolean.TRUE)
                .orElseThrow(() ->
                        new ItemNotFoundException(
                                "Tag not found!"));
    }

    public List<TagDTO> getTagListByVideoId(String videoId) {
        List<TagEntity> list = tagRepository.getTagListByVideoId(videoId);

        List<TagDTO> dtoList = new LinkedList<>();

        list.forEach(entity -> {
            TagDTO dto = toDTO(entity);

            dtoList.add(dto);
        });

        return dtoList;
    }
}
