package io.hhplus.course_registration;

import io.hhplus.course_registration.dto.CourseDto;
import io.hhplus.course_registration.entity.*;
import io.hhplus.course_registration.entity.enums.CourseStatus;
import io.hhplus.course_registration.repository.CourseEnrollmentRepository;
import io.hhplus.course_registration.repository.CourseInfoRepository;
import io.hhplus.course_registration.repository.MemberRepository;
import io.hhplus.course_registration.repository.RegisterCourseHistoryRepository;
import io.hhplus.course_registration.service.CourseService;
import io.hhplus.course_registration.util.Validation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {

    @InjectMocks
    CourseService courseService;

    @Mock
    MemberRepository memberRepository;

    @Mock
    RegisterCourseHistoryRepository registerCourseHistoryRepository;

    @Mock
    CourseInfoRepository courseInfoRepository;

    @Mock
    CourseEnrollmentRepository courseEnrollmentRepository;

    @Mock
    Validation validation;

    @Nested
    @DisplayName("강의 등록 테스트")
    class RegisterCourseTest {

        @Test
        @DisplayName("해당 강의가 없을 경우")
        void courseInfoIsNullTest() {
            // given
            Long courseInfoId = 1L;
            Long memberId = 1L;
            CourseDto.RegisterCourseRequest req = new CourseDto.RegisterCourseRequest(courseInfoId, memberId);
            doReturn(Optional.empty()).when(courseInfoRepository).findById(anyLong());

            // when
            Throwable throwable = assertThrows(IllegalArgumentException.class, () -> courseService.registerCourse(req));

            // then
            assertEquals("해당 강의를 찾을 수 없습니다.", throwable.getMessage());
        }

        @Test
        @DisplayName("해당 유저가 없을 경우")
        void memberIsNullTest() {
            // given
            Long courseInfoId = 1L;
            Long memberId = 1L;
            CourseDto.RegisterCourseRequest req = new CourseDto.RegisterCourseRequest(courseInfoId, memberId);
            doReturn(Optional.of(new CourseInfo())).when(courseInfoRepository).findById(anyLong());
            doReturn(Optional.empty()).when(memberRepository).findById(anyLong());

            // when
            Throwable throwable = assertThrows(IllegalArgumentException.class, () -> courseService.registerCourse(req));

            // then
            assertEquals("해당 유저를 찾을 수 없습니다.", throwable.getMessage());
        }

        @Test
        @DisplayName("해당 강의가 최대 수강 인원 일 경우")
        void courseIsFullTest() {
            // given
            Long courseInfoId = 1L;
            Long memberId = 1L;
            CourseDto.RegisterCourseRequest req = new CourseDto.RegisterCourseRequest(courseInfoId, memberId);

            Course course = new Course(1L, "고기굽기의 모든것");
            Teacher teacher = new Teacher(1L, "안성재");
            CourseInfo courseInfo = new CourseInfo(1L, course, teacher, 30, LocalDate.of(2024, 10, 10), CourseStatus.FULL);

            doReturn(Optional.of(courseInfo)).when(courseInfoRepository).findById(anyLong());

            // when
            Throwable throwable = assertThrows(IllegalArgumentException.class, () -> courseService.registerCourse(req));

            // then
            assertEquals("해당 강의는 최대 수강인원에 도달했습니다.", throwable.getMessage());
        }

        @Test
        @DisplayName("동일한 유저가 동일한 강의를 여러번 신청할 경우")
        void sameRegisterCourseTest() throws Exception {
            // given
            Long courseInfoId = 1L;
            Long memberId = 1L;
            CourseDto.RegisterCourseRequest req = new CourseDto.RegisterCourseRequest(courseInfoId, memberId);

            Course course = new Course(1L, "고기굽기의 모든것");
            Member member = new Member("본업도 잘하는 남자");

            Field createdAtField = Member.class.getDeclaredField("memberId");
            createdAtField.setAccessible(true);
            createdAtField.set(member, memberId);

            Teacher teacher = new Teacher(1L, "안성재");
            CourseInfo courseInfo = new CourseInfo(1L, course, teacher, 30, LocalDate.of(2024, 10, 10), CourseStatus.EMPTY);

            Boolean exists = true;

            doReturn(Optional.of(courseInfo)).when(courseInfoRepository).findById(anyLong());
            doReturn(Optional.of(member)).when(memberRepository).findById(anyLong());
            doReturn(exists).when(registerCourseHistoryRepository).existsByCourseInfoCourseInfoIdAndMemberMemberId(courseInfoId, memberId);

            // when
            Throwable throwable = assertThrows(IllegalArgumentException.class, () -> courseService.registerCourse(req));

            // then
            assertEquals("동일한 강의는 한번만 신청이 가능합니다.", throwable.getMessage());
        }

        @Test
        @DisplayName("최대 수강 인원 체크는 넘어갔으나 도중 최대 수강 인원에 도달했을 경우")
        void enrollmentIs30Test() throws Exception {
            // given
            Long courseInfoId = 1L;
            Long memberId = 1L;
            CourseDto.RegisterCourseRequest req = new CourseDto.RegisterCourseRequest(courseInfoId, memberId);

            Course course = new Course(1L, "고기굽기의 모든것");
            Teacher teacher = new Teacher(1L, "안성재");
            Member member = new Member("본업도 잘하는 남자");

            Field createdAtField = Member.class.getDeclaredField("memberId");
            createdAtField.setAccessible(true);
            createdAtField.set(member, memberId);

            CourseInfo courseInfo = new CourseInfo(1L, course, teacher, 30, LocalDate.of(2024, 10, 10), CourseStatus.EMPTY);
            CourseEnrollment courseEnrollment = new CourseEnrollment(1L, courseInfo, 30);

            Boolean exists = false;

            doReturn(Optional.of(courseInfo)).when(courseInfoRepository).findById(anyLong());
            doReturn(Optional.of(member)).when(memberRepository).findById(anyLong());
            doReturn(courseEnrollment).when(courseEnrollmentRepository).findByCourseInfoCourseInfoId(anyLong());
            doReturn(exists).when(registerCourseHistoryRepository).existsByCourseInfoCourseInfoIdAndMemberMemberId(anyLong(), anyLong());

            // when
            Throwable throwable = assertThrows(IllegalArgumentException.class, () -> courseService.registerCourse(req));

            // then
            assertEquals("강의 최대 수강 인원은 30명까지 입니다.", throwable.getMessage());
        }

        @Test
        @DisplayName("정상 수강 등록")
        void registerCourseTest() {
            // given
            Long courseInfoId = 1L;
            Long memberId = 1L;
            CourseDto.RegisterCourseRequest req = new CourseDto.RegisterCourseRequest(courseInfoId, memberId);

            Course course = new Course(1L, "고기굽기의 모든것");
            Teacher teacher = new Teacher(1L, "안성재");
            CourseInfo courseInfo = new CourseInfo(1L, course, teacher, 30, LocalDate.of(2024, 10, 10), CourseStatus.EMPTY);
            CourseEnrollment courseEnrollment = new CourseEnrollment(1L, courseInfo, 0);

            doReturn(Optional.of(courseInfo)).when(courseInfoRepository).findById(anyLong());
            doReturn(Optional.of(new Member())).when(memberRepository).findById(anyLong());
            doReturn(courseEnrollment).when(courseEnrollmentRepository).findByCourseInfoCourseInfoId(anyLong());

            // when
            CourseDto.RegisterCourseResponse result = courseService.registerCourse(req);

            // then
            assertAll(() -> {
                assertNotNull(result);
                assertEquals(course.getCourseId(), result.courseId());
                assertEquals(courseInfo.getCourseInfoId(), result.courseInfoId());
                assertEquals(course.getCourseName(), result.courseName());
                assertEquals(teacher.getTeacherName(), result.teacherName());
                assertEquals(courseInfo.getCourseDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), result.courseDate());
            });
        }

    }

    @Nested
    @DisplayName("신청 가능한 강의 목록 조회 테스트")
    class GetOpenCourseListTest {

        @Test
        @DisplayName("해당 유저가 없을 경우")
        void memberIsNullTest() {
            // given
            Long memberId = 1L;
            doReturn(Optional.empty()).when(memberRepository).findById(anyLong());

            // when
            Throwable throwable = assertThrows(IllegalArgumentException.class, () -> courseService.getMyRegisteredCourseList(memberId));

            // then
            assertEquals("해당 유저를 찾을 수 없습니다.", throwable.getMessage());
        }

        @Test
        @DisplayName("정상 조회 건")
        void getOpenCourseListTest() {
            // given
            Course course = new Course(1L, "장사의 모든것");
            Teacher teacher = new Teacher(1L, "백종원");
            CourseInfo courseInfo = new CourseInfo(1L, course, teacher, 30, LocalDate.of(2024, 10, 10), CourseStatus.EMPTY);
            CourseEnrollment courseEnrollment = new CourseEnrollment(1L, courseInfo, 0);

            List<CourseInfo> courseInfoList = new ArrayList<>();
            courseInfoList.add(courseInfo);
            List<CourseEnrollment> courseEnrollmentList = new ArrayList<>();
            courseEnrollmentList.add(courseEnrollment);

            doReturn(courseInfoList).when(courseInfoRepository).findByCourseStatus(CourseStatus.EMPTY);
            doReturn(courseEnrollmentList).when(courseEnrollmentRepository).findAllByCourseInfoCourseInfoIdIn(anyList());

            // when
            List<CourseDto.OpenCourseResponse> result = courseService.getOpenCourseList();

            // then
            assertAll(() -> {
                assertNotNull(result);
                assertEquals(1, result.size());
                assertEquals(course.getCourseId(), result.get(0).courseId());
                assertEquals(courseInfo.getCourseInfoId(), result.get(0).courseInfoId());
                assertEquals(course.getCourseName(), result.get(0).courseName());
                assertEquals(teacher.getTeacherName(), result.get(0).teacherName());
                assertEquals(courseInfo.getCourseDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), result.get(0).courseDate());
                assertEquals(courseEnrollment.getEnrollment(), result.get(0).enrollment());
            });
        }

    }

    @Nested
    @DisplayName("나의 수강 신청 목록 조회 테스트")
    class GetMyRegisteredCourseListTest {

        @Test
        @DisplayName("정상 조회 건")
        void getMyRegisteredCourseListTest() throws Exception {
            // given
            Long memberId = 1L;

            Member member = new Member(memberId, "불꽃남자");
            Course course = new Course(1L, "리조또의 모든것");
            Teacher teacher = new Teacher(1L, "나폴리 맛피아");
            CourseInfo courseInfo = new CourseInfo(1L, course, teacher, 30, LocalDate.of(2024, 10, 10), CourseStatus.EMPTY);
            RegisterCourseHistory registerCourseHistory = new RegisterCourseHistory(1L, member, courseInfo);

            Field createdAtField = EntityTimestamp.class.getDeclaredField("createdAt");
            createdAtField.setAccessible(true);
            LocalDateTime localDateTime = LocalDateTime.now();
            createdAtField.set(registerCourseHistory, localDateTime);

            List<RegisterCourseHistory> registerCourseHistoryList = new ArrayList<>();
            registerCourseHistoryList.add(registerCourseHistory);

            doReturn(Optional.of(member)).when(memberRepository).findById(anyLong());
            doReturn(registerCourseHistoryList).when(registerCourseHistoryRepository).findAllByMemberMemberId(anyLong());

            // when
            List<CourseDto.MyRegisteredCourseResponse> result = courseService.getMyRegisteredCourseList(memberId);

            // then
            assertAll(() -> {
                assertNotNull(result);
                assertEquals(1, result.size());
                assertEquals(course.getCourseId(), result.get(0).courseId());
                assertEquals(courseInfo.getCourseInfoId(), result.get(0).courseInfoId());
                assertEquals(course.getCourseName(), result.get(0).courseName());
                assertEquals(teacher.getTeacherName(), result.get(0).teacherName());
                assertEquals(courseInfo.getCourseDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), result.get(0).courseDate());
                assertEquals(registerCourseHistory.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), result.get(0).createDateTime());
            });
        }

    }


}
