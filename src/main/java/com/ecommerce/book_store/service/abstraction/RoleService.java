package com.ecommerce.book_store.service.abstraction;

import com.ecommerce.book_store.http.dto.request.implement.RoleRequestDto;
import com.ecommerce.book_store.http.dto.response.implement.RoleResponseDto;
import com.ecommerce.book_store.persistent.entity.Role;

public interface RoleService extends IService<RoleRequestDto, RoleResponseDto, Role>{
}
