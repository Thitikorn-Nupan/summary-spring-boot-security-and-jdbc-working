package com.ttknp.understandspringsecuritywithabstractauthenticationtokenandapplyjdbc.helpers.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class JdbcExecuteSQLHelper {

    private static final Logger log = LoggerFactory.getLogger(JdbcExecuteSQLHelper.class);
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcExecuteSQLHelper(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // It's very useful for sql where result more 1 rows
    public <T> T selectWhereMapPropByRowMapper(String sql, ResultSetExtractor<T> resultSetExtractor, Object ...params) {
        // it maps ? auto
        return jdbcTemplate.query(sql, resultSetExtractor,params);
    }

    // ResultSetExtractor works like RowMapper
    public <T> T selectMapPropByResultSetExtractor(String sql,ResultSetExtractor<T> resultSetExtractor){
        return jdbcTemplate.query(sql,resultSetExtractor) ;
    }

    // BeanPropertyRowMapper, this class saves you a lot of time for the mapping.
    public <T> List<T> selectAllMapPropByBeanPropertyRowMapper(String sql, Class<T> aBeanClass) {
        // var rowMapper = BeanPropertyRowMapper.newInstance(aBeanClass); // auto map getter/setter
        // return jdbcTemplate.query(sql,rowMapper);
        // can reduce by below
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<T>(aBeanClass));
    }

    // jdbcTemplate.queryForList, it works for rows, but not recommend, the mapping in Map may not same as the object, need casting.
    public List<Map<String, Object>> selectAllOnlyColumnMapPropByRowMapper(String sql) {
        return jdbcTemplate.queryForList(sql);
    }

    // U can be only String , Integer , ... anything but should not be Object
    public <U> List<U> selectAllOnlyColumnMapPropByRowMapper(String sql, Class<U> aClass) {
        return jdbcTemplate.queryForList(sql, aClass);
    }

    public <T> T selectOneMapPropByBeanPropertyRowMapper(String sql, Class<T> aBeanClass, Object... params) {
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<T>(aBeanClass), params);
    }

    public Map<String, String> selectOneMapPropByRowMapper(String sql, Object... params) {
        return this.jdbcTemplate.queryForObject(sql, (resultSet, rowNum) -> mapRow(resultSet), params); // result like this : {SID=1, FULL_NAME=Adam Slider, BIRTHDAY=1999-09-19, LEVEL=A+}
    }

    // <T extends Student> meaning i need the generic have to relation with Student class
    // U can be only String , Integer , ... anything but should not be Object
    public <U> Integer deleteByUniq(String sql, U param) {
        return jdbcTemplate.update(sql, param);
    }

    public  Integer countRows(String sql){
        return jdbcTemplate.queryForObject(sql,Integer.class);
    }

    // Executing multiple queries with execute(...) The method execute(String sql) returns void if a call of the method succeeds without errors.
    public Integer multipleQueries(String sql) {
        try {
            jdbcTemplate.execute(sql);
            return 1;
        }catch (Exception e){
            log.debug("Error multiple queries {}", e.getMessage());
            return -1;
        }
    }

    // *** SimpleJdbcInsert class simplifies writing code to execute SQL INSERT statement, i.e.
    // you don’t have to write lengthy and tedious SQL Insert statement anymore – just specify the table name, column names and parameter values.
    public Integer save(String schema,String table,Map<String, Object> params) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        simpleJdbcInsert.withSchemaName(schema);
        simpleJdbcInsert.withTableName(table);
        return simpleJdbcInsert.execute(params); // You see, you don’t have to write SQL Insert statement. And using named parameters same as the column names make the code more readable.
    }

    // *** SimpleJdbcInsert class
    // If your table have auto inceremnt have to set which key is auto increment
    // MapSqlParameterSource params for using with which tables have auto increment
    public Number save(String schema, String table,String primaryKey, MapSqlParameterSource params) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        simpleJdbcInsert.withSchemaName(schema);
        simpleJdbcInsert.withTableName(table);
        simpleJdbcInsert.usingGeneratedKeyColumns(primaryKey); // ** You can use the SimpleJdbcInsert class to execute SQL Insert statement and return value of the primary column which is auto-generated.
        return simpleJdbcInsert.executeAndReturnKey(params);
    }

    // *** SimpleJdbcInsert class
    // If your table have auto inceremnt have to set which key is auto increment
    // SimpleJdbcInsert with BeanPropertySqlParameterSource class Its auto mapping
    public <T> Integer save(String schema, String table, String primaryKey,T object) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        simpleJdbcInsert.withSchemaName(schema);
        simpleJdbcInsert.withTableName(table);
        simpleJdbcInsert.usingGeneratedKeyColumns(primaryKey);
        // As you can see, you don’t even have to specify parameter names and values, as long as you provide an object that has attributes same as the column names in the database table.
        // In the above code, the contact variable is an object of the
        // BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(object);
        // can reduce
        return simpleJdbcInsert.execute(new BeanPropertySqlParameterSource(object));
    }

    private Map<String, String> mapRow(ResultSet resultSet) throws SQLException {
        Map<String, String> rowMap = new LinkedHashMap<>();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            String columnName = metaData.getColumnLabel(i);
            String columnValue = resultSet.getString(i);
            rowMap.put(columnName, columnValue);
        }
        return rowMap;
    }


}