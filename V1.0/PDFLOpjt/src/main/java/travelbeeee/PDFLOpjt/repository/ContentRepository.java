package travelbeeee.PDFLOpjt.repository;

import org.apache.ibatis.annotations.Mapper;
import travelbeeee.PDFLOpjt.domain.Content;

import java.util.List;

@Mapper
public interface ContentRepository {
    int insert(Content content);
    int update(Content content);
    int delete(int contentId);
    int deleteAll();
    Content selectById(int contentId);
    List<Content> selectAll();
}
