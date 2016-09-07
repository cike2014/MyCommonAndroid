package com.mmednet.common.rxjava;

import java.util.List;

/**
 * Created by alpha on 2016/9/7.
 */
public class Student {

    private int id;
    private String sname;
    private List<Course> courses;

    public class Course {

        public Course(){}
        private int id;
        private String cname;

        public void setId(int id) {
            this.id = id;
        }

        public void setCname(String cname) {
            this.cname = cname;
        }

        public int getId() {
            return id;
        }

        public String getCname() {
            return cname;
        }

        @Override
        public String toString() {
            return "Course{" +
                    "id=" + id +
                    ", cname='" + cname + '\'' +
                    '}';
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
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
