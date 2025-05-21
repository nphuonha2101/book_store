package com.ecommerce.book_store.service.implement;

import com.ecommerce.book_store.http.dto.request.implement.RoleRequestDto;
import com.ecommerce.book_store.http.dto.response.implement.RoleResponseDto;
import com.ecommerce.book_store.persistent.entity.AbstractEntity;
import com.ecommerce.book_store.persistent.entity.Role;
import com.ecommerce.book_store.persistent.repository.abstraction.RoleRepository;
import com.ecommerce.book_store.service.abstraction.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends IServiceImpl<RoleRequestDto, RoleResponseDto, Role>
                    implements RoleService {
    public RoleServiceImpl(RoleRepository roleRepository) {
        super(roleRepository);
    }

    @Override
    public Role toEntity(RoleRequestDto requestDto) {
        return null;

    }

    @Override
    public RoleResponseDto toResponseDto(AbstractEntity entity) {
        return null;
    }

    @Override
    public void copyProperties(RoleRequestDto requestDto, Role entity) {

    }
}
