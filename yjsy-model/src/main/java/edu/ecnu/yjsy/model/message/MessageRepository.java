package edu.ecnu.yjsy.model.message;

import static edu.ecnu.yjsy.model.constant.Column.MESSAGE_AUTHOR;
import static edu.ecnu.yjsy.model.constant.Table.MESSAGE;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RepositoryRestResource(exported = false)
public interface MessageRepository extends JpaRepository<Message, Long> {

    // FIXME - QUERY REWRITTING
    @Query(value = "select * from t_tz left join t_tz_zh on t_tz_zh.messages=t_tz.id where t_tz_zh.recivers = ?1 and (t_tz.jssj > ?2 or t_tz.jssj = '') and t_tz.kssj < ?3",
            nativeQuery = true)
    List<Message> findByAccountStartEnd(Long AccountId, String start,
            String end);

    // FIXME - QUERY REWRITTING
    @Query(value = "select * from " + MESSAGE + " where " + MESSAGE_AUTHOR
            + " = ?1 order by id desc limit ?2,?3", nativeQuery = true)
    List<Message> findByAuthor(Long authorId, int begin, int size);

}
