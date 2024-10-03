package io.hhplus.course_registration.controller;

import io.hhplus.course_registration.dto.CourseDto;
import io.hhplus.course_registration.dto.ResponseDto;
import io.hhplus.course_registration.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @PostMapping("/course")
    public ResponseEntity<ResponseDto> registerCourse(@RequestBody CourseDto.RegisterCourseRequest req) {
        CourseDto.RegisterCourseResponse result = courseService.registerCourse(req);
        return ResponseEntity.ok(new ResponseDto("isOk", result));
    }

    @GetMapping("/course")
    public ResponseEntity<ResponseDto> getOpenCourseList() {
        List<CourseDto.OpenCourseResponse> result = courseService.getOpenCourseList();
        return ResponseEntity.ok(new ResponseDto("isOk", result));
    }

    @GetMapping("/{memberId}/course")
    public ResponseEntity<ResponseDto> getMyRegisteredCourseList(@PathVariable(name = "memberId") Long memberId) {
        List<CourseDto.MyRegisteredCourseResponse> result = courseService.getMyRegisteredCourseList(memberId);
        return ResponseEntity.ok(new ResponseDto("isOk", result));
    }

}
