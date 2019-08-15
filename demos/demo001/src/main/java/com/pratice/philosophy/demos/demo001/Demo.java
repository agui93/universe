package com.pratice.philosophy.demos.demo001;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Demo {

    private static void test001() {
        RowMapper<Actor> rowMapper = (rs, rowNum) -> {
            Actor actor1 = new Actor();
            actor1.setId(rs.getInt("id"));
            actor1.setFirstName(rs.getString("first_name"));
            actor1.setLastName(rs.getString("last_name"));
            return actor1;
        };
        JdbcTemplate jdbcTemplate = JdbcManager.INSTANCE.fetchJdbcTemplate();


        jdbcTemplate.execute("drop table  if  exists t_actor");
        String sql_createTable = "create table t_actor (id Integer , first_name varchar(100),last_name varchar (100));";
        jdbcTemplate.execute(sql_createTable);
        System.out.println();
        System.out.println(sql_createTable);


        System.out.println();
        String sql_select = "select id,first_name, last_name from t_actor";
        List<Actor> actors = jdbcTemplate.query(sql_select, rowMapper);
        System.out.println(sql_select + "#####" + actors);


        System.out.println();
        String sql_insert = "insert into t_actor (id, first_name, last_name) values (?, ?, ?)";
        jdbcTemplate.update(sql_insert, 1212L, "Joe", "Watling");
        System.out.println(sql_insert + "#####" + 1212L + ";Joe" + ";Watling");


        System.out.println();
        sql_select = "select count(*) from t_actor";
        int rowCount = jdbcTemplate.queryForObject(sql_select, Integer.class);
        System.out.println(sql_select + "#####" + rowCount);


        System.out.println();
        sql_select = "select count(*) from t_actor where first_name = ?";
        int countOfActorsNamedJoe = jdbcTemplate.queryForObject(sql_select, Integer.class, "Joe");
        System.out.println(sql_select + "#####" + "Joe" + "#####" + countOfActorsNamedJoe);


        System.out.println();
        sql_select = "select last_name from t_actor where id = ?";
        String lastName = jdbcTemplate.queryForObject(sql_select, new Object[]{1212L}, String.class);
        System.out.println(sql_select + "#####" + 1212 + "#####" + lastName);


        System.out.println();
        sql_select = "select first_name, last_name from t_actor where id = ?";
        Actor actor = jdbcTemplate.queryForObject(sql_select, new Object[]{1212L}, ((rs, rowNum) -> {
            Actor actor1 = new Actor();
            actor1.setFirstName(rs.getString("first_name"));
            actor1.setLastName(rs.getString("last_name"));
            return actor1;
        }));
        System.out.println(sql_select + "#####" + 1212 + "#####" + actor);


        System.out.println();
        sql_insert = "insert into t_actor (id, first_name, last_name) values (?, ?, ?)";
        jdbcTemplate.update(sql_insert, 1313L, "John", "Watling");
        System.out.println(sql_insert + "#####" + 1313L + ";John" + ";Watling");


        System.out.println();
        sql_select = "select id,first_name, last_name from t_actor";
        actors = jdbcTemplate.query(sql_select, rowMapper);
        System.out.println(sql_select + "#####" + actors);


        System.out.println();
        String sql_update = "update t_actor set last_name = ? where id = ?";
        jdbcTemplate.update(sql_update, "Banjo", 1313L);
        System.out.println(sql_update + "#####" + "Banjo" + ";" + 1313L);


        System.out.println();
        sql_select = "select id,first_name, last_name from t_actor";
        actors = jdbcTemplate.query(sql_select, rowMapper);
        System.out.println(sql_select + "#####" + actors);

        System.out.println();
        String sql_delete = "delete from t_actor where id = ?";
        jdbcTemplate.update(sql_delete, 1313);
        System.out.println(sql_delete + "#####" + 1313);


        System.out.println();
        sql_select = "select id,first_name, last_name from t_actor";
        actors = jdbcTemplate.query(sql_select, rowMapper);
        System.out.println(sql_select + "#####" + actors);


        NamedParameterJdbcTemplate namedParameterJdbcTemplate = JdbcManager.INSTANCE.fetchNamedParameterJdbcTemplate();
        sql_select = "select count(*) from t_actor where first_name = :first_name";
        SqlParameterSource sqlNamedParameters = new MapSqlParameterSource("first_name", "Joe");
        rowCount = namedParameterJdbcTemplate.queryForObject(sql_select, sqlNamedParameters, Integer.class);
        System.out.println();
        System.out.println(sql_select + "#####" + "Joe" + "#####" + rowCount);


        sql_select = "select count(*) from t_actor where first_name = :first_name";
        Map<String, String> namedParameters = Collections.singletonMap("first_name", "Joe");
        rowCount = namedParameterJdbcTemplate.queryForObject(sql_select, namedParameters, Integer.class);
        System.out.println();
        System.out.println(sql_select + "#####" + "Joe" + "#####" + rowCount);


        sql_select = "select count(*) from t_actor where first_name = :firstName and last_name = :lastName";
        actor = new Actor();
        actor.setFirstName("Joe");
        actor.setLastName("Watling");
        sqlNamedParameters = new BeanPropertySqlParameterSource(actor);
        int row = namedParameterJdbcTemplate.queryForObject(sql_select, sqlNamedParameters, Integer.class);
        System.out.println();
        System.out.println(sql_select + "#####" + "Joe;Watling" + "#####" + row);


    }

    public static void main(String[] args) {
        test001();
    }
}
