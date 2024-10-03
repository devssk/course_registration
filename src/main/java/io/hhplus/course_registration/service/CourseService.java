package io.hhplus.course_registration.service;

import io.hhplus.course_registration.dto.CourseDto;
import io.hhplus.course_registration.entity.CourseEnrollment;
import io.hhplus.course_registration.entity.CourseInfo;
import io.hhplus.course_registration.entity.Member;
import io.hhplus.course_registration.entity.RegisterCourseHistory;
import io.hhplus.course_registration.entity.enums.CourseStatus;
import io.hhplus.course_registration.repository.*;
import io.hhplus.course_registration.util.Validation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final MemberRepository memberRepository;
    private final RegisterCourseHistoryRepository registerCourseHistoryRepository;
    private final CourseInfoRepository courseInfoRepository;
    private final CourseEnrollmentRepository courseEnrollmentRepository;
    private final Validation validation;

    @Transactional
    public CourseDto.RegisterCourseResponse registerCourse(CourseDto.RegisterCourseRequest req) {
        validation.validId(req.courseInfoId());
        validation.validId(req.memberId());

        CourseInfo courseInfo = courseInfoRepository.findById(req.courseInfoId()).orElseThrow(
                () -> new IllegalArgumentException("해당 강의를 찾을 수 없습니다.")
        );
        if (courseInfo.getCourseStatus() == CourseStatus.FULL) {
            throw new IllegalArgumentException("해당 강의는 최대 수강인원에 도달했습니다.");
        }
        Member member = memberRepository.findById(req.memberId()).orElseThrow(
                () -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다.")
        );
        CourseEnrollment courseEnrollment = courseEnrollmentRepository.findByCourseInfoCourseInfoId(courseInfo.getCourseInfoId());
        if (courseEnrollment.getEnrollment() >= courseInfo.getCapacity()) {
            throw new IllegalArgumentException("강의 최대 수강 인원은 30명까지 입니다.");
        }
        courseEnrollment.plusEnrollment();
        if (courseEnrollment.getEnrollment() == courseInfo.getCapacity()) {
            courseInfo.updateCourseStatusFull();
        }

        RegisterCourseHistory registerCourseHistory = new RegisterCourseHistory(member, courseInfo);
        registerCourseHistoryRepository.save(registerCourseHistory);

        return new CourseDto.RegisterCourseResponse(
                registerCourseHistory.getCourseInfo().getCourse().getCourseId(),
                registerCourseHistory.getCourseInfo().getCourseInfoId(),
                registerCourseHistory.getCourseInfo().getCourse().getCourseName(),
                registerCourseHistory.getCourseInfo().getTeacher().getTeacherName(),
                registerCourseHistory.getCourseInfo().getCourseDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        );
    }

    @Transactional(readOnly = true)
    public List<CourseDto.OpenCourseResponse> getOpenCourseList() {
        List<CourseInfo> findOpenCourseList = courseInfoRepository.findByCourseStatus(CourseStatus.EMPTY);
        List<Long> list = findOpenCourseList.stream().map(CourseInfo::getCourseInfoId).toList();
        List<CourseEnrollment> findCourseEnrollmentList = courseEnrollmentRepository.findAllByCourseInfoCourseInfoIdIn(list);
        HashMap<Long, Integer> courseEnrollmentHashMap = new HashMap<>();
        findCourseEnrollmentList.forEach(courseEnrollment -> courseEnrollmentHashMap.put(courseEnrollment.getCourseEnrollmentId(), courseEnrollment.getEnrollment()));
        return findOpenCourseList.stream().map(courseInfo -> new CourseDto.OpenCourseResponse(
                courseInfo.getCourse().getCourseId(),
                courseInfo.getCourseInfoId(),
                courseInfo.getCourse().getCourseName(),
                courseInfo.getTeacher().getTeacherName(),
                courseInfo.getCourseDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                courseEnrollmentHashMap.get(courseInfo.getCourseInfoId())
        )).toList();
    }

    @Transactional(readOnly = true)
    public List<CourseDto.MyRegisteredCourseResponse> getMyRegisteredCourseList(Long memberId) {
        validation.validId(memberId);

        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다.")
        );

        List<RegisterCourseHistory> findRegisterCourseHistoryList = registerCourseHistoryRepository.findAllByMemberMemberId(member.getMemberId());
        return findRegisterCourseHistoryList.stream().map(registerCourseHistory -> new CourseDto.MyRegisteredCourseResponse(
                registerCourseHistory.getCourseInfo().getCourse().getCourseId(),
                registerCourseHistory.getCourseInfo().getCourseInfoId(),
                registerCourseHistory.getCourseInfo().getCourse().getCourseName(),
                registerCourseHistory.getCourseInfo().getTeacher().getTeacherName(),
                registerCourseHistory.getCourseInfo().getCourseDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                registerCourseHistory.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        )).toList();
    }

}
