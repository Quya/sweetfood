package com.quya.dao;

import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer;

import com.quya.common.utils.exception.BusinessException;
import com.quya.common.utils.log.HollyinfoLog;
import com.quya.common.utils.log.HollyinfoLogger;


//import com.hollyinfo.hiiap.HiiapMaxValueIncrementer;

public abstract class BaseDao extends JdbcDaoSupport {
    private static final HollyinfoLog LOG = HollyinfoLogger.getLog(BaseDao.class);

    // ~主键增长机制
    private DataFieldMaxValueIncrementer idIncrementer;

    public DataFieldMaxValueIncrementer getIdIncrementer() {
        return idIncrementer;
    }

    public void setIdIncrementer(DataFieldMaxValueIncrementer pincrementer) {
        idIncrementer = pincrementer;
    }


    /**
     * 判断表名是否存在
     * @param tableName
     * @return
     */
    public boolean isExistedTableName(final String tableName) {
        String sql = "SELECT COUNT(name)  FROM sysobjects WHERE (name = ?)";;
        int judge = this.getJdbcTemplate().queryForInt(sql, new Object[] {tableName.toUpperCase()});
        return judge == 0 ? false : true;
    }

    /**
     * @author liwei2840 用于创建表，如果表名不存在，则创建一个
     * @param pTableName(表名称)
     * @param sql[] (创建表的sql语句)
     */
    public void createTable(final String pTableName, final String sql[]) {
        if (!isExistedTableName(pTableName)) {
            this.getJdbcTemplate().batchUpdate(sql);
        }
    }

    /**
     * 执行Sql语句，此方法不抛出任何异常
     * @param pSql
     */
    public void executeSqlNoThrowException(String pSql) {
        try {
            getJdbcTemplate().execute(pSql);
        } catch (DataAccessException e) {
            LOG.debug("执行sql出现异常[" + pSql + "]");
        }
    }
    /**
     * 删除表，次方法不抛出任何异常
     * @param tableName
     */
    public void dropTableNoThrowException(String tableName) {
    	if (isExistedTableName(tableName)) {
    		executeSqlNoThrowException("DROP TABLE " + tableName);
    	}
    }

    /**
     * 执行Sql语句，如果发生错误抛出异常
     * @param pSql
     */
    public void executeSqlThrowException(String pSql) {
        getJdbcTemplate().execute(pSql);
    }
    public void createTempTable(String[] pDdlSql) {
        int size = pDdlSql.length;
        String tStr = "";
        for (int i = 0; i < size; i++) {
            tStr = pDdlSql[i];
            if (tStr != null && tStr.length() > 0) {
              this.executeSqlNoThrowException(tStr.indexOf(";") != -1 ? tStr.substring(0, tStr.indexOf(";")) : tStr);
            }
        }
        try {
            getConnection().commit();
        } catch (CannotGetJdbcConnectionException e) {
            LOG.error("创建临时表异常", e);
            throw new BusinessException("创建临时表异常", e);
        } catch (SQLException e) {
            LOG.error("创建临时表异常", e);
            throw new BusinessException("创建临时表异常", e);
        }
    }
  
}