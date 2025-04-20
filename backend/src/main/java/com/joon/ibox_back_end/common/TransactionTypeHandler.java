//package com.joon.ibox_back_end.common;
//
//import com.joon.ibox_back_end.commonEntity.po.Transactions;
//import org.apache.ibatis.type.BaseTypeHandler;
//import org.apache.ibatis.type.JdbcType;
//import org.apache.ibatis.type.MappedTypes;
//
//import java.sql.CallableStatement;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
///**
// * @program: backend
// * @description: 自定义类型处理器
// * @author: Joonnn
// * @create: 2025-04-0201:08
// **/
//
//@MappedTypes(Transactions.TransactionType.class)
//public class TransactionTypeHandler extends BaseTypeHandler<Transactions.TransactionType> {
//
//    @Override
//    public void setNonNullParameter(PreparedStatement ps, int i,
//                                    Transactions.TransactionType parameter, JdbcType jdbcType) throws SQLException {
//        ps.setString(i, parameter.getDbValue());
//    }
//
//    @Override
//    public Transactions.TransactionType getNullableResult(ResultSet rs, String columnName) throws SQLException {
//        String dbValue = rs.getString(columnName);
//        return dbValue == null ? null : Transactions.TransactionType.fromDbValue(dbValue);
//    }
//
//    @Override
//    public Transactions.TransactionType getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
//        String dbValue = rs.getString(columnIndex);
//        return dbValue == null ? null : Transactions.TransactionType.fromDbValue(dbValue);
//    }
//
//    @Override
//    public Transactions.TransactionType getNullableResult(CallableStatement cs, int columnIndex) throws SQLException, SQLException {
//        String dbValue = cs.getString(columnIndex);
//        return dbValue == null ? null : Transactions.TransactionType.fromDbValue(dbValue);
//    }
//}
