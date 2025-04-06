package com.ecommerce.book_store.http.dto.response.implement;

import com.ecommerce.book_store.http.dto.response.AbstractResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RibbonResponseDto extends AbstractResponseDto {
    private Long id;
    private String name;
    private String description;
    private boolean status;
    private List<RibbonItemResponseDto> ribbonItems;
}
