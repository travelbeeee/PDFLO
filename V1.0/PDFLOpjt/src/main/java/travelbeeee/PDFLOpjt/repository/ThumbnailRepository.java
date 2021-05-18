package travelbeeee.PDFLOpjt.repository;

import org.apache.ibatis.annotations.Mapper;
import travelbeeee.PDFLOpjt.domain.Thumbnail;

@Mapper
public interface ThumbnailRepository {
    int insert(Thumbnail thumbnail);
    int updateContentId(Thumbnail thumbnail);
    int delete(int thumbnailId);
    int deleteAll();
    Thumbnail selectById(int thumbnailId);
}
