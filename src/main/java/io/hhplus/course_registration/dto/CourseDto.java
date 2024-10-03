package io.hhplus.course_registration.dto;

public class CourseDto {

    public record RegisterCourseRequest(
            Long courseInfoId,
            Long memberId
    ) {}

    public record RegisterCourseResponse(
            Long courseId,
            Long courseInfoId,
            String courseName,
            String teacherName,
            String courseDate
    ) {}

    public record OpenCourseResponse(
            Long courseId,
            Long courseInfoId,
            String courseName,
            String teacherName,
            String courseDate,
            Integer enrollment
    ) {}

    public record MyRegisteredCourseResponse(
            Long courseId,
            Long courseInfoId,
            String courseName,
            String teacherName,
            String courseDate,
            String createDateTime
    ) {}

}
