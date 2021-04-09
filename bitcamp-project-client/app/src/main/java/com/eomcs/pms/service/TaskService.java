package com.eomcs.pms.service;

import java.util.List;
import org.apache.ibatis.session.SqlSession;
import com.eomcs.pms.dao.TaskDao;
import com.eomcs.pms.domain.Task;

// 서비스 객체
//  - 비즈니스 로직을 담고 있다.
//  - 업무에 따라 트랜잭션을 제어하는 일을 한다.
//  - 서비스 객체의 메서드는 가능한 업무 관련 용어를 사용하여 매서드를 정의한다.
//
public class TaskService {

  // 서비스 객체는 트랜젝션을 제어해야 하기 때문에
  // DAO가 사용하는 SqlSession 객체를 주입 받아야 한다.
  SqlSession sqlSession;

  // 비즈니스 로직을 수행하는 동안 데이터 처리를 위해 사용할 DAO 를 주입 받아야 한다.
  TaskDao taskDao;

  public TaskService(SqlSession sqlSession, TaskDao taskDao) {
    this.sqlSession = sqlSession;
    this.taskDao = taskDao;
  }

  // 등록 업무
  public int add(Task task) throws Exception {
    try {
      int count = taskDao.insert(task);
      sqlSession.commit();
      return count;

    } catch (Exception e) {
      sqlSession.rollback();
      throw e;
    }
  }

  // 목록 조회 업무
  public List<Task> list() throws Exception {
    return taskDao.findAll();
  }

  // 목록 조회 업무
  public List<Task> listOfProject(int projectNo) throws Exception {
    return taskDao.findByProjectNo(projectNo);
  }


  // 상세 조회 업무
  public Task get(int no) throws Exception {
    return taskDao.findByNo(no);
  }

  // 변경 업무
  public int update(Task task) throws Exception {
    try {
      int count = taskDao.update(task);
      sqlSession.commit();
      return count;

    } catch (Exception e) {
      sqlSession.rollback();
      throw e;
    }
  }

  // 삭제 업무
  public int delete(int no) throws Exception {
    try {
      int count = taskDao.delete(no);
      sqlSession.commit();
      return count;

    } catch (Exception e) {
      sqlSession.rollback();
      throw e;
    }
  }
}
