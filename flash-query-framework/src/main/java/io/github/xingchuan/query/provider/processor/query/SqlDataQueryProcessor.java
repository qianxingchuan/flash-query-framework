package io.github.xingchuan.query.provider.processor.query;

import cn.hutool.core.lang.Dict;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.github.xingchuan.query.api.domain.base.DataSource;
import io.github.xingchuan.query.api.domain.enums.DataSourceType;
import io.github.xingchuan.query.api.domain.error.CommonError;
import io.github.xingchuan.query.provider.helper.ResponseBuilder;
import io.github.xingchuan.query.provider.processor.query.connection.SqlConnectionPoolManager;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import static io.github.xingchuan.query.provider.processor.query.connection.DefaultJdbcDirectConnectPool.DIRECT_JDBC;

/**
 * 通过sql查询数据
 *
 * @author xingchuan.qxc
 * @since 1.0
 */
public class SqlDataQueryProcessor extends AbstractDataQueryProcessor {

    private final SqlConnectionPoolManager sqlConnectionPoolManager;

    private String connectionPoolType;


    public SqlDataQueryProcessor(SqlConnectionPoolManager sqlConnectionPoolManager, DataQueryProcessorManager dataQueryProcessorManager) {
        this(sqlConnectionPoolManager, dataQueryProcessorManager, null);
    }

    public SqlDataQueryProcessor(SqlConnectionPoolManager sqlConnectionPoolManager, DataQueryProcessorManager dataQueryProcessorManager, String connectionPoolType) {
        super(dataQueryProcessorManager);
        this.sqlConnectionPoolManager = sqlConnectionPoolManager;
        this.connectionPoolType = connectionPoolType;
        if (StringUtils.isBlank(connectionPoolType)) {
            this.connectionPoolType = DIRECT_JDBC;
        }
    }

    @Override
    public JSONArray queryData(String query, DataSource dataSource) throws Exception {
        checkDataSourceType(dataSource);
        JSONArray result = JSONUtil.createArray();
        try (Connection connection = sqlConnectionPoolManager.fetchSqlConnectionPool(connectionPoolType).getConnection(dataSource)) {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (resultSet.next()) {
                JSONObject rowJson = JSONUtil.createObj();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    rowJson.set(columnName, resultSet.getString(columnName));
                }
                result.add(rowJson);
            }
        }
        return result;
    }


    @Override
    public JSON executeUpdate(String update, DataSource dataSource) throws Exception {
        checkDataSourceType(dataSource);
        int rowsEffect = 0;
        try (Connection connection = sqlConnectionPoolManager.fetchSqlConnectionPool(connectionPoolType).getConnection(dataSource)) {
            connection.setAutoCommit(false);
            rowsEffect = connection.prepareStatement(update).executeUpdate();
            connection.commit();
        }
        return ResponseBuilder.buildResponse(true, "", "", rowsEffect);
    }

    @Override
    public String processorType() {
        return DataSourceType.DB.name();
    }

    /**
     * 检查数据源类型
     *
     * @param dataSource
     */
    private void checkDataSourceType(DataSource dataSource) {
        DataSourceType dataSourceType = dataSource.getDataSourceType();
        if (!DataSourceType.DB.equals(dataSourceType)) {
            // 传入的不是一个DB的数据源，不支持
            throw CommonError.NOT_SUPPORT_OPERATION().parseErrorMsg(Dict.create().set("operation", dataSourceType + " ds Type for SQL query.")).newException();
        }
    }
}
