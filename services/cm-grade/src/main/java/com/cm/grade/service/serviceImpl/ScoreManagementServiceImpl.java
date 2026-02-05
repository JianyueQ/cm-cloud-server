package com.cm.grade.service.serviceImpl;

import com.cm.common.core.constant.ParametersQuestionConstant;
import com.cm.common.core.context.BaseContext;
import com.cm.common.core.exception.ParametersQuestionException;
import com.cm.dto.GradeAddDTO;
import com.cm.entity.CourseSelection;
import com.cm.entity.Grade;
import com.cm.grade.mapper.ScoreManagementMapper;
import com.cm.grade.service.ScoreManagementService;
import com.cm.vo.CourseStudentTreeVO;
import com.cm.vo.GradeVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 31373
 */
@Service
public class ScoreManagementServiceImpl implements ScoreManagementService {

    @Autowired
    private ScoreManagementMapper scoreManagementMapper;

    /**
     * 课程学生树
     * @return 课程学生树
     */
    @Override
    public List<CourseStudentTreeVO> courseStudentTree() {
        Long currentId = BaseContext.getCurrentId();
        return scoreManagementMapper.courseStudentTree(currentId);
    }

    @Override
    public GradeVO getGradeByCourseAndStudent(Long courseId, Long studentId) {
        return scoreManagementMapper.getGradeByCourseAndStudent(courseId, studentId);
    }

    @Override
    public void addGrade(GradeAddDTO gradeAddDTO) {
        GradeVO gradeVO = scoreManagementMapper.getGradeByCourseAndStudent(gradeAddDTO.getCourseId(), gradeAddDTO.getStudentId());
        if (gradeVO != null){
            //"该学生已存在该课程的记录"
            throw new ParametersQuestionException(ParametersQuestionConstant.STUDENT_EXIST_COURSE_RECORD);
        }

        //根据课程id和学生id获取选课表信息
        CourseSelection courseSelection = scoreManagementMapper.getCourseSelectionByCourseAndStudent(gradeAddDTO.getCourseId(), gradeAddDTO.getStudentId());
        Grade grade = new Grade();
        BeanUtils.copyProperties(gradeAddDTO, grade);
        grade.setCourseTeachingId(courseSelection.getCourseTeachingId());
        grade.setTeacherId(courseSelection.getTeacherId());
        grade.setTeacherName(courseSelection.getTeacherName());
        scoreManagementMapper.addGrade(grade);
    }

    @Override
    public GradeVO getGradeByCourseAndStudentByCourseId(Long courseId) {
        Long studentId = scoreManagementMapper.getStudentIdByCourseId(BaseContext.getCurrentId());
        return scoreManagementMapper.getGradeByCourseAndStudent(courseId, studentId);
    }

    @Override
    public List<CourseStudentTreeVO> adminCourseStudentTree() {
        return scoreManagementMapper.adminCourseStudentTree();
    }

    @Override
    public void updateGrade(GradeAddDTO gradeAddDTO) {
        //先查询是否有成绩信息,如果没有则抛出校验异常
        GradeVO gradeVO = scoreManagementMapper.getGradeByCourseAndStudent(gradeAddDTO.getCourseId(), gradeAddDTO.getStudentId());
        if (gradeVO == null){
            //"该学生不存在该课程的记录"
            throw new ParametersQuestionException(ParametersQuestionConstant.STUDENT_NOT_EXIST_COURSE_RECORD);
        }
        Grade grade = new Grade();
        BeanUtils.copyProperties(gradeAddDTO, grade);
        scoreManagementMapper.updateGrade(grade);
    }


}
