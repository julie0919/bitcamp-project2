package com.eomcs.pms.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import com.eomcs.pms.domain.Member;
import com.eomcs.pms.domain.Task;

public class TaskDao {

  Connection con;

  public TaskDao() throws Exception {
    this.con = DriverManager.getConnection(
        "jdbc:mysql://localhost:3306/studydb?user=study&password=1111");
  }

  public int insert(Task task) throws Exception {
    try (PreparedStatement stmt = con.prepareStatement(
        "insert into pms_task(content,deadline,owner,status,project_no) values(?,?,?,?,?)");) {

      stmt.setString(1, task.getContent());
      stmt.setDate(2, task.getDeadline());
      stmt.setInt(3, task.getOwner().getNo());
      stmt.setInt(4, task.getStatus());
      stmt.setInt(5, task.getProjectNo());
      return stmt.executeUpdate();
    }
  }

  public List<Task> findAll() throws Exception {
    ArrayList<Task> list = new ArrayList<>();

    try (PreparedStatement stmt = con.prepareStatement(
        "select "
            + "   t.no,"
            + "   t.content,"
            + "   t.deadline,"
            + "   t.status,"
            + "   m.no as owner_no,"
            + "   m.name as owner_name,"
            + "   p.no as project_no,"
            + "   p.title as project_title"
            + " from pms_task t "
            + "   inner join pms_member m on t.owner=m.no"
            + "   inner join pms_project p on t.project_no=p.no"
            + " order by p.no desc, t.content asc")) {

      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        Task task = new Task();
        task.setNo(rs.getInt("no"));
        task.setContent(rs.getString("content"));
        task.setDeadline(rs.getDate("deadline"));

        Member owner = new Member();
        owner.setNo(rs.getInt("owner_no"));
        owner.setName(rs.getString("owner_name"));
        task.setOwner(owner);

        task.setStatus(rs.getInt("status"));
        task.setProjectNo(rs.getInt("project_no"));
        task.setProjectTitle(rs.getString("project_title"));

        list.add(task);
      }
    }
    return list;
  }

  public Task findByNo(int no) throws Exception {
    try (PreparedStatement stmt = con.prepareStatement(
        "select "
            + " t.no,"
            + " t.content,"
            + " t.deadline,"
            + " t.status,"
            + " m.no as owner_no,"
            + " m.name as owner_name, "
            + " p.no as project_no,"
            + " p.title as project_title"
            + " from pms_task t "
            + " inner join pms_member m on t.owner=m.no "
            + " inner join pms_project p on t.project_no=p.no "
            + " where t.no = ?")) {

      stmt.setInt(1, no);

      try (ResultSet rs = stmt.executeQuery()) {
        if (!rs.next()) {
          System.out.println("해당 번호의 프로젝트가 없습니다.");
          return null;
        }

        Task task = new Task();
        task.setNo(rs.getInt("no"));
        task.setContent(rs.getString("content"));
        task.setDeadline(new Date(rs.getTimestamp("deadline").getTime()));

        Member owner = new Member();
        owner.setNo(rs.getInt("owner_no"));
        owner.setName(rs.getString("owner_name"));
        task.setOwner(owner);

        task.setStatus(rs.getInt("status"));

        task.setProjectNo(rs.getInt("project_no"));
        task.setProjectTitle(rs.getString("project_title"));


        return task;
      } 
    }
  }


  //  public int update(Project project) throws Exception {
  //    try (PreparedStatement stmt = con.prepareStatement(
  //        "update pms_project set"
  //            + " title=?,"
  //            + " content=?,"
  //            + " sdt=?,"
  //            + " edt=?,"
  //            + " owner=?"
  //            + " where no=?")) {
  //
  //      con.setAutoCommit(false);
  //
  //      stmt.setString(1, project.getTitle());
  //      stmt.setString(2, project.getContent());
  //      stmt.setDate(3, project.getStartDate());
  //      stmt.setDate(4, project.getEndDate());
  //      stmt.setInt(5, project.getOwner().getNo());
  //      stmt.setInt(6, project.getNo());
  //
  //      int count = stmt.executeUpdate();
  //
  //      // 기존 프로젝트의 모든 멤버를 삭제한다.
  //      deleteMember(project.getNo());
  //
  //      // 프로젝트 멤버를 추가한다.
  //      for (Member member : project.getMembers()) {
  //        insertMember(project.getNo(), member.getNo());
  //      }
  //
  //      con.commit(); // 의미: 트렌젝션 종료
  //
  //      return count;
  //
  //    } finally { // 트랜젝션 종료 후 auto commit 을 원래 상태로 설정
  //      con.setAutoCommit(true);
  //    }
  //  }  

  public int delete(int no) throws Exception {
    try (PreparedStatement stmt = con.prepareStatement(
        "delete from pms_task where no=?")) {

      stmt.setInt(1, no);
      return stmt.executeUpdate();        
    }
  }

  public List<Task> findByProjectNo(int projectNo) throws Exception {
    ArrayList<Task> list = new ArrayList<>();
    try (PreparedStatement stmt = con.prepareStatement(
        "select "
            + "   t.no,"
            + "   t.content,"
            + "   t.deadline,"
            + "   t.status,"
            + "   m.no as owner_no,"
            + "   m.name as owner_name,"
            + "   p.no as project_no,"
            + "   p.title as project_title"
            + " from pms_task t "
            + "   inner join pms_member m on t.owner=m.no"
            + "   inner join pms_project p on t.project_no=p.no"
            + " where t.project_no=?"
            + " order by p.no desc, t.content asc")) {

      stmt.setInt(1, projectNo);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        Task task = new Task();
        task.setNo(rs.getInt("no"));
        task.setContent(rs.getString("content"));
        task.setDeadline(rs.getDate("deadline"));

        Member owner = new Member();
        owner.setNo(rs.getInt("owner_no"));
        owner.setName(rs.getString("owner_name"));
        task.setOwner(owner);

        task.setStatus(rs.getInt("status"));

        task.setProjectNo(rs.getInt("project_no"));
        task.setProjectTitle(rs.getString("project_title"));

        list.add(task);
      }
      return list;
    }
  }
}  