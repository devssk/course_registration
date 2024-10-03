package io.hhplus.course_registration;

import io.hhplus.course_registration.dto.CourseDto;
import io.hhplus.course_registration.entity.*;
import io.hhplus.course_registration.entity.enums.CourseStatus;
import io.hhplus.course_registration.repository.*;
import io.hhplus.course_registration.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RegisterCourseIntegrationTest {

    @Autowired
    CourseService courseService;

    @Autowired
    CourseRepository courseRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeacherRepository teacherRepository;
    @Autowired
    CourseInfoRepository courseInfoRepository;
    @Autowired
    CourseEnrollmentRepository courseEnrollmentRepository;
    @Autowired
    RegisterCourseHistoryRepository registerCourseHistoryRepository;

    @BeforeEach
    void setUp() {
        Course course = new Course("흑백요리사 리뷰");
        Teacher teacher1 = new Teacher("안성재");
        Teacher teacher2 = new Teacher("백종원");
        Member member1 = new Member("승우아빠");
        Member member2 = new Member("은수저");
        Member member3 = new Member("시크릿코스");
        Member member4 = new Member("나폴리 맛피아");
        Member member5 = new Member("요리하는 돌아이");
        Member member6 = new Member("최현석");
        Member member7 = new Member("최강록");
        Member member8 = new Member("정지선");
        Member member9 = new Member("여경래");
        Member member10 = new Member("파브리");
        Member member11 = new Member("안유성");
        Member member12 = new Member("철가방 요리사");
        Member member13 = new Member("불꽃 남자");
        Member member14 = new Member("만찢남");
        Member member15 = new Member("짹짹이");
        Member member16 = new Member("중식 여신");
        Member member17 = new Member("비빔대왕");
        Member member18 = new Member("이모카세 1호");
        Member member19 = new Member("급식대가");
        Member member20 = new Member("간귀");
        Member member21 = new Member("트리플스타");
        Member member22 = new Member("히든 천재");
        Member member23 = new Member("원투쓰리");
        Member member24 = new Member("이영숙");
        Member member25 = new Member("오세득");
        Member member26 = new Member("박준우");
        Member member27 = new Member("에드워드 리");
        Member member28 = new Member("셀럽의 셰프");
        Member member29 = new Member("국내파스타");
        Member member30 = new Member("골목식당 1호");
        Member member31 = new Member("방기수");
        Member member32 = new Member("조셉 리저우드");
        Member member33 = new Member("50억 초밥왕");
        Member member34 = new Member("정육맨");
        Member member35 = new Member("고기 깡패");
        Member member36 = new Member("김승민");
        Member member37 = new Member("김도윤");
        Member member38 = new Member("조은주");
        Member member39 = new Member("선경 롱게스트");
        Member member40 = new Member("황진선");

        courseRepository.save(course);
        teacherRepository.save(teacher1);
        teacherRepository.save(teacher2);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);
        memberRepository.save(member5);
        memberRepository.save(member6);
        memberRepository.save(member7);
        memberRepository.save(member8);
        memberRepository.save(member9);
        memberRepository.save(member10);
        memberRepository.save(member11);
        memberRepository.save(member12);
        memberRepository.save(member13);
        memberRepository.save(member14);
        memberRepository.save(member15);
        memberRepository.save(member16);
        memberRepository.save(member17);
        memberRepository.save(member18);
        memberRepository.save(member19);
        memberRepository.save(member20);
        memberRepository.save(member21);
        memberRepository.save(member22);
        memberRepository.save(member23);
        memberRepository.save(member24);
        memberRepository.save(member25);
        memberRepository.save(member26);
        memberRepository.save(member27);
        memberRepository.save(member28);
        memberRepository.save(member29);
        memberRepository.save(member30);
        memberRepository.save(member31);
        memberRepository.save(member32);
        memberRepository.save(member33);
        memberRepository.save(member34);
        memberRepository.save(member35);
        memberRepository.save(member36);
        memberRepository.save(member37);
        memberRepository.save(member38);
        memberRepository.save(member39);
        memberRepository.save(member40);

        CourseInfo courseInfo1 = new CourseInfo(course, teacher1, 30, LocalDate.of(2024, 10, 11), CourseStatus.EMPTY);
        CourseInfo courseInfo2 = new CourseInfo(course, teacher1, 30, LocalDate.of(2024, 10, 13), CourseStatus.EMPTY);
        CourseInfo courseInfo3 = new CourseInfo(course, teacher2, 30, LocalDate.of(2024, 10, 11), CourseStatus.EMPTY);
        CourseInfo courseInfo4 = new CourseInfo(course, teacher2, 30, LocalDate.of(2024, 10, 16), CourseStatus.EMPTY);

        courseInfoRepository.save(courseInfo1);
        courseInfoRepository.save(courseInfo2);
        courseInfoRepository.save(courseInfo3);
        courseInfoRepository.save(courseInfo4);

        CourseEnrollment courseEnrollment1 = new CourseEnrollment(courseInfo1, 0);
        CourseEnrollment courseEnrollment2 = new CourseEnrollment(courseInfo2, 0);
        CourseEnrollment courseEnrollment3 = new CourseEnrollment(courseInfo3, 0);
        CourseEnrollment courseEnrollment4 = new CourseEnrollment(courseInfo4, 0);

        courseEnrollmentRepository.save(courseEnrollment1);
        courseEnrollmentRepository.save(courseEnrollment2);
        courseEnrollmentRepository.save(courseEnrollment3);
        courseEnrollmentRepository.save(courseEnrollment4);
    }

    @Test
    @DisplayName("한 강의에 40명 동시에 신청할 때")
    void member40RegisterCourseTest() throws InterruptedException {
        // given
        Long courseInfoId = 1L;

        // when
        int threadCount = 40;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        for (int i = 1; i < 41; i++) {
            final int index = i;
            executorService.execute(() -> {
                Long memberId = (long) index;
                CourseDto.RegisterCourseRequest req = new CourseDto.RegisterCourseRequest(courseInfoId, memberId);
                try {
                    courseService.registerCourse(req);
                } catch (IllegalArgumentException e) {
                    assertTrue("해당 강의는 최대 수강인원에 도달했습니다.".equals(e.getMessage()) || "강의 최대 수강 인원은 30명까지 입니다.".equals(e.getMessage()));
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();

        List<RegisterCourseHistory> resultList = registerCourseHistoryRepository.findAllByCourseInfoCourseInfoId(courseInfoId);
        CourseEnrollment result = courseEnrollmentRepository.findById(1L).get();
        CourseInfo courseInfo = courseInfoRepository.findById(courseInfoId).get();

        // then
        assertEquals(resultList.size(), 30);
        assertEquals(result.getEnrollment(), 30);
        assertEquals(courseInfo.getCourseStatus(), CourseStatus.FULL);
    }

}
