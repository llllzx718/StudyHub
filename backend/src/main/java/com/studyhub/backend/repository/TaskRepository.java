package com.studyhub.backend.repository;

import com.studyhub.backend.model.Task;
import com.studyhub.backend.model.TaskPriority;



import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.RowMapper;

import java.util.Optional;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.time.LocalDate;

@Repository
public class TaskRepository {

    private final JdbcTemplate jdbcTemplate;

    public TaskRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //查询当前用户全部任务
    public List<Task> findAllByUserId(Long userId) {

        String sql = """
        SELECT
            id,
            text,
            done,
            user_id,
            priority,
            deadline,
            created_at
        FROM task
        WHERE user_id = ?
        ORDER BY id
        """;

        return jdbcTemplate.query(
                sql,
                taskRowMapper,
                userId
        );
    }

    //查询当前用户的某一个任务
    public Optional<Task> findByIdAndUserId(Long id, Long userId) {
        String sql = """
        SELECT
            id,
            text,
            done,
            user_id,
            priority,
            deadline,
            created_at
        FROM task
        WHERE id = ?
          AND user_id = ?
        """;

        List<Task> tasks = jdbcTemplate.query(
                sql,
                taskRowMapper,
                id,
                userId
        );

        return tasks.stream().findFirst();
    }
    //新增任务
    public Task insert(Task task) {

        String sql = """
        INSERT INTO task(
            text,
            done,
            user_id,
            priority,
            deadline
        )
        VALUES (?, ?, ?, ?, ?)
        """;

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps =
                            connection.prepareStatement(
                                    sql,
                                    Statement.RETURN_GENERATED_KEYS
                            );

                    ps.setString(1, task.text());
                    ps.setBoolean(2, task.done());
                    ps.setLong(3, task.userId());
                    ps.setString(4, task.priority().name());
                    ps.setObject(5, task.deadline());

                    return ps;
                },
                keyHolder
        );

        Long id =
                keyHolder.getKey().longValue();

        return findByIdAndUserId(
                id,
                task.userId()
        ).orElseThrow();
    }

    //更新任务
    public int update(
            Long id,
            Long userId,
            Task task
    ) {

        String sql = """
        UPDATE task
        SET text = ?,
            done = ?,
            priority = ?,
            deadline = ?
        WHERE id = ?
          AND user_id = ?
        """;

        return jdbcTemplate.update(
                sql,
                task.text(),
                task.done(),
                task.priority().name(),
                task.deadline(),
                id,
                userId
        );
    }

    //删除任务
    public int delete(
            Long id,
            Long userId
    ) {

        String sql = """
            DELETE FROM task
            WHERE id = ?
            AND user_id = ?
            """;

        return jdbcTemplate.update(
                sql,
                id,
                userId
        );
    }


    private final RowMapper<Task> taskRowMapper =
            (rs, rowNum) -> new Task(
                    rs.getLong("id"),
                    rs.getString("text"),
                    rs.getBoolean("done"),
                    rs.getLong("user_id"),

                    TaskPriority.valueOf(
                            rs.getString("priority")
                    ),

                    rs.getObject(
                            "deadline",
                            LocalDate.class
                    ),

                    rs.getTimestamp("created_at")
                            .toLocalDateTime()
            );

}
