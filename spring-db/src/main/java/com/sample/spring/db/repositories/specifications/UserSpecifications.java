package com.sample.spring.db.repositories.specifications;

import com.sample.spring.db.entities.User;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class UserSpecifications {

    public Specification<User> search(String search) {
        if (StringUtils.isBlank(search)) {
            return null;
        }
        return (root, criteriaQuery, cb) ->
                cb.or(
                        cb.like(cb.lower(root.get("firstName")), "%%" + search + "%%"),
                        cb.like(cb.lower(root.get("lastName")), "%%" + search + "%%")
                );
    }
}
