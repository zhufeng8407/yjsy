package edu.ecnu.yjsy.model.message;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

import edu.ecnu.yjsy.model.auth.Account;

@Transactional(readOnly = true)
@RepositoryRestResource(exported = false)
public interface MessageGroupRepository
        extends JpaRepository<MessageGroup, Long> {

    @Query("select new MessageGroup(id,name) from MessageGroup where owner=?1")
    public List<MessageGroup> findByOwner(Account owner);

}
