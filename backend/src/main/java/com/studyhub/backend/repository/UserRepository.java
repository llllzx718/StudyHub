package com.studyhub.backend.repository;

import com.studyhub.backend.model.User;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<User> userRowMapper =
            (rs, rowNum) -> new User(
                    rs.getLong("id"),
                    rs.getString("username"),
                    rs.getString("password_hash")
            );

    public Optional<User> findByUsername(String username) {

        String sql = """
                SELECT id, username,password_hash
                FROM app_user
                WHERE username = ?
                """;

        List<User> users = jdbcTemplate.query(
                sql,
                userRowMapper,
                username
        );

        return users.stream().findFirst();
    }

    public Optional<User> findById(Long id) {

        String sql = """
            SELECT id, username, password_hash
            FROM app_user
            WHERE id = ?
            """;

        List<User> users = jdbcTemplate.query(
                sql,
                userRowMapper,
                id
        );

        return users.stream().findFirst();
    }

    public User insert(String username,String passwordHash) {

        String sql = """
                INSERT INTO app_user(username,password_hash)
                VALUES (?,?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
                connection -> {

                    PreparedStatement ps =
                            connection.prepareStatement(
                                    sql,
                                    Statement.RETURN_GENERATED_KEYS
                            );

                    ps.setString(1, username);
                    ps.setString(2, passwordHash);

                    return ps;
                },
                keyHolder
        );

        Long id = keyHolder.getKey().longValue();

        return new User(
                id,
                username,
                passwordHash
        );
    }
}