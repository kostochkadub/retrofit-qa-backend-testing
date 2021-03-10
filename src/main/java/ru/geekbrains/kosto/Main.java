package ru.geekbrains.kosto;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class Main {

    //NyBatis Configuration file
    private static String resource = "mybatis-config.xml";

    public static void main(String[] args) throws IOException {

        SqlSessionFactory sqlSessionFactory;

        InputStream is = Resources.getResourceAsStream(resource);
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(is);

        SqlSession session = sqlSessionFactory.openSession();
        session.getMapper(CategoriesMapper.class);

    }

}
