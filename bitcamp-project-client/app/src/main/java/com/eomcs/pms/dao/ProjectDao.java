package com.eomcs.pms.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.eomcs.pms.domain.Member;
import com.eomcs.pms.domain.Project;

public class ProjectDao {

  Connection con;

  public ProjectDao() throws Exception {
    this.con = DriverManager.getConnection(
        "jdbc:mysql://localhost:3306/studydb?user=study&password=1111");
  }

  public int insert(Project project) throws Exception {
    try (PreparedStatement stmt = con.prepareStatement(
        "insert into pms_project(title,content,sdt,edt,owner) values(?,?,?,?,?)",
        Statement.RETURN_GENERATED_KEYS)) {


      // 수동 커밋으로 설정한다.
      // - pms_project 테이블과 pms_member_project 테이블에 모두 성공적으로 데이터를 저장했을 때
      //   작업을 완료한다.
      con.setAutoCommit(false); // 의미=> 트렌젝션 시작

      stmt.setString(1, project.getTitle());
      stmt.setString(2, project.getContent());
      stmt.setDate(3, project.getStartDate());
      stmt.setDate(4, project.getEndDate());
      stmt.setInt(5, project.getOwner().getNo());
      int count = stmt.executeUpdate();

      // 프로젝트 데이터의 PK값 알아내기
      try (ResultSet keyRs = stmt.getGeneratedKeys()) {
        keyRs.next();
        project.setNo(keyRs.getInt(1));
      }

      // 2) 프로젝트의 팀원들을 추가한다.
      for (Member member : project.getMembers()) {
        insertMember(project.getNo(), member.getNo());
      }

      // 프로젝트 정보 뿐만 아니라 팀원 정보도 정상적으로 입력되었다면,
      // 실제 테이블에 데이터를 적용한다.
      con.commit(); // 의미: 트렌젝션 종료

      return count;

    } finally { // 트랜젝션 종료 후 auto commit 을 원래 상태로 설정
      con.setAutoCommit(true);
    }
  }

  public List<Project> findAll() throws Exception {
    ArrayList<Project> list = new ArrayList<>();

    try (PreparedStatement stmt = con.prepareStatement(
        "select "
            + " p.no,"
            + " p.title,"
            + " p.sdt,"
            + " p.edt,"
            + " m.no as owner_no,"
            + " m.name as owner_name"
            + " from pms_project p "
            + " inner join pms_member m on p.owner=m.no"
            + " order by title asc");
        ResultSet rs = stmt.executeQuery()) {

      while (rs.next()) {
        Project project = new Project();
        project.setNo(rs.getInt("no"));
        project.setTitle(rs.getString("title"));
        project.setStartDate(rs.getDate("sdt"));
        project.setEndDate(rs.getDate("edt"));

        Member owner = new Member();
        owner.setNo(rs.getInt("owner_no"));
        owner.setName(rs.getString("owner_name"));
        project.setOwner(owner);

        project.setMembers(findMember(project.getNo()));

        list.add(project);
      }
    }
    return list;
  }

  public Project findByNo(int no) throws Exception {
    try (PreparedStatement stmt = con.prepareStatement(
        "select "
            + " p.no,"
            + " p.title,"
            + " p.content,"
            + " p.sdt,"
            + " p.edt,"
            + " m.no as owner_no,"
            + " m.name as owner_name"
            + " from pms_project p "
            + " inner join pms_member m on p.owner=m.no"
            + " where p.no=?")) {

      stmt.setInt(1, no);

      try (ResultSet rs = stmt.executeQuery()) {
        if (!rs.next()) {
          System.out.println("해당 번호의 프로젝트가 없습니다.");
          return null;
        }

        Project project = new Project();

        project.setNo(rs.getInt("no"));
        project.setTitle(rs.getString("title"));
        project.setContent(rs.getString("content"));
        project.setStartDate(new Date(rs.getTimestamp("sdt").getTime()));
        project.setEndDate(new Date(rs.getTimestamp("edt").getTime()));

        Member owner = new Member();
        owner.setNo(rs.getInt("owner_no"));
        owner.setName(rs.getString("owner_name"));
        project.setOwner(owner);

        project.setMembers(findMember(project.getNo()));

        return project;
      } 
    }
  }

