package com.begcode.monolith.util.mybatis.handler;

import java.sql.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

@MappedTypes(ZonedDateTime.class)
public class ZonedDateTimeTypeHandler extends BaseTypeHandler<ZonedDateTime> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, ZonedDateTime parameter, JdbcType jdbcType) throws SQLException {
        ps.setTimestamp(i, Timestamp.from(parameter.toInstant()), GregorianCalendar.from(parameter));
    }

    @Override
    public ZonedDateTime getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Timestamp ts = rs.getTimestamp(columnName, Calendar.getInstance());
        if (ts != null) {
            return ZonedDateTime.ofInstant(ts.toInstant(), ZoneId.of("+08:00"));
        }

        return null;
    }

    @Override
    public ZonedDateTime getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Timestamp ts = rs.getTimestamp(columnIndex, Calendar.getInstance());
        if (ts != null) {
            return ZonedDateTime.ofInstant(ts.toInstant(), ZoneId.of("+08:00"));
        }

        return null;
    }

    @Override
    public ZonedDateTime getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Timestamp ts = cs.getTimestamp(columnIndex, Calendar.getInstance());
        if (ts != null) {
            return ZonedDateTime.ofInstant(ts.toInstant(), ZoneId.of("+08:00"));
        }

        return null;
    }
}
