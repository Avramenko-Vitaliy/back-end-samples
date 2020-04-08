package com.sample.spring.tests.services;

import com.sample.spring.tests.controllers.params.Params;
import com.sample.spring.tests.converters.UsersConverter;
import com.sample.spring.tests.dtos.Pagination;
import com.sample.spring.tests.dtos.RegisterUserDto;
import com.sample.spring.tests.dtos.UserDto;
import com.sample.spring.tests.entities.User;
import com.sample.spring.tests.repositories.UsersRepository;
import com.sample.spring.tests.repositories.specifications.SpecificationBuilder;
import com.sample.spring.tests.repositories.specifications.UserSpecifications;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UsersService {

    private static final String DEFAULT_SORT = "id";
    private static final Map<String, String> MAPPED_SORTING = new HashMap<>();
    static {
        MAPPED_SORTING.put("id", "id");
        MAPPED_SORTING.put("first_name", "firstName");
        MAPPED_SORTING.put("last_name", "lastName");
    }

    private final PasswordEncoder passwordEncoder;
    private final UsersRepository usersRepository;

    public UserDto getUserById(UUID id) {
        User user = usersRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("User not found!")
        );
        return UsersConverter.toDto(user);
    }

    public UserDto registration(RegisterUserDto dto) {
        dto.setId(UUID.randomUUID());
        User user = UsersConverter.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user = usersRepository.save(user);
        return UsersConverter.toDto(user);
    }

    public Pagination<UserDto> getUsers(Params params) {
        String sort = MAPPED_SORTING.getOrDefault(params.getSort(), DEFAULT_SORT);
        params.setSort(sort);
        Specification<User> specification = new SpecificationBuilder<User>()
                .add(UserSpecifications.search(params.getSearch()))
                .build();
        Pageable pageable = makePageable(params);
        Page<User> page = usersRepository.findAll(specification, pageable);
        List<UserDto> data = page.getContent().stream()
                .map(UsersConverter::toDto)
                .collect(Collectors.toList());
        return new Pagination<>(data, page.getTotalElements());
    }

    private PageRequest makePageable(Params params) {
        Sort sort = Sort.by(params.isAsc() ? Sort.Order.asc(params.getSort()) : Sort.Order.desc(params.getSort()));
        return PageRequest.of(params.getOffset() / params.getLimit(), params.getLimit(), sort);
    }
}
