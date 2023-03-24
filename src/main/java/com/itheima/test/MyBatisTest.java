package com.itheima.test;

import com.itheima.domain.Course;
import com.itheima.domain.MyBatisUtils;

import com.itheima.domain.School;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.Transaction;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Iterator;
import java.util.List;


public class MyBatisTest {
    public static void main(String[] args) throws Exception {
        //加载Mybatis的核心配置文件,获取SqlSessionFactory对象
        MyBatisUtils my =new MyBatisUtils();
        SqlSession session=my.getSession();

       // 1.查找id为2的课程信息情况
       Course course1 = session.selectOne("findById1",2);
        System.out.println("课程号\t课程名\t课时\t开课学院");
        System.out.println(course1.getId()+"\t"+course1.getName()+"\t"+ course1.getHours()+"\t"+ course1.getSid());
        System.out.println("------------------------");
       //2.查找所有计算机学院开设的课程信息
        List<Course> list1 = session.selectList("findByFK","计算机学院");
        Course course2 = null;
        System.out.println("课程号\t课程名\t课时");
        Iterator<Course> iterator1=list1.listIterator();
        while(iterator1.hasNext())
        {
            course2 = (Course)iterator1.next();
            System.out.print(course2.getId()+"\t"+course2.getName()+"\t"+course2.getHours()+"\t");
            System.out.println();
        }
        System.out.println("------------------------");
        //3.将id=4的课程课时加8
        session.update("updateById",4);
        Course course3 = session.selectOne("findById2",4);
        System.out.println(course3.getHours());
        //4.插入记录： names=”⼤数据存储“，hours=32，schools =1
        Course course4 = new Course();
        course4.setName("大数据存储");
        course4.setHours(32);
        course4.setSid(1);
        session.insert("insertByAll",course4);
        System.out.println("------------------------");
        //5.输出所有的学院开设的课程信息
        List<School> list2= session.selectList("findAllSid");
        School school = null;
        System.out.println("学院号\t学院名\t课程号\t课程名\t课时");
        Iterator<School> iterator2=list2.listIterator();
        while(iterator2.hasNext())
        {
            school = (School)iterator2.next();
            System.out.print(school.getId()+"\t"+school.getSchoolname()+"\t");
            List<Course> list3 = session.selectList("findBySid",school.getId());
            Iterator<Course> iterator = list3.listIterator();
            int i=0;
            while (iterator.hasNext())
            {
                i++;
                Course course = (Course) iterator.next();
                if(i!=1)    System.out.print("\t\t\t\t");
                System.out.println(course.getId()+"\t"+course.getName()+"\t"+course.getHours());
            }
        }
        session.commit();
        session.close();
    }
}