  public int update(Project project) throws Exception {
    try (PreparedStatement stmt = con.prepareStatement(
        "update pms_project set"
            + " title=?,"
            + " content=?,"
            + " sdt=?,"
            + " edt=?,"
            + " owner=?"
            + " where no=?")) {

      con.setAutoCommit(false);

      stmt.setString(1, project.getTitle());
      stmt.setString(2, project.getContent());
      stmt.setDate(3, project.getStartDate());
      stmt.setDate(4, project.getEndDate());
      stmt.setInt(5, project.getOwner().getNo());
      stmt.setInt(6, project.getNo());

      int count = stmt.executeUpdate();

      // 기존 프로젝트의 모든 멤버를 삭제한다.
      deleteMember(project.getNo());

      // 프로젝트 멤버를 추가한다.
      for (Member member : project.getMembers()) {
        insertMember(project.getNo(), member.getNo());
      }

      con.commit(); // 의미: 트렌젝션 종료

      return count;

    } finally { // 트랜젝션 종료 후 auto commit 을 원래 상태로 설정
      con.setAutoCommit(true);
    }
  }  

  public int delete(int no) throws Exception {
    try (PreparedStatement stmt = con.prepareStatement(
        "delete from pms_project where no=?")) {

      con.setAutoCommit(false);

      // 프로젝트에 소속된 팀원 정보 삭제
      deleteMember(no);

      // 프로젝트 정보 삭제
      stmt.setInt(1, no);
      int count = stmt.executeUpdate();        
      con.commit();

      return count;

    } finally { // 트랜젝션 종료 후 auto commit 을 원래 상태로 설정
      con.setAutoCommit(true);
    }
  }

  public int insertMember(int projectNo, int memberNo) throws Exception {
    try (PreparedStatement stmt = con.prepareStatement(
        "insert into pms_member_project(member_no,project_no) values(?,?)")) {
      stmt.setInt(1, memberNo);
      stmt.setInt(2, projectNo);
      return stmt.executeUpdate();
    }
  }

  public List<Member> findMember(int projectNo) throws Exception {
    ArrayList<Member> list = new ArrayList<>();

    try (PreparedStatement stmt = con.prepareStatement(
        "select"
            + " m.no, "
            + " m.name"
            + " from pms_member_project mp"
            + " inner join pms_member m on mp.member_no=m.no"
            + " where"
            + " mp.project_no=?")) {

      stmt.setInt(1, projectNo);

      try (ResultSet membersRs = stmt.executeQuery()) {
        while (membersRs.next()) {
          Member m = new Member();
          m.setNo(membersRs.getInt("no"));
          m.setName(membersRs.getString("name"));
          list.add(m);
        }
      }
    }
    return list;
  }  

  public int deleteMember(int projectNo) throws Exception {
    try (PreparedStatement stmt = con.prepareStatement(
        "delete from pms_member_project where project_no=?")) {
      stmt.setInt(1, projectNo);
      return stmt.executeUpdate();
    }
  }
  //  public List<Board> findByKeyword(String keyword) throws Exception {
  //    ArrayList<Board> list = new ArrayList<>();
  //
  //    try (PreparedStatement stmt = con.prepareStatement(
  //        "select "
  //            + " b.no,"
  //            + " b.title,"
  //            + " b.cdt,"
  //            + " b.vw_cnt, "
  //            + " b.like_cnt,"
  //            + " m.no as writer_no,"
  //            + " m.name as writer_name"
  //            + " from pms_board b "
  //            + " inner join pms_member m on m.no=b.writer"
  //            + " where title like concat('%',?,'%')"
  //            + " or content like concat('%',?,'%')"
  //            + " or writer like concat('%',?,'%')"
  //            + " order by no desc")) {
  //
  //      stmt.setString(1, keyword);
  //      stmt.setString(2, keyword);
  //      stmt.setString(3, keyword);
  //
  //      ResultSet rs = stmt.executeQuery();
  //
  //      while (rs.next()) {
  //        Board board = new Board();
  //        board.setNo(rs.getInt("no"));
  //        board.setTitle(rs.getString("title"));
  //        board.setRegisteredDate(rs.getDate("cdt"));
  //        board.setViewCount(rs.getInt("vw_cnt"));
  //
  //        Member writer = new Member();
  //        writer.setNo(rs.getInt("writer_no"));
  //        writer.setName(rs.getString("writer_name"));
  //        board.setWriter(writer);
  //
  //        list.add(board);
  //      }
  //    }
  //    return list;
  //  }
}  