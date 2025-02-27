package com.ecommerce.book_store.service.implement;

import com.ecommerce.book_store.http.dto.request.implement.SliderRequestDto;
import com.ecommerce.book_store.http.dto.response.implement.CategoryResponseDto;
import com.ecommerce.book_store.http.dto.response.implement.SliderResponseDto;
import com.ecommerce.book_store.persistent.entity.AbstractEntity;
import com.ecommerce.book_store.persistent.entity.Slider;
import com.ecommerce.book_store.persistent.repository.abstraction.SliderRepository;
import com.ecommerce.book_store.service.abstraction.SliderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SliderServiceImpl extends IServiceImpl<SliderRequestDto, SliderResponseDto, Slider> implements SliderService {

    private final FirebaseStorageService firebaseStorageService;
    private final SliderRepository sliderRepository;

    @Autowired
    public SliderServiceImpl(SliderRepository repository, FirebaseStorageService firebaseStorageService, SliderRepository sliderRepository) {
        super(repository);
        this.firebaseStorageService = firebaseStorageService;
        this.sliderRepository = sliderRepository;
    }

    @Override
    public Slider toEntity(SliderRequestDto requestDto) {
        // Upload slider image if exists
        String imageUrl = null;
        if (requestDto.getImage() != null && !requestDto.getImage().isEmpty()) {
            imageUrl = firebaseStorageService.uploadFile(
                    requestDto.getImage(), "sliders"
            );
        }

        // Create slider entity
        Slider result = new Slider(
                requestDto.getTitle(),
                requestDto.getDescription(),
                imageUrl,
                requestDto.getUrl()
        );

        return result;
    }

    @Override
    public SliderResponseDto toResponseDto(AbstractEntity entity) {
        Slider slider = (Slider) entity;

        return new SliderResponseDto(
                slider.getId(),
                slider.getTitle(),
                slider.getDescription(),
                slider.getImage(),
                slider.getUrl()
        );
    }

    @Override
    public void copyProperties(SliderRequestDto requestDto, Slider entity) {
        // Check if a new image is provided
        if (requestDto.getImage() != null && !requestDto.getImage().isEmpty()) {
            String imageUrl = firebaseStorageService.uploadFile(
                    requestDto.getImage(),
                    "sliders"
            );
            entity.setImage(imageUrl);
        }

        entity.setTitle(requestDto.getTitle());
        entity.setDescription(requestDto.getDescription());

        entity.setUrl(requestDto.getUrl());
    }

}