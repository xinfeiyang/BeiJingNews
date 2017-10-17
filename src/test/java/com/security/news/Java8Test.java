package com.security.news;

import android.os.Build;
import android.support.annotation.RequiresApi;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Created by Feng on 2017/10/16.
 */
public class Java8Test {

    public class Student {

        /** 学号 */
        private long id;

        private String name;

        private int age;

        /** 年级 */
        private int grade;

        /** 专业 */
        private String major;

        /** 学校 */
        private String school;

        public Student() {

        }

        public Student(long id, String name, int age, int grade, String major, String school) {
            this.id = id;
            this.name = name;
            this.age = age;
            this.grade = grade;
            this.major = major;
            this.school = school;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public int getGrade() {
            return grade;
        }

        public void setGrade(int grade) {
            this.grade = grade;
        }

        public String getMajor() {
            return major;
        }

        public void setMajor(String major) {
            this.major = major;
        }

        public String getSchool() {
            return school;
        }

        public void setSchool(String school) {
            this.school = school;
        }

        @Override
        public String toString() {
            return "Student{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", age=" + age +
                    ", grade=" + grade +
                    ", major='" + major + '\'' +
                    ", school='" + school + '\'' +
                    '}';
        }
    }

    //去重distinct:distinct基于Object.equals(Object)实现
    @Test
    public void testDistinct(){
        List<Integer> list=new ArrayList<>();
        list.add(25);
        list.add(26);
        list.add(38);
        list.add(25);
        list.add(26);
        list.add(28);
        list.stream().filter(i->i%2==0).distinct().forEach(i->System.out.println(i));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Test
    public void test() {
        List<Student> students = new ArrayList<Student>();
        students.add(new Student(20160001, "孔明", 20, 1, "土木工程", "武汉大学"));
        students.add(new Student(20160002, "伯约", 21, 2, "信息安全", "武汉大学"));
        students.add(new Student(20160003, "玄德", 22, 3, "经济管理", "武汉大学"));
        students.add(new Student(20160004, "云长", 21, 2, "信息安全", "武汉大学"));
        students.add(new Student(20161001, "翼德", 21, 2, "机械与自动化", "华中科技大学"));
        students.add(new Student(20161002, "元直", 23, 4, "土木工程", "华中科技大学"));
        students.add(new Student(20161003, "奉孝", 23, 4, "计算机科学", "华中科技大学"));
        students.add(new Student(20162001, "仲谋", 22, 3, "土木工程", "浙江大学"));
        students.add(new Student(20162002, "鲁肃", 23, 4, "计算机科学", "浙江大学"));
        students.add(new Student(20163001, "丁奉", 24, 5, "土木工程", "南京大学"));

        //过滤filter
        //students.stream().filter(student->student.getSchool().equals("武汉大学")).forEach(student->System.out.println(student));
        //limit
        //students.stream().filter(student -> "土木工程".equals(student.getMajor())).limit(2).forEach(s->System.out.println(s));
        /*students.stream().filter(student -> "土木工程".equals(student.getMajor()))
                .sorted((s1,s2)->s1.getAge()-s2.getAge())
                .skip(2)
                .limit(2)
                .forEach(s->System.out.println(s));*/

        /*students.stream().filter(student -> "计算机科学".equals(student.getMajor()))
                .map(Student::getName).forEach(System.out::println);*/
        //map
       /* int sum=students.stream().filter(student -> "计算机科学".equals(student.getMajor()))
                .mapToInt(Student::getAge).sum();
        System.out.println(sum);*/
        /*Optional<Student> max = students.stream().max((s1,s2)->Integer.compare(s1.getAge(),s2.getAge()));
        System.out.println(max.get());*/
        students.stream().sorted((s1,s2)->Integer.compare(s1.getAge(),s2.getAge()))
                .forEach(System.out::println);

    }

    ////flatMap:flatMap与map的区别在于 flatMap是将一个流中的每个值都转成一个个流，然后再将这些流扁平化成为一个流
    @Test
    public void testFlatMap(){

        String[] strs = {"java8", "is", "easy", "to", "use"};
        Stream<String[]> stream = Arrays.asList(strs).stream().map(str -> str.split(""));
        stream.flatMap(new Function<String[], Stream<?>>() {
            @Override
            public Stream<?> apply(String[] strings) {
                return Arrays.asList(strings).stream();
            }
        }).distinct().forEach(System.out::println);
    }


    public class Store {

        private String name;
        private Set<String> book;

        public void addBook(String book) {
            if (this.book == null) {
                this.book = new HashSet<>();
            }
            this.book.add(book);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Set<String> getBook() {
            return book;
        }

        public void setBook(Set<String> book) {
            this.book = book;
        }
    }

    @Test
    public void testFlatmap(){
        Store obj1 = new Store();
        obj1.setName("mkyong");
        obj1.addBook("Java 8 in Action");
        obj1.addBook("Spring Boot in Action");
        obj1.addBook("Effective Java (2nd Edition)");

        Store obj2 = new Store();
        obj2.setName("zilap");
        obj2.addBook("Learning Python, 5th Edition");
        obj2.addBook("Effective Java (2nd Edition)");

        List<Store> list = new ArrayList<>();
        list.add(obj1);
        list.add(obj2);

        Stream<Set<String>> setStream = list.stream().map(s -> s.getBook());
        setStream.flatMap(s->s.stream())
                .forEach(System.out::println);

    }
}
