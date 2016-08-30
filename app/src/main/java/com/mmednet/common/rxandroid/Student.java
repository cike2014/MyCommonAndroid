package com.mmednet.common.rxandroid;

import java.util.List;

/**
 * Created by alpha on 2016/8/29.
 */
public class Student {

    public int id;
    public String sname;

    public List<Course> courses;

    public Student(){}

    public Student(int id, String sname, List<Course> courses) {
        this.id = id;
        this.sname = sname;
        this.courses = courses;
    }




    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", sname='" + sname + '\'' +
                ", courses=" + courses +
                '}';
    }
}

 class Course {
    public int id;
    public String cname;

    public Course(){}
    public Course(int id, String cname) {
        this.id = id;
        this.cname = cname;
    }


}
