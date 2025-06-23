package com.pravin.learnsphere_backend_with_spring_boot.dto;

public class CourseInstanceDTO {
    // No-arg constructor for frameworks and manual instantiation
    public CourseInstanceDTO() {}

    private String instanceId;
    private String courseId;
    private String courseName;
    private String courseDescription;
    private String[] coursePrerequisites;
    private Integer year;
    private Integer semester;

    public CourseInstanceDTO(String string, String string2, int i, int j) {
      //TODO Auto-generated constructor stub
    }
    public String getInstanceId() {
        return instanceId;
    }
    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }
    public String getCourseId() {
        return courseId;
    }
    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }
    public Integer getYear() {
        return year;
    }
    public void setYear(Integer year) {
        this.year = year;
    }
    public Integer getSemester() {
        return semester;
    }
    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public String getCourseName() {
        return courseName;
    }
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    public String getCourseDescription() {
        return courseDescription;
    }
    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }
    public String[] getCoursePrerequisites() {
        return coursePrerequisites;
    }
    public void setCoursePrerequisites(String[] coursePrerequisites) {
        this.coursePrerequisites = coursePrerequisites;
    }
}
