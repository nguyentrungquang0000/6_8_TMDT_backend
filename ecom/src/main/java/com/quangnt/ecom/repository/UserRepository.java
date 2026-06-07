package com.quangnt.ecom.repository;

import com.quangnt.ecom.dto.Role;
import com.quangnt.ecom.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    @Query("SELECT u FROM User u WHERE LOWER(u.email) = LOWER(:email)")
    Optional<User> findByEmail(@Param("email") String email);

    @Query("""
        SELECT u
        FROM User u
        WHERE u.isDeleted = false
            AND (:#{@tenantProvider.getTenantId()} IS NULL OR u.cinemaId = :#{@tenantProvider.getTenantId()})
            AND (:role IS NULL OR u.role = :role)
            AND (:lock IS NULL OR u.isLock = :lock)
            AND (
                :keyword IS NULL
                OR :keyword = ''
                OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(u.phone) LIKE LOWER(CONCAT('%', :keyword, '%'))
            )
    """)
    Page<User> search(
            @Param("keyword") String keyword,
            @Param("role") Role role,
            @Param("lock") Boolean lock,
            Pageable pageable
    );
}
