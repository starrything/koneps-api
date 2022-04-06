package com.jonghyun.koneps.domain.charts;

import com.jonghyun.koneps.domain.chart.ChartDto;
import com.jonghyun.koneps.domain.chart.ChartService;
import com.jonghyun.koneps.domain.data.ConnectionPool;
import com.jonghyun.koneps.domain.data.VisualConnectionPool;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public class ChartsDevTest {

    @Autowired
    ChartService chartService;

    @Test
    public void getTableColumnSpec() throws SQLException {
        ConnectionPool connectionPool = VisualConnectionPool
                //.create("jdbc:mariadb://197.200.1.48:3317/framework", "framework", "Framework1!");
                .create("jdbc:postgresql://197.200.1.52:5432/visual", "visual", "visual1234!!");

        Connection conn = connectionPool.getConnection();
        ResultSet rs = conn.getMetaData().getColumns(null, "public", "tb_chart", "%");
        while (rs.next()) {
            String name = rs.getString("COLUMN_NAME");
            int position = rs.getInt("ORDINAL_POSITION");
            String type = rs.getString("TYPE_NAME");
            int length = rs.getInt("CHAR_OCTET_LENGTH");
            int precision = rs.getInt("COLUMN_SIZE");
            int scale = rs.getInt("DECIMAL_DIGITS");
            boolean nullable = rs.getInt("NULLABLE") == 0 ? false : true;
        }

        connectionPool.releaseConnection(conn);
        connectionPool.shutdown();
    }

    @Test
    public void getLineChartData() throws SQLException {
        List<Map<String, Object>> metrics = new ArrayList<>();
        Map<String, Object> code = new HashMap<>();
        code.put("column", "code");
        code.put("metrics", "COUNT");
        metrics.add(code);
        Map<String, Object> upper = new HashMap<>();
        upper.put("column", "upper_code");
        upper.put("metrics", "COUNT_DISTINCT");
        metrics.add(upper);
        /*List<Map<String, Object>> groupBys = new ArrayList<>();
        Map<String, Object> groupBy = new HashMap<>();
        groupBy.put("key", 0);
        groupBy.put("value", "upper_code");
        groupBys.add(groupBy);*/

        ChartDto chartDto = new ChartDto();
        chartDto.setDatasetId("dataset-00011");
        chartDto.setTimeColumn("creation_date");
        chartDto.setTimeGrain("DAY");
        chartDto.setTimeRange("month");
        //chartDto.setMetrics(metrics);
        //chartDto.setGroupBy(groupBys);
        chartDto.setQueryRowLimit("1000");
        chartService.getLineChartData(chartDto);
    }
}
