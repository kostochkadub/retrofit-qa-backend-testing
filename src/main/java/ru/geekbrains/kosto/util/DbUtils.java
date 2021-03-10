package ru.geekbrains.kosto.util;

import lombok.SneakyThrows;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import ru.geekbrains.kosto.java4.lesson6.db.dao.CategoriesMapper;
import ru.geekbrains.kosto.java4.lesson6.db.dao.ProductsMapper;

import java.io.IOException;
import java.io.InputStream;

public class DbUtils {

    private static  String resource = "mybatis-config.xml";

    @SneakyThrows
    public static CategoriesMapper getCategoriesMapper(){
        return getSqlSession().getMapper(CategoriesMapper.class);
    }

    @SneakyThrows
    public static ProductsMapper getProductsMapper() {
        return getSqlSession().getMapper(ProductsMapper.class);
    }

    private static SqlSession getSqlSession() throws IOException {
        SqlSessionFactory sqlSessionFactory;
        InputStream is = Resources.getResourceAsStream(resource);
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);
        SqlSession session = sqlSessionFactory.openSession(true);
        return session;
    }

}
