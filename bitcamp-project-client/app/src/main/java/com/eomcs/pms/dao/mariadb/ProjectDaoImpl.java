package com.eomcs.pms.dao.mariadb;

import java.util.HashMap;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import com.eomcs.pms.dao.ProjectDao;
import com.eomcs.pms.domain.Member;
import com.eomcs.pms.domain.Project;

public class ProjectDaoImpl implements ProjectDao {

  //auto commit 객체 받기
  SqlSession sqlSession;

  public ProjectDaoImpl(SqlSession sqlSession) throws Exception {
    this.sqlSession = sqlSession;
  }

  @Override
  public int insert(Project project) throws Exception {
    return sqlSession.insert("ProjectMapper.insert", project);
  }

  @Override
  public List<Project> findByKeyword(String item, String keyword) throws Exception {

    HashMap<String,Object> params = new HashMap<>();
    params.put("item", item);
    params.put("keyword", keyword);

    return sqlSession.selectList("ProjectMapper.findByKeyword", params);
  }

  @Override
  public List<Project> findByKeywords(String title, String owner, String member) throws Exception {

    HashMap<String,Object> params = new HashMap<>();
    params.put("title", title);
    params.put("owner", owner);
    params.put("member", member);

    return sqlSession.selectList("ProjectMapper.findByKeywords", params);
  }

  @Override
  public Project findByNo(int no) throws Exception {
    // 1) 프로젝트 정보를 가져올 때 멤버 목록도 함께 가져오기
    return sqlSession.selectOne("ProjectMapper.findByNo", no);
    // 프로젝트의 멤버 목록을 따로 가져올 때
    //    Project project = sqlSession.selectOne("ProjectMaper.findByNo",no);
    //    project.setMembers(findMember(no));
    //
    //    return project;
  }

  @Override
  public int update(Project project) throws Exception {
    return sqlSession.update("ProjectMapper.update", project);
  }  

  @Override
  public int delete(int no) throws Exception {
    return sqlSession.delete("ProjectMapper.delete", no);
  }

  @Override
  public int insertMember(int projectNo, int memberNo) throws Exception {
    HashMap<String,Object> params = new HashMap<>();
    params.put("projectNo", projectNo);
    params.put("memberNo", memberNo);

    int count = sqlSession.insert("ProjectMapper.insertMember", params);
    sqlSession.commit();
    return count;
  }

  @Override
  public int insertMembers(int projectNo, List<Member> members) throws Exception {
    HashMap<String,Object> params = new HashMap<>();
    params.put("projectNo", projectNo);
    params.put("members", members);

    return sqlSession.insert("ProjectMapper.insertMembers", params);
  }

  @Override
  public List<Member> findMember(int projectNo) throws Exception {
    return sqlSession.selectList("ProjectMapper.findMember",projectNo);
  }  

  @Override
  public int deleteMember (int projectNo) throws Exception {
    return sqlSession.delete("ProjectMapper.deleteMember", projectNo);
  }
}