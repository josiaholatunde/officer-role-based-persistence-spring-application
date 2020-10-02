package com.persistence.demo.dao;

import com.persistence.demo.entities.Officer;
import com.persistence.demo.entities.Rank;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@SuppressWarnings({ "SqlResolve", "SqlNoDataSourceInspection", "ConstantConditions"})
public class JdbcOfficerDao implements  OfficerDAO {

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert insertOfficer;
    private RowMapper<Officer> officerMapper = (rs, rowNum) -> new Officer(rs.getInt("id"),
            Rank.valueOf(rs.getString("rank")),
            rs.getString("first_name"), rs.getString("last_name"));

    public JdbcOfficerDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        insertOfficer = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("officers")
                .usingGeneratedKeyColumns("id");
    }
    @Override
    public long count() {
        return jdbcTemplate.queryForObject("select count(*) from officers", Long.class);
    }

    @Override
    public Officer save(Officer officer) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("rank", officer.getRank());
        parameters.put("first_name", officer.getFirstName());
        parameters.put("last_name", officer.getLastName());
        Integer id = (Integer) insertOfficer.executeAndReturnKey(parameters);
        officer.setId(id);
        return officer;
    }

    @Override
    public List<Officer> findAll() {
        return jdbcTemplate.query("select * from officers",
                officerMapper);
    }

    @Override
    public Optional<Officer> findById(Integer id) {
        if (id == null || !existsById(id)) return Optional.empty();
        return Optional.ofNullable(jdbcTemplate.queryForObject("select * from officers where id=?", officerMapper
                , id));
    }

    @Override
    public void delete(Officer officer) {
        int count = jdbcTemplate.update("delete from officers where id=?", officer.getId());
        // check if deletion occurred
    }

    @Override
    public boolean existsById(Integer id) {
        return jdbcTemplate.queryForObject("select EXISTS(select * from officers where id=?)", Boolean.class, id);
    }
}
