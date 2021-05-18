package travelbeeee.PDFLOpjt.repository;

import org.apache.ibatis.annotations.Mapper;
import travelbeeee.PDFLOpjt.domain.User;

import java.util.List;

@Mapper
public interface UserRepository {
    int insert(User member);
    int update(User member);
    int updateAuth(int userId);
    int delete(int userId);
    User selectById(int userId);
    User selectByName(String username);
    User selectByNamePwd(String username, String userpwd);
    int deleteAll();
    List<User> selectByEmail(String email);
}
