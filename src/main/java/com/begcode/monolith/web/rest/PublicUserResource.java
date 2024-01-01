package com.begcode.monolith.web.rest;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.begcode.monolith.service.UserQueryService;
import com.begcode.monolith.service.UserService;
import com.begcode.monolith.service.criteria.UserCriteria;
import com.begcode.monolith.service.dto.UserDTO;
import com.begcode.monolith.service.mapper.UserMapper;
import com.begcode.monolith.util.web.IPageUtil;
import com.begcode.monolith.util.web.PageableUtils;
import java.util.*;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api")
public class PublicUserResource {

    private static final List<String> ALLOWED_ORDERED_PROPERTIES = Collections.unmodifiableList(
        Arrays.asList("id", "login", "firstName", "lastName", "email", "activated", "langKey")
    );

    private final Logger log = LoggerFactory.getLogger(PublicUserResource.class);

    private final UserService userService;

    private final UserQueryService userQueryService;

    private final UserMapper userMapper;

    public PublicUserResource(UserService userService, UserQueryService userQueryService, UserMapper userMapper) {
        this.userService = userService;
        this.userQueryService = userQueryService;
        this.userMapper = userMapper;
    }

    /**
     * {@code GET /users} : get all users with only public information - calling this method is allowed for anyone.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body all users.
     */
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllPublicUsers(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        UserCriteria criteria
    ) {
        log.debug("REST request to get all public User names");

        // final Page<UserDTO> page = userService.getAllPublicUsers(pageable);
        final IPage<UserDTO> page = userQueryService.findByCriteria(criteria, PageableUtils.toPage(pageable));
        HttpHeaders headers = IPageUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getRecords(), headers, HttpStatus.OK);
    }

    private boolean onlyContainsAllowedProperties(Pageable pageable) {
        return pageable.getSort().stream().map(Sort.Order::getProperty).allMatch(ALLOWED_ORDERED_PROPERTIES::contains);
    }

    /**
     * {@code GET /users/check} : check user exist.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body all users.
     */
    @GetMapping("/users/check")
    public ResponseEntity<Boolean> existUser(UserCriteria criteria) {
        log.debug("REST request to get all public User names");
        if (criteria == null) {
            return ResponseEntity.ok(false);
        }
        UserCriteria newCriteria = new UserCriteria();
        newCriteria.setLogin(criteria.getLogin());
        newCriteria.setMobile(criteria.getMobile());
        newCriteria.setEmail(criteria.getEmail());
        newCriteria.setId(criteria.getId());
        long count = userQueryService.countByCriteria(newCriteria);
        return ResponseEntity.ok(count > 0);
    }
}
