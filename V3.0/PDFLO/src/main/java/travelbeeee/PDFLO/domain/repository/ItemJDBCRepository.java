//package travelbeeee.PDFLO.domain.repository;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.RowMapper;
//import org.springframework.stereotype.Repository;
//import travelbeeee.PDFLO.domain.model.dto.ItemViewDto;
//
//import java.util.List;
//
//@Repository
//@RequiredArgsConstructor
//@Slf4j
//public class ItemJDBCRepository {
//
//    private final JdbcTemplate jdbcTemplate;
//
//    /**
//     * Item 테이블과 Thumbnail 테이블, Popular_Item 테이블을 참조해
//     * itemId, title, created_date, avgScore, commentCnt, orderCnt, thumbnailLocation, thumbnailFileName 찾아오기
//     */
//    public List<ItemViewDto> findAllItemViewDtoOrderByPopular(){
//        String sql = "select * from " +
//                "(select item.item_id, item.title, item.created_date, thumbnail.location, thumbnail.salted_file_name " +
//                "from item join thumbnail on item.thumbnail_id = thumbnail.thumbnail_id) as subquery " +
//                "join popular_item on subquery.item_id = popular_item.item_id " +
//                "order by popular_item.score desc;";
//        return jdbcTemplate.query(sql, itemViewDtoRowMapper());
//    }
//
//    private RowMapper<ItemViewDto> itemViewDtoRowMapper(){
//        return (rs, rowNum) -> {
//            ItemViewDto itemViewDto = new ItemViewDto();
//            itemViewDto.setItemId(rs.getLong("item_id"));
//            itemViewDto.setTitle(rs.getString("title"));
//            itemViewDto.setCreatedDate(rs.getTimestamp("created_date"));
//            itemViewDto.setAvgScore(rs.getDouble("comment_avg"));
//            itemViewDto.setCommentCnt(rs.getInt("comment_cnt"));
//            itemViewDto.setOrderCnt(rs.getInt("order_cnt"));
//            itemViewDto.setThumbnailLocation(rs.getString("location"));
//            itemViewDto.setThumbnailFileName(rs.getString("salted_file_name"));
//            return itemViewDto;
//        };
//    }
    
//    /**
//     * Item 테이블과 Comment 테이블과 Thubmanil 테이블을 조인해
//     * 판매중인 상품별로 상품제목 / 상품ID / 상품생성날짜 / 썸네일정보 / 후기 평점 / 후기 갯수 를 Select 하는 쿼리
//     * @return
//     */
//    public List<ItemViewDto> findItemViewDto(){
//        String sql = "select sql1.item_id, sql1.title, sql1.created_date, sql1.avgScore, sql1.commentCnt, thumbnail.location, thumbnail.salted_file_name " +
//                "from " +
//                "(select item.item_id,  item.title, item.thumbnail_id, item.created_date, avg(comment.score) as avgScore, count(*) as commentCnt from comment right join item " +
//                "on item.item_id = comment.item_id group by item.item_id) AS sql1 " +
//                "join thumbnail on sql1.thumbnail_id = thumbnail.thumbnail_id;";
//        /**
//         * select sql1.item_id, sql1.title, sql1.created_date, sql1.avgScore, sql1.commentCnt, thumbnail.location, thumbnail.salted_file_name
//         * from
//         * (select item.item_id,  item.title, item.thumbnail_id, item.created_date, avg(comment.score) as avgScore, count(*) as commentCnt from comment right join item
//         * on item.item_id = comment.item_id group by item.item_id) AS sql1
//         * join thumbnail  on sql1.thumbnail_id = thumbnail.thumbnail_id;
//         */
//        return jdbcTemplate.query(sql, itemViewDtoRowMapper());
//    }
//
//    public List<ItemViewDto> findPopularItemViewDto(){
//        String sql = "select * from (select sql1.item_id, sql1.title, sql1.created_date, sql1.avgScore, sql1.commentCnt, thumbnail.location, thumbnail.salted_file_name from " +
//                "(select item.item_id,  item.title, item.thumbnail_id, item.created_date, avg(comment.score) as avgScore, count(*) as commentCnt from comment right join item " +
//                "on item.item_id = comment.item_id group by item.item_id) AS sql1 " +
//                "join thumbnail  on sql1.thumbnail_id = thumbnail.thumbnail_id) AS sql2 " +
//                "join popular_item on sql2.item_id = popular_item.item_id order by popular_item.score desc;";
//        return jdbcTemplate.query(sql, itemViewDtoRowMapper());
//    }
//}
