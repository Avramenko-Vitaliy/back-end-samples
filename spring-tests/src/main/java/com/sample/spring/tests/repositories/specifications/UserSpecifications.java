package com.sample.spring.tests.repositories.specifications;

import com.sample.spring.tests.entities.User;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

@UtilityClass
public class UserSpecifications {

    public Specification<User> search(String search) {
        if (StringUtils.isEmpty(search)) {
            return null;
        }
        return (root, criteriaQuery, cb) ->
                cb.or(
                        cb.like(cb.lower(root.get("firstName")), "%%" + search + "%%"),
                        cb.like(cb.lower(root.get("lastName")), "%%" + search + "%%")
                );
    }
}
